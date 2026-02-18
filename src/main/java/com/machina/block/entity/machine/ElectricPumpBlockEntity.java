package com.machina.block.entity.machine;

import com.google.common.base.Predicates;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class ElectricPumpBlockEntity extends MachinaBlockEntity {

	public ElectricPumpBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ElectricPumpBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ELECTRIC_PUMP.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
	}

	@Override
	public void tick() {
		if (this.level != null && this.level.isClientSide()) {
			return;
		}

		// TODO: Config
		int energyPerMb = 2;

		BlockPos fpos = this.getBlockPos().below();
		FluidState fluid = this.level.getFluidState(fpos);

		// Is block below a fluid?
		if (fluid.isSource()) {
			FluidStack stored = this.getFluid(0);
			if (stored.isEmpty() || stored.getFluid().equals(fluid.getType())) {
				int simulated = this.fill(0, new FluidStack(fluid.getType(), 1000), FluidAction.SIMULATE);
				simulated = Math.min(simulated, this.consumeEnergySim(energyPerMb * simulated) / energyPerMb);
				if (simulated == 1000) {
					this.level.setBlock(fpos, Blocks.AIR.defaultBlockState(), 3);
					this.fill(0, new FluidStack(fluid.getType(), simulated), FluidAction.EXECUTE);
					this.consumeEnergy(simulated * energyPerMb);

				}
			}
		}

	}

	@Override
	public boolean activeModel() {
		return true;
	}

	@Override
	public int getMaxEnergy() {
		// TODO Config
		return 10_000_000;
	}
}
