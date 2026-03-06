package com.machina.block.entity.machine;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicates;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.PlanetHelper;
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
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
	}

	public static List<Pair<FluidObject, Integer>> findRecipe(Level level) {
		ResourceKey<Level> dimension = level.dimension();
		if (PlanetHelper.isPlanetLevel(dimension)) {
			Planet planet = PlanetHelper.getPlanetFor(level);
			List<Pair<FluidObject, Integer>> gases = new ArrayList<>();

			int co2 = (int) planet.GCO2();
			if (co2 > 0)
				gases.add(Pair.of(FluidInit.CARBON_DIOXIDE, co2));

			int h2 = (int) planet.GH2();
			if (h2 > 0)
				gases.add(Pair.of(FluidInit.HYDROGEN, h2));

			int h2o = (int) planet.GH2O();
			if (h2o > 0)
				gases.add(Pair.of(FluidObject.WATER, h2o));

			int n2 = (int) planet.GN2();
			if (n2 > 0)
				gases.add(Pair.of(FluidInit.NITROGEN, n2));

			int o2 = (int) planet.GO2();
			if (o2 > 0)
				gases.add(Pair.of(FluidInit.OXYGEN, o2));

			return gases;
		}
		if (dimension.equals(Level.OVERWORLD)) {
			//@formatter:off
			return List.of(
					Pair.of(FluidInit.NITROGEN, 80),
					Pair.of(FluidInit.OXYGEN, 20),
					Pair.of(FluidInit.ARGON, 4),
					Pair.of(FluidInit.CARBON_DIOXIDE, 1));
			//@formatter:on
		}
		return List.of();
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide()) {
			return;
		}

		List<Pair<FluidObject, Integer>> atm = findRecipe(this.level);

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
