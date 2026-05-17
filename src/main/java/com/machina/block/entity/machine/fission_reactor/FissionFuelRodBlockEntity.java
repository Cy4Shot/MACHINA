package com.machina.block.entity.machine.fission_reactor;

import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.block.machine.fission_reactor.FissionFuelRodBlock;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class FissionFuelRodBlockEntity extends MultiblockPartBlockEntity {

	public FissionFuelRodBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.FISSION_FUEL_ROD.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.FISSION_REACTOR;
	}

	@Override
	public boolean isPort(MachinaCap cap) {
		return false;
	}

	@Override
	public int getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	public void form(BlockPos master) {
		setLit(true);
		super.form(master);
	}

	@Override
	public void deform(boolean deleted) {
		if (deleted) {
			setLit(false);
		}
		super.deform(deleted);
	}

	public void setLit(boolean lit) {
		this.level.setBlock(worldPosition, getBlockState().setValue(FissionFuelRodBlock.LIT, lit), 3);
	}
}
