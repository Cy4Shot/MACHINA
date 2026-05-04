package com.machina.block.entity.machine.fission_reactor;

import com.machina.api.block.entity.MultiblockMasterBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class FissionReactorControllerBlockEntity extends MultiblockMasterBlockEntity {

	public FissionReactorControllerBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.FISSION_REACTOR_CONTROLLER.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.FISSION_REACTOR;
	}

	@Override
	public void createStorages() {
//		energyStorage(Side.OUTPUTS);
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
	public void tick() {
		if (this.level != null && this.level.isClientSide())
			return;

		if (!this.formed)
			return;

		super.tick();
	}
}
