package com.machina.block.entity.machine;

import java.util.Objects;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.block.BlockHelper;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.DataMapsInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChemicalGeneratorBlockEntity extends MachinaBlockEntity {

	public ChemicalGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ChemicalGeneratorBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.CHEMICAL_GENERATOR.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.OUTPUTS);
		fluidStorage(16_000, f -> f.getFluidHolder().getData(DataMapsInit.CHEMICAL_BURNABLE) != null, Side.INPUTS);
	}

	@Override
	public boolean isLit() {
		return !getFluid(0).isEmpty() && !this.isEnergyFull();
	}

	public int getRate() {
		FluidStack fluid = getFluid(0).copy();
		return Objects.requireNonNullElse(fluid.getFluidHolder().getData(DataMapsInit.CHEMICAL_BURNABLE), 0);
	}

	@Override
	public void tick() {
		if (this.level != null && this.level.isClientSide())
			return;

		FluidStack fluid = getFluid(0).copy();
		int rate = Objects.requireNonNullElse(fluid.getFluidHolder().getData(DataMapsInit.CHEMICAL_BURNABLE), 0);

		if (this.isLit()) {
			receiveEnergy(rate, false);
			if (fluid.getAmount() > 0) {
				fluid.setAmount(fluid.getAmount() - 1);
				setFluid(0, fluid);
			} else {
				setFluid(0, FluidStack.EMPTY);
			}
		}

		BlockHelper.sendEnergy(level, worldPosition, getEnergy(), CommonConfig.chemicalGeneratorTransferRate.get(),
				this);

		sync();

		super.tick();
	}

	@Override
	public int getMaxEnergy() {
		return CommonConfig.chemicalGeneratorCapacity.get();
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
