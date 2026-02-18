package com.machina.block.entity.machine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicates;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class AtmosphericSeparatorBlockEntity extends MachinaBlockEntity {

	@SuppressWarnings("serial")
	private static final Map<ResourceKey<Level>, List<Pair<FluidObject, Integer>>> RECIPES = new HashMap<>() {
		{
			//@formatter:off
			put(Level.OVERWORLD, List.of(
					Pair.of(FluidInit.NITROGEN, 80),
					Pair.of(FluidInit.OXYGEN, 20),
					Pair.of(FluidInit.ARGON, 4),
					Pair.of(FluidInit.CARBON_DIOXIDE, 1)
			));
			//@formatter:on
		}
	};

	public AtmosphericSeparatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public AtmosphericSeparatorBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ATMOSPHERIC_SEPARATOR.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide()) {
			return;
		}

		ResourceKey<Level> dim = this.level.dimension();
		List<Pair<FluidObject, Integer>> atm = RECIPES.getOrDefault(dim, List.of());

		int totalConsumption = 0;
		for (int i = 0; i < atm.size(); i++) {
			Pair<FluidObject, Integer> p = atm.get(i);
			totalConsumption += this.fill(i, new FluidStack(p.getFirst().fluid(), p.getSecond()), FluidAction.SIMULATE);
		}
		int energy = totalConsumption * 20;
		if (this.consumeEnergySim(energy) == energy) {
			this.consumeEnergy(totalConsumption * 20);
			for (int i = 0; i < atm.size(); i++) {
				Pair<FluidObject, Integer> p = atm.get(i);
				this.fill(i, new FluidStack(p.getFirst().fluid(), p.getSecond()), FluidAction.EXECUTE);
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
