package com.machina.block.entity.machine.steam_turbine;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MultiblockMasterBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.cap.steam.ISteamHandler;
import com.machina.api.cap.steam.SteamBlockEntity;
import com.machina.api.util.block.BlockHelper;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class SteamTurbineControllerBlockEntity extends MultiblockMasterBlockEntity implements SteamBlockEntity {

	private static final int RF_PER_STEAM = 10;
	private static final float EFFICIENCY_RAMP_UP = 0.02f;
	private static final float EFFICIENCY_RAMP_DOWN = 0.05f;

	private int lastSteamRate = 0;
	private int lastEnergyProduced = 0;
	private float efficiency = 0.0f;

	public SteamTurbineControllerBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.STEAM_TURBINE_CONTROLLER.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.STEAM_TURBINE;
	}

	@Override
	public void createStorages() {
		energyStorage(Side.OUTPUTS);
	}

	@Override
	public int getMaxEnergy() {
		return 1_000_000;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		tag.putInt("lastSteamRate", lastSteamRate);
		tag.putInt("lastEnergyProduced", lastEnergyProduced);
		tag.putFloat("efficiency", efficiency);
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		lastSteamRate = tag.getInt("lastSteamRate");
		lastEnergyProduced = tag.getInt("lastEnergyProduced");
		efficiency = tag.getFloat("efficiency");
		super.loadAdditional(tag, registries);
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide())
			return;

		if (!this.formed) {
			if (efficiency > 0) {
				efficiency = Math.max(0, efficiency - EFFICIENCY_RAMP_DOWN);
				this.setChanged();
			}
			lastSteamRate = 0;
			lastEnergyProduced = 0;
			return;
		}

		int steamReceived = collectSteamFromPorts();
		lastSteamRate = steamReceived;

		if (lastSteamRate > 0) {
			if (efficiency < 1.0f) {
				efficiency = Math.min(1.0f, efficiency + EFFICIENCY_RAMP_UP);
			}

			int energyToGenerate = (int) (lastSteamRate * RF_PER_STEAM * efficiency);

			int energySpace = getMaxEnergy() - getEnergy();
			energyToGenerate = Math.min(energyToGenerate, energySpace);

			if (energyToGenerate > 0) {
				setEnergy(getEnergy() + energyToGenerate);
			}

			lastEnergyProduced = energyToGenerate;
		} else {
			if (efficiency > 0) {
				efficiency = Math.max(0, efficiency - EFFICIENCY_RAMP_DOWN);
			}
			lastEnergyProduced = 0;
		}

		if (getEnergy() > 0) {
			pushEnergyThroughPorts();
		}

		this.setChanged();
		super.tick();
	}

	private void pushEnergyThroughPorts() {
		int energyToSend = Math.min(getEnergy(), 100000);

		for (BlockPos partPos : parts) {
			if (level.getBlockEntity(partPos) instanceof SteamTurbineEnergyPortBlockEntity) {
				for (Direction dir : Direction.values()) {
					BlockPos adjacent = partPos.relative(dir);

					if (parts.contains(adjacent) || adjacent.equals(worldPosition)) {
						continue;
					}

					if (BlockHelper.hasEnergy(level, adjacent, dir.getOpposite())) {
						int sent = BlockHelper.receiveEnergy(level, adjacent, dir.getOpposite(), energyToSend);
						if (sent > 0) {
							consumeEnergy(sent);
							energyToSend -= sent;
							if (energyToSend <= 0) {
								return;
							}
						}
					}
				}
			}
		}
	}

	private int collectSteamFromPorts() {
		int totalSteam = 0;

		for (BlockPos partPos : parts) {
			if (level.getBlockEntity(partPos) instanceof SteamTurbineSteamPortBlockEntity port) {
				totalSteam += port.getAndResetSteamReceived();
			}
		}

		return totalSteam;
	}

	@Override
	public boolean isLit() {
		return formed && efficiency > 0;
	}

	@Override
	public ISteamHandler getSteamCap() {
		return null;
	}

	public int getLastSteamRate() {
		return lastSteamRate;
	}

	public int getLastEnergyProduced() {
		return lastEnergyProduced;
	}

	public float getEfficiency() {
		return efficiency;
	}
}
