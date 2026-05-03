package com.machina.block.entity.machine.geothermal_generator;

import com.machina.api.block.entity.MultiblockMasterBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.block.BlockHelper;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.DataMapsInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class GeothermalGeneratorControllerBlockEntity extends MultiblockMasterBlockEntity {

	public GeothermalGeneratorControllerBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.GEOTHERMAL_GENERATOR_CONTROLLER.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.GEOTHERMAL_GENERATOR;
	}

	@Override
	public void createStorages() {
		energyStorage(Side.OUTPUTS);
	}

	@Override
	public int getMaxEnergy() {
		return CommonConfig.geothermalGeneratorCapacity.get();
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

		if (isLit()) {
			receiveEnergy(energySource(), false);
			sync();
		}

		BlockHelper.sendEnergy(level, worldPosition, getEnergy(), CommonConfig.geothermalGeneratorTransferRate.get(),
				this);

		super.tick();
	}

	@SuppressWarnings("deprecation")
	public Integer energySource() {
		BlockPos sourcePos = this.getBlockPos().below(2);
		return level.getBlockState(sourcePos).getBlock().builtInRegistryHolder()
				.getData(DataMapsInit.GEOTHERMAL_ENERGY_SOURCE);
	}

	@Override
	public boolean isLit() {
		return !this.isEnergyFull() && energySource() != null;
	}
}
