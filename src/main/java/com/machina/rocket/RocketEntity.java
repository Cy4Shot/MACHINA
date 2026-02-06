package com.machina.rocket;

import java.util.function.Function;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.google.common.util.concurrent.Runnables;
import com.machina.api.cap.fluid.FluidHandlerEntity;
import com.machina.api.cap.fluid.MachinaEntityTank;
import com.machina.api.item.RocketItem;
import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CCinematicLand;
import com.machina.api.network.s2c.S2CCinematicLaunch;
import com.machina.api.network.s2c.S2CRocketScreenOpen;
import com.machina.api.rocket.RocketCosts;
import com.machina.api.rocket.RocketProps;
import com.machina.api.starchart.Starchart;
import com.machina.api.util.PlanetHelper;
import com.machina.client.model.rocket.RocketModel;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.ItemInit;
import com.machina.world.PlanetRegistrationHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.wrapper.InvWrapper;

public class RocketEntity extends Entity implements ContainerListener, HasCustomInventoryScreen, FluidHandlerEntity {

    public enum RocketStage {
        NONE,
        TAKEOFF,
        LANDING;

        public static final EntityDataSerializer<RocketStage> SERIALIZER = EntityDataSerializer
                .simpleEnum(RocketStage.class);
    }

    private static final int DEFAULT_SLOTS = 2;
    private static final String TAG_PROPS = "rocket_props";
    private static final String TAG_COSTS = "rocket_costs";
    private static final String TAG_DESTINATION = "rocket_destination";
    private static final String TAG_FUEL = "rocket_tank_fuel";
    private static final String TAG_COOL = "rocket_tank_cool";

    private static final int FUEL_TANK = 0;
    private static final int COOL_TANK = 1;

    // @formatter:off
    private static final EntityDataAccessor<RocketStage> STAGE = SynchedEntityData.defineId(RocketEntity.class, RocketStage.SERIALIZER);
    private static final EntityDataAccessor<RocketProps> PROPS = SynchedEntityData.defineId(RocketEntity.class, RocketProps.SERIALIZER);
    private static final EntityDataAccessor<RocketCosts> COSTS = SynchedEntityData.defineId(RocketEntity.class, RocketCosts.SERIALIZER);
    private static final EntityDataAccessor<ResourceKey<Level>> DESTINATION = SynchedEntityData.defineId(RocketEntity.class, DimensionSerializer.SERIALIZER);
    // @formatter:on

    protected SimpleContainer inventory;
    protected MachinaEntityTank fuelTank;
    protected MachinaEntityTank coolTank;

    private LazyOptional<InvWrapper> itemHandler = null;

    public RocketEntity(EntityType<? extends RocketEntity> t, Level l) {
        super(t, l);
    }

    public RocketEntity(Level level, RocketProps props) {
        this(EntityTypeInit.ROCKET.get(), level);
        this.setProps(props);
    }
    
    public SimpleContainer getOrCreateInventory() {
        createInventory(getProps());
        return this.inventory;
    }

    protected void createInventory(RocketProps props) {
        SimpleContainer sc = this.inventory;
        int targetSize = DEFAULT_SLOTS + props.slots();
        if (sc == null || sc.getContainerSize() != targetSize) {
            this.inventory = new SimpleContainer(targetSize);
            if (sc != null) {
                sc.removeListener(this);
                int i = Math.min(sc.getContainerSize(), this.inventory.getContainerSize());

                for (int j = 0; j < i; ++j) {
                    ItemStack itemstack = sc.getItem(j);
                    if (!itemstack.isEmpty()) {
                        this.inventory.setItem(j, itemstack.copy());
                    }
                }
            }
            this.inventory.addListener(this);
            this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
        }
        this.createFluidInventory(props);
    }

    protected void createFluidInventory(RocketProps props) {
        //@formatter:off
        if (this.fuelTank == null) {
            this.fuelTank = new MachinaEntityTank(this, props.fuelStorage(), stack -> stack.isFluidEqual(props.fuelStack()), FUEL_TANK, Runnables.doNothing());
        }
        if (this.coolTank == null) {
            this.coolTank = new MachinaEntityTank(this, props.coolantStorage(), stack -> stack.isFluidEqual(props.coolantStack()), COOL_TANK, Runnables.doNothing());
        }
        //@formatter:on
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(STAGE, RocketStage.NONE);
        this.entityData.define(PROPS, RocketProps.NULL);
        this.entityData.define(COSTS, RocketCosts.NULL);
        this.entityData.define(DESTINATION, Level.OVERWORLD);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.entityData.get(STAGE).equals(RocketStage.NONE)) {
            return;
        }

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

