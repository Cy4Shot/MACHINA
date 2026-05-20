package com.machina.block.entity.machine.fission_reactor;

import com.machina.api.block.entity.MultiblockMasterBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class FissionReactorControllerBlockEntity extends MultiblockMasterBlockEntity {

	public record FissionFuel(int burnTime, int steamPerTick, ItemStack wasteProduct) {
		public static final Codec<FissionFuel> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.INT.fieldOf("burnTime").forGetter(FissionFuel::burnTime),
						Codec.INT.fieldOf("steamPerTick").forGetter(FissionFuel::steamPerTick),
						ItemStack.CODEC.fieldOf("wasteProduct").forGetter(FissionFuel::wasteProduct))
				.apply(instance, FissionFuel::new));
	}

	public FissionReactorControllerBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.FISSION_REACTOR_CONTROLLER.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.FISSION_REACTOR;
	}

	@Override
	public void createStorages() {
		itemStorage(Side.INPUTS);
		itemStorage(Side.OUTPUTS);
		fluidStorage(100_000, stack -> stack.getFluid().equals(Fluids.WATER), Side.INPUTS); // TODO: config
		energyStorage(Side.OUTPUTS);
	}

	@Override
	public int getMaxEnergy() {
		return 1_000_000; // TODO: config
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
