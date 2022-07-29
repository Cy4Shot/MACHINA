package com.machina.block.tile;

import com.machina.block.container.TemperatureRegulatorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.HeatHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;

public class TemperatureRegulatorTileEntity extends BaseTileEntity
		implements IHeatTileEntity, IMachinaContainerProvider {

	public float heat = 0;

	public TemperatureRegulatorTileEntity() {
		super(TileEntityInit.TEMPERATURE_REGULATOR.get());
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		float target = HeatHelper.getHeatOffset(worldPosition, level);
		heat = HeatHelper.limitHeat(heat + (target - heat) * 0.05f, level.dimension());
		sync();
	}

	@Override
	public float getHeat() {
		return heat;
	}

	public float propFull() {
		return HeatHelper.propFull(normalizedHeat(), this.level.dimension());
	}

	public float normalizedHeat() {
		return HeatHelper.normalizeHeat(heat, this.level.dimension());
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new TemperatureRegulatorContainer(id, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putFloat("Heat", this.heat);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.heat = compound.getFloat("Heat");
	}

	@Override
	public boolean isGenerator() {
		return true;
	}
}