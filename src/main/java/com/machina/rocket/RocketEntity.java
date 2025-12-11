package com.machina.rocket;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.api.item.RocketItem;
import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CRocketScreenOpen;
import com.machina.api.rocket.RocketCosts;
import com.machina.api.rocket.RocketProps;
import com.machina.api.starchart.Starchart;
import com.machina.client.model.rocket.RocketModel;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.wrapper.InvWrapper;

public class RocketEntity extends Entity implements ContainerListener, HasCustomInventoryScreen {

    private static final int DEFAULT_SLOTS = 2;
    private static final String TAG_PROPS = "rocket_props";
    private static final String TAG_COSTS = "rocket_costs";
    private static final String TAG_DESTINATION = "rocket_destination";

    // @formatter:off
    private static final EntityDataAccessor<RocketProps> PROPS = SynchedEntityData.defineId(RocketEntity.class, RocketProps.SERIALIZER);
    private static final EntityDataAccessor<RocketCosts> COSTS = SynchedEntityData.defineId(RocketEntity.class, RocketCosts.SERIALIZER);
    private static final EntityDataAccessor<ResourceKey<Level>> DESTINATION = SynchedEntityData.defineId(RocketEntity.class, DimensionSerializer.SERIALIZER);
    // @formatter:on

    protected SimpleContainer inventory;

    private LazyOptional<?> itemHandler = null;

    public RocketEntity(EntityType<? extends RocketEntity> t, Level l) {
        super(t, l);
    }

    public RocketEntity(Level level, RocketProps props) {
        this(EntityTypeInit.ROCKET.get(), level);
        this.setProps(props);
    }

    protected void createInventory(RocketProps props) {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(props.slots() + DEFAULT_SLOTS);
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }
        this.inventory.addListener(this);
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PROPS, RocketProps.NULL);
        this.entityData.define(COSTS, RocketCosts.NULL);
        this.entityData.define(DESTINATION, Level.OVERWORLD);
    }

    @Override
    public void tick() {
        super.tick();

        // Apply Gravity (TODO: Respect Planet Gravity)
        double d0 = 0.08D;
        float f3 = 0.91F;
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 vec35 = this.getDeltaMovement();
        double d2 = vec35.y;
        if (!this.isNoGravity()) {
            d2 -= d0;
        }
        this.setDeltaMovement(vec35.x * (double) f3, d2 * (double) 0.98F, vec35.z * (double) f3);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        if (tag.contains(TAG_PROPS)) {
            setProps(RocketProps.fromNBT(tag.getCompound(TAG_PROPS)));
        }
        this.entityData.set(DESTINATION,
                ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString(TAG_DESTINATION))));
        setCosts(RocketCosts.fromNBT(tag.getCompound(TAG_COSTS)));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        RocketProps props = getProps();
        if (props != null) {
            tag.put(TAG_PROPS, props.toNBT());
        }
        tag.put(TAG_COSTS, getCosts().toNBT());
        tag.putString(TAG_DESTINATION, getDestination().location().toString());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER && this.isAlive() && itemHandler != null)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (itemHandler != null) {
            LazyOptional<?> oldHandler = itemHandler;
            itemHandler = null;
            oldHandler.invalidate();
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.interact(player, hand);
    }

    @Override
    public void openCustomInventoryScreen(Player p) {
        if (this.level().isClientSide()) {
            return;
        }
        ServerPlayer player = (ServerPlayer) p;
        if (player.containerMenu != player.inventoryMenu) {
            player.closeContainer();
        }

        player.nextContainerCounter();
        PacketSender.sendToClient(player,
                new S2CRocketScreenOpen(player.containerCounter, this.inventory.getContainerSize(), getId()));
        player.containerMenu = new RocketMenu(player.containerCounter, player.getInventory(), this.inventory, this);
        player.initMenu(player.containerMenu);
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
    }

    @Override
    public void containerChanged(Container container) {
    }

    public void setProps(RocketProps props) {
        this.entityData.set(PROPS, props);
        this.createInventory(props);
    }

    public RocketProps getProps() {
        return this.entityData.get(PROPS);
    }

    public void setCosts(RocketCosts costs) {
        this.entityData.set(COSTS, costs);
    }

    public RocketCosts getCosts() {
        return this.entityData.get(COSTS);
    }

    public void setDestination(ResourceKey<Level> dim) {
        this.entityData.set(DESTINATION, dim);
        this.setCosts(RocketCosts.from(Starchart.system(this.level()), this.getProps(), this.level().dimension(), dim));
    }

    public ResourceKey<Level> getDestination() {
        return this.entityData.get(DESTINATION);
    }

    @OnlyIn(Dist.CLIENT)
    public RocketModel getModel() {
        RocketProps props = getProps();
        if (props == null)
            return null;
        return new RocketModel(props.parts());
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public void destroyRocket() {
        ItemStack item = new ItemStack(ItemInit.ROCKET.get());
        RocketItem.setProperties(item, getProps());
        this.spawnAtLocation(item);

        if (this.inventory != null) {
            for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                    this.spawnAtLocation(itemstack);
                }
            }
        }

        this.kill();
    }

    @Override
    protected AABB makeBoundingBox() {
        RocketProps props = getProps();
        if (props == null)
            return super.makeBoundingBox();
        return props.boundingBox().move(this.position());
    }
}
