package com.machina.block.entity.machine;

import java.util.List;

import com.machina.api.cap.fluid.MachinaTank;
import com.machina.api.cap.fluid.MirroredTank;
import com.machina.api.cap.fluid.MirroredTank.BlockEntityMirrorable;
import com.machina.api.network.s2c.S2CRocketRefuelingStationSync;
import com.machina.registration.init.BlockEntityInit;
import com.machina.rocket.RocketEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;

public class RocketRefuelingStationBlockEntity extends TankBlockEntity implements BlockEntityMirrorable {

	private static final String TAG_TRACKED_ROCKET = "tracked_rocket";
	private static final String TAG_SELECTED_TANK = "selected_tank";

	public static final int FUEL_TANK = 0;
	public static final int COOLANT_TANK = 1;

	private static final int SEARCH_RADIUS = 5;
	private static final int SEARCH_RADIUS_SQ = SEARCH_RADIUS * SEARCH_RADIUS;
	private static final int RESCAN_INTERVAL = 10;

	private int trackedRocketId = -1;
	private int selectedTank = FUEL_TANK;
	private int scanCooldown;
	private int syncCooldown;
	private boolean forceRescan;

	private int lastSyncedTrackedRocketId = Integer.MIN_VALUE;
	private int lastSyncedSelectedTank = Integer.MIN_VALUE;

	private final MirroredTank<?> mirroredTank = new MirroredTank<>(this);

	public RocketRefuelingStationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public RocketRefuelingStationBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ROCKET_REFUELING_STATION.get(), pos, state);
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide()) {
			return;
		}

		RocketEntity trackedBefore = getTrackedRocket();

		boolean shouldRescan = this.forceRescan || --this.scanCooldown <= 0 || !isTrackedRocketValid(trackedBefore);
		if (shouldRescan) {
			rescanRockets();
		}

		super.tick();

		syncTrackingData();
	}

	@Override
	public void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		this.trackedRocketId = tag.getInt(TAG_TRACKED_ROCKET);
		this.selectedTank = normalizeTank(tag.getInt(TAG_SELECTED_TANK));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		tag.putInt(TAG_TRACKED_ROCKET, this.trackedRocketId);
		tag.putInt(TAG_SELECTED_TANK, this.selectedTank);
		super.saveAdditional(tag, registries);
	}

	public void clientSyncState(int trackedRocketId, int selectedTank) {
		this.trackedRocketId = trackedRocketId;
		this.selectedTank = normalizeTank(selectedTank);
	}

	public void setSelectedTank(int selectedTank) {
		int normalized = normalizeTank(selectedTank);
		if (this.selectedTank != normalized) {
			this.selectedTank = normalized;
			this.setChanged();
		}
	}

	public int getTrackedRocketId() {
		return this.trackedRocketId;
	}

	public int getSelectedTank() {
		return this.selectedTank;
	}

	public boolean isFuelTankSelected() {
		return this.selectedTank == FUEL_TANK;
	}

	public boolean hasTrackedRocket() {
		return this.trackedRocketId != -1;
	}

	public BlockPos getTrackedRocketPos() {
		RocketEntity tracked = getTrackedRocket();
		return tracked == null ? null : tracked.blockPosition();
	}

	@Override
	public MachinaTank<?> getTank(int id) {
		return this.mirroredTank;
	}

	@Override
	public FluidStack getFluid(int tank) {
		FluidTank selected = getMirrorableTank();
		return selected == null ? FluidStack.EMPTY : selected.getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		FluidTank selected = getMirrorableTank();
		return selected == null ? 0 : selected.getCapacity();
	}

	@Override
	public int fill(int tank, FluidStack resource, FluidAction action) {
		FluidTank selected = getMirrorableTank();
		return selected == null ? 0 : selected.fill(resource, action);
	}

	@Override
	public FluidStack drain(int tank, int amount, FluidAction action) {
		FluidTank selected = getMirrorableTank();
		return selected == null ? FluidStack.EMPTY : selected.drain(amount, action);
	}

	@Override
	public int fill(Direction dir, FluidStack resource, FluidAction action) {
		if (dir == null || this.fluidSides.get(0)[dir.ordinal()].isInput()) {
			return fill(0, resource, action);
		}
		return 0;
	}

	@Override
	public FluidStack drain(Direction dir, FluidStack resource, FluidAction action) {
		if (dir == null || this.fluidSides.get(0)[dir.ordinal()].isOutput()) {
			FluidTank selected = getMirrorableTank();
			return selected == null ? FluidStack.EMPTY : selected.drain(resource, action);
		}
		return FluidStack.EMPTY;
	}

	@Override
	public FluidStack drain(Direction dir, int maxDrain, FluidAction action) {
		if (dir == null || this.fluidSides.get(0)[dir.ordinal()].isOutput()) {
			return drain(0, maxDrain, action);
		}
		return FluidStack.EMPTY;
	}

	private void rescanRockets() {
		this.forceRescan = false;
		this.scanCooldown = RESCAN_INTERVAL;

		AABB area = new AABB(Vec3.atCenterOf(this.worldPosition), Vec3.atCenterOf(this.worldPosition))
				.inflate(SEARCH_RADIUS);
		List<RocketEntity> rockets = this.level.getEntitiesOfClass(RocketEntity.class, area,
				r -> r.isAlive() && inRange(r));

		this.trackedRocketId = rockets.stream()
				.min((a, b) -> Double.compare(
						this.worldPosition.distToCenterSqr(a.getX(), a.getY(), a.getZ()),
						this.worldPosition.distToCenterSqr(b.getX(), b.getY(), b.getZ())))
				.map(RocketEntity::getId)
				.orElse(-1);
	}

	private RocketEntity getTrackedRocket() {
		if (!(this.level instanceof Level l) || this.trackedRocketId < 0) {
			return null;
		}

		if (l.getEntity(this.trackedRocketId) instanceof RocketEntity rocket && rocket.isAlive() && inRange(rocket)) {
			return rocket;
		}

		return null;
	}

	private boolean isTrackedRocketValid(RocketEntity trackedBefore) {
		if (trackedBefore == null) {
			return false;
		}
		return trackedBefore.isAlive() && inRange(trackedBefore);
	}

	private boolean inRange(RocketEntity rocket) {
		return this.worldPosition.distToCenterSqr(rocket.getX(), rocket.getY(), rocket.getZ()) <= SEARCH_RADIUS_SQ;
	}

	private void syncTrackingData() {
		boolean changed = this.trackedRocketId != this.lastSyncedTrackedRocketId
				|| this.selectedTank != this.lastSyncedSelectedTank;
		boolean periodicSync = --this.syncCooldown <= 0;

		if (!changed && !periodicSync) {
			return;
		}

		if (periodicSync) {
			this.syncCooldown = 20;
		}

		this.lastSyncedTrackedRocketId = this.trackedRocketId;
		this.lastSyncedSelectedTank = this.selectedTank;

		if (this.level instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(this.worldPosition),
					new S2CRocketRefuelingStationSync(this.worldPosition, this.trackedRocketId, this.selectedTank));
		}

		this.setChanged();
	}

	private int normalizeTank(int tank) {
		return tank == COOLANT_TANK ? COOLANT_TANK : FUEL_TANK;
	}

	@Override
	public FluidTank getMirrorableTank() {
		RocketEntity tracked = getTrackedRocket();
		if (tracked == null) {
			return null;
		}
		return tracked.getTank(this.selectedTank);
	}
}
