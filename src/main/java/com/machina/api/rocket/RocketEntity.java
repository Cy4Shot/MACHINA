package com.machina.api.rocket;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.model.rocket.RocketModel;
import com.machina.api.item.RocketItem;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;

public class RocketEntity extends Entity implements ContainerListener, HasCustomInventoryScreen {

    private static final int DEFAULT_SLOTS = 2;
    private static final String TAG_PROPS = "rocket_props";

    private static final EntityDataAccessor<RocketProps> PROPS = SynchedEntityData.defineId(RocketEntity.class,
            RocketProps.SERIALIZER);

    protected SimpleContainer inventory;

    private LazyOptional<?> itemHandler = null;

    public RocketEntity(EntityType<? extends RocketEntity> t, Level l) {
        super(t, l);
    }

    public RocketEntity(Level level, RocketProps props) {
        this(EntityTypeInit.ROCKET.get(), level);
        this.createInventory(props);
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
    }

    @Override
    public void tick() {
        super.tick();

        // Apply Gravity
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
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        RocketProps props = getProps();
        if (props != null) {
            tag.put(TAG_PROPS, props.toNBT());
        }
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
    public void openCustomInventoryScreen(Player player) {
        player.openMenu(new MenuProvider() {
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
                return null;
            }

            @Override
            public Component getDisplayName() {
                return Component.empty();
            }
        });
    }

    @Override
    public void containerChanged(Container container) {
    }

    public void setProps(RocketProps props) {
        this.entityData.set(PROPS, props);
    }

    public RocketProps getProps() {
        return this.entityData.get(PROPS);
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
