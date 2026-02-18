package com.machina.rocket;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.api.cap.fluid.FluidHandlerEntity;
import com.machina.api.cap.fluid.MachinaEntityTank;
import com.machina.api.item.RocketItem;
import com.machina.api.network.s2c.S2CCinematicLand;
import com.machina.api.network.s2c.S2CCinematicLaunch;
import com.machina.api.network.s2c.S2CRocketScreenOpen;
import com.machina.api.rocket.RocketCosts;
import com.machina.api.rocket.RocketProps;
import com.machina.api.starchart.Starchart;
import com.machina.api.util.PlanetHelper;
import com.machina.api.util.reflect.MachinaStreamCodecs;
import com.machina.api.util.reflect.MachinaStreamCodecs.HasId;
import com.machina.client.model.rocket.RocketModel;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.ItemInit;
import com.machina.world.PlanetRegistrationHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
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
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.network.PacketDistributor;

public class RocketEntity extends Entity implements ContainerListener, HasCustomInventoryScreen, ContainerEntity,
		FluidHandlerEntity, IEntityWithComplexSpawn {

	public enum RocketStage implements HasId {
		NONE, TAKEOFF, LANDING;

		@Override
		public int getId() {
			return this.ordinal();
		}

		public static final StreamCodec<ByteBuf, RocketStage> STREAM_CODEC = MachinaStreamCodecs
				.enumCodec(RocketStage.class);

		public static final EntityDataSerializer<RocketStage> SERIALIZER = EntityDataSerializer
				.forValueType(STREAM_CODEC);
	}

	private static final int DEFAULT_SLOTS = 2;
	private static final String TAG_PROPS = "rocket_props";
	private static final String TAG_COSTS = "rocket_costs";
	private static final String TAG_DESTINATION = "rocket_destination";
	private static final String TAG_FUEL = "rocket_tank_fuel";
	private static final String TAG_COOL = "rocket_tank_cool";
	private static final String TAG_ITEMS = "Items";
	private static final String TAG_SLOT = "Slot";

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

	private CompoundTag pendingFuelNBT;
	private CompoundTag pendingCoolNBT;

	@Nullable
	private ResourceKey<LootTable> lootTable;
	private long lootTableSeed;

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
		}
		this.createFluidInventory(props);
	}

	@SuppressWarnings("removal")
	protected void createFluidInventory(RocketProps props) {
		if (this.fuelTank != null && this.coolTank != null)
			return;

		//@formatter:off
        this.fuelTank = new MachinaEntityTank(this, props.fuelStorage(),
                stack -> stack.isFluidEqual(props.fuelStack()),
                FUEL_TANK, () -> { });

        this.coolTank = new MachinaEntityTank(this, props.coolantStorage(),
                stack -> stack.isFluidEqual(props.coolantStack()),
                COOL_TANK, () -> { });
        //@formatter:on
	}

	@Override
	protected void defineSynchedData(Builder builder) {
		builder.define(STAGE, RocketStage.NONE);
		builder.define(PROPS, RocketProps.NULL);
		builder.define(COSTS, RocketCosts.NULL);
		builder.define(DESTINATION, Level.OVERWORLD);
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
		if (props != null && !this.level().isClientSide()) {
			int transferRate = 1000; // TODO: Make this configurable
			Container inv = getOrCreateInventory();
			IFluidHandlerItem h1 = inv.getItem(0).getCapability(Capabilities.FluidHandler.ITEM);
			if (h1 != null) {
				int amount = Math.min(transferRate, this.fuelTank.getSpace());
				FluidStack result = h1.drain(new FluidStack(props.fuelType(), amount), FluidAction.EXECUTE);
				if (!result.isEmpty()) {
					this.fuelTank.fill(result, FluidAction.EXECUTE);
					inv.setItem(0, h1.getContainer());
				}
			}
			IFluidHandlerItem h2 = inv.getItem(1).getCapability(Capabilities.FluidHandler.ITEM);
			if (h2 != null) {
				int amount = Math.min(transferRate, this.coolTank.getSpace());
				FluidStack result = h2.drain(new FluidStack(props.coolantType(), amount), FluidAction.EXECUTE);
				if (!result.isEmpty()) {
					this.coolTank.fill(result, FluidAction.EXECUTE);
					inv.setItem(1, h2.getContainer());
				}
			}
		}
	}

	@Override
	public MachinaEntityTank getTank(int id) {
		return switch (id) {
		case FUEL_TANK -> this.fuelTank;
		case COOL_TANK -> this.coolTank;
		default -> null;
		};
	}

	@Override
	protected void readAdditionalSaveData(@NotNull CompoundTag tag) {

		if (tag.contains(TAG_PROPS)) {
			RocketProps props = RocketProps.fromNBT(tag.getCompound(TAG_PROPS));
			setProps(props); // this recreates inventory + tanks safely
		}

		RocketProps props = getProps();
		if (props != null) {
			createInventory(props);
			createFluidInventory(props);
		}

		if (tag.contains(TAG_DESTINATION)) {
			this.entityData.set(DESTINATION,
					ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString(TAG_DESTINATION))));
		}

		if (tag.contains(TAG_COSTS)) {
			setCosts(RocketCosts.fromNBT(tag.getCompound(TAG_COSTS)));
		}

		if (fuelTank != null && tag.contains(TAG_FUEL)) {
			fuelTank.readFromNBT(this.registryAccess(), tag.getCompound(TAG_FUEL));
		}

		if (coolTank != null && tag.contains(TAG_COOL)) {
			coolTank.readFromNBT(this.registryAccess(), tag.getCompound(TAG_COOL));
		}

		if (this.inventory != null && tag.contains(TAG_ITEMS)) {
			ListTag listtag = tag.getList(TAG_ITEMS, CompoundTag.TAG_COMPOUND);

			for (int i = 0; i < listtag.size(); ++i) {
				CompoundTag compoundtag = listtag.getCompound(i);
				int slot = compoundtag.getByte(TAG_SLOT) & 255;
				if (slot >= 0 && slot < this.inventory.getContainerSize()) {
					this.inventory.setItem(slot, ItemStack.parse(this.registryAccess(), compoundtag).get());
				}
			}
		}
	}

	@Override
	protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
		RocketProps props = getProps();
		if (props != null) {
			tag.put(TAG_PROPS, props.toNBT());
		}

		tag.put(TAG_COSTS, getCosts().toNBT());
		tag.putString(TAG_DESTINATION, getDestination().location().toString());

		if (fuelTank != null) {
			tag.put(TAG_FUEL, fuelTank.writeToNBT(this.registryAccess(), new CompoundTag()));
		}

		if (coolTank != null) {
			tag.put(TAG_COOL, coolTank.writeToNBT(this.registryAccess(), new CompoundTag()));
		}

		if (this.inventory != null) {
			ListTag listtag = new ListTag();

			for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
				ItemStack stack = this.inventory.getItem(i);

				if (!stack.isEmpty()) {
					CompoundTag compoundtag = new CompoundTag();
					compoundtag.putByte(TAG_SLOT, (byte) i);
					stack.save(this.registryAccess(), compoundtag);
					listtag.add(compoundtag);
				}
			}

			tag.put(TAG_ITEMS, listtag);
		}
	}

	@Override
	public @NotNull InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			this.openCustomInventoryScreen(player);
			return InteractionResult.sidedSuccess(this.level().isClientSide());
		}
		return super.interact(player, hand);
	}

	@Override
	public void openCustomInventoryScreen(@NotNull Player p) {
		if (this.level().isClientSide()) {
			return;
		}
		ServerPlayer player = (ServerPlayer) p;
		if (player.containerMenu != player.inventoryMenu) {
			player.closeContainer();
		}

		player.nextContainerCounter();
		PacketDistributor.sendToPlayer(player,
				new S2CRocketScreenOpen(player.containerCounter, this.inventory.getContainerSize(), getId()));
		player.containerMenu = new RocketMenu(player.containerCounter, player.getInventory(), this.inventory, this);
		player.initMenu(player.containerMenu);
		NeoForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		buffer.writeNbt(fuelTank.writeToNBT(this.registryAccess(), new CompoundTag()));
		buffer.writeNbt(coolTank.writeToNBT(this.registryAccess(), new CompoundTag()));
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buffer) {
		pendingFuelNBT = buffer.readNbt();
		pendingCoolNBT = buffer.readNbt();
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);

		if (key.equals(PROPS)) {

			createInventory(getProps()); // rebuild tanks correctly

			if (pendingFuelNBT != null && fuelTank != null) {
				fuelTank.readFromNBT(this.registryAccess(), pendingFuelNBT);
				pendingFuelNBT = null;
			}

			if (pendingCoolNBT != null && coolTank != null) {
				coolTank.readFromNBT(this.registryAccess(), pendingCoolNBT);
				pendingCoolNBT = null;
			}
		}
	}

	@Override
	public void containerChanged(@NotNull Container container) {
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
				if (!itemstack.isEmpty()) {
					this.spawnAtLocation(itemstack);
				}
			}
		}

		this.kill();
	}

	@Override
	protected @NotNull AABB makeBoundingBox() {
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
		PacketDistributor.sendToPlayer(player, new S2CCinematicLaunch(this.getId()));
	}

	public void tryLand(ServerPlayer player) {
		this.entityData.set(STAGE, RocketStage.LANDING);
		PacketDistributor.sendToPlayer(player, new S2CCinematicLand(this.getId()));
	}

	private static DimensionTransition createCustomDimensionTransition(ServerLevel level, Entity entity, Vec3 speed,
			float yRot, float xRot, DimensionTransition.PostDimensionTransition postDimensionTransition) {
		BlockPos origin = entity.blockPosition();
		level.getChunk(origin);
		int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
		EntityDimensions dimensions = entity.getDimensions(entity.getPose());
		Vec3 spawnPos = new Vec3(origin.getX() + 0.5, surfaceY + 1, origin.getZ() + 0.5);
		Vec3 safePos = PortalShape.findCollisionFreePosition(spawnPos, level, entity, dimensions);
		return new DimensionTransition(level, safePos, speed, yRot, xRot, postDimensionTransition);
	}

	public void completeLaunch(ServerPlayer player) {
		RocketCosts costs = getCosts();
		if (costs != null) {
			this.fuelTank.drain(costs.fuelRequired(), FluidAction.EXECUTE);
			this.coolTank.drain(costs.coolantRequired(), FluidAction.EXECUTE);
		}

		ResourceKey<Level> dst = getDestination();
		ServerLevel planet = PlanetRegistrationHandler.createPlanet(player.getServer(), PlanetHelper.getIdLevel(dst));
		this.changeDimension(
				createCustomDimensionTransition(planet, this, Vec3.ZERO, this.yRot, this.xRot, transported -> {
					PlanetRegistrationHandler.sendPlayerToDimension(player, planet, transported.blockPosition());
					if (transported instanceof RocketEntity rocket) {
						rocket.tryLand(player);
					}
				}));
	}

	public void completeLand(ServerPlayer player) {
		this.entityData.set(STAGE, RocketStage.NONE);
		setDestination(Level.OVERWORLD);
	}

	@Override
	public int getContainerSize() {
		return this.getOrCreateInventory().getContainerSize();
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.getOrCreateInventory().getItem(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return this.getOrCreateInventory().removeItem(slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.getOrCreateInventory().removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.getOrCreateInventory().setItem(slot, stack);
	}

	@Override
	public void setChanged() {
		this.getOrCreateInventory().setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return this.getOrCreateInventory().stillValid(player);
	}

	@Override
	public void clearContent() {
		this.getOrCreateInventory().clearContent();
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new RocketMenu(containerId, playerInventory, this.inventory, this);
	}

	@Override
	public ResourceKey<LootTable> getLootTable() {
		return this.lootTable;
	}

	@Override
	public void setLootTable(ResourceKey<LootTable> lootTable) {
		this.lootTable = lootTable;
	}

	@Override
	public long getLootTableSeed() {
		return this.lootTableSeed;
	}

	@Override
	public void setLootTableSeed(long lootTableSeed) {
		this.lootTableSeed = lootTableSeed;
	}

	@Override
	public NonNullList<ItemStack> getItemStacks() {
		return this.getOrCreateInventory().getItems();
	}

	@Override
	public void clearItemStacks() {
		this.clearContent();
		this.setChanged();
	}
}