        // Insert fuel & coolant
        RocketProps props = getProps();
        if (props != null) {
            int transferRate = 1000; // TODO: Make this configurable
            getOrCreateInventory().getItem(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                int amount = Math.min(transferRate, this.fuelTank.getSpace());
                FluidStack result = handler.drain(new FluidStack(props.fuelType(), amount), FluidAction.EXECUTE);
                this.fuelTank.fill(result, FluidAction.EXECUTE);
            });
            getOrCreateInventory().getItem(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                int amount = Math.min(transferRate, this.coolTank.getSpace());
                FluidStack result = handler.drain(new FluidStack(props.coolantType(), amount), FluidAction.EXECUTE);
                this.coolTank.fill(result, FluidAction.EXECUTE);
            });
        }
    }

    @Override
    public MachinaEntityTank getTank(int id) {
        this.createFluidInventory(getProps());
        return switch (id) {
            case FUEL_TANK -> this.fuelTank;
            case COOL_TANK -> this.coolTank;
            default -> null;
        };
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        if (tag.contains(TAG_PROPS)) {
            setProps(RocketProps.fromNBT(tag.getCompound(TAG_PROPS)));
        }
        this.entityData.set(DESTINATION,
                ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString(TAG_DESTINATION))));
        setCosts(RocketCosts.fromNBT(tag.getCompound(TAG_COSTS)));
        fuelTank.readFromNBT(tag.getCompound(TAG_FUEL));
        coolTank.readFromNBT(tag.getCompound(TAG_COOL));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        RocketProps props = getProps();
        if (props != null) {
            tag.put(TAG_PROPS, props.toNBT());
        }
        tag.put(TAG_COSTS, getCosts().toNBT());
        tag.putString(TAG_DESTINATION, getDestination().location().toString());
        tag.put(TAG_FUEL, fuelTank.writeToNBT(new CompoundTag()));
        tag.put(TAG_COOL, coolTank.writeToNBT(new CompoundTag()));
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
            LazyOptional<InvWrapper> oldHandler = itemHandler;
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

    public boolean fuelSatisfied() {
        this.createFluidInventory(getProps());
        RocketCosts costs = getCosts();
        return this.fuelTank.getFluidAmount() >= costs.fuelRequired()
                && this.coolTank.getFluidAmount() >= costs.coolantRequired();
    }

    public boolean isPossible() {
        return getCosts().possible();
    }

    public void tryLaunch(ServerPlayer player) {
        this.entityData.set(STAGE, RocketStage.TAKEOFF);
        PacketSender.sendToClient(player, new S2CCinematicLaunch(this.getId()));
    }

    public void tryLand(ServerPlayer player) {
        this.entityData.set(STAGE, RocketStage.LANDING);
        PacketSender.sendToClient(player, new S2CCinematicLand(this.getId()));
    }

    public void completeLaunch(ServerPlayer player) {
        ResourceKey<Level> dst = getDestination();
        ServerLevel planet = PlanetRegistrationHandler.createPlanet(player.getServer(), PlanetHelper.getIdLevel(dst));
        Entity transported = this.changeDimension(planet, new ITeleporter() {
            @Override
            public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld,
                    Function<ServerLevel, PortalInfo> defaultPortalInfo) {
                BlockPos assp = entity.blockPosition();
                destWorld.getChunk(assp); // Preload the chunk
                int assh = destWorld.getHeight(Types.WORLD_SURFACE_WG, assp.getX(), assp.getZ());
                Vec3 pos = new BlockPos(assp.getX(), assh + 1, assp.getZ()).getCenter();
                return new PortalInfo(pos, Vec3.ZERO, entity.getYRot(), entity.getXRot());
            }
        });

        PlanetRegistrationHandler.sendPlayerToDimension(player, planet, transported.blockPosition());
        if (transported instanceof RocketEntity rocket) {
            rocket.tryLand(player);
        }
    }

    public void completeLand(ServerPlayer player) {
        this.entityData.set(STAGE, RocketStage.NONE);
        setDestination(Level.OVERWORLD);
    }
}
