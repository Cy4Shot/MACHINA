package com.machina.api.starchart.planet_trait;

import com.google.common.base.Supplier;
import com.machina.registration.init.FamiliesInit.OreFamily;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PlanetOreTrait extends PlanetTrait {

	private final OreFamily ore;

	public PlanetOreTrait(String name, int color, OreFamily ore) {
		super(name, color);
		this.ore = ore;
	}

	public BlockState getRawOreBlock(Supplier<BlockState> filler) {
		return ore.rawBlock().map(Block::defaultBlockState).orElse(filler.get());
	}

	public BlockState getOreBlock(Supplier<BlockState> filler) {
		return ore.ore().map(oreMap -> {
			return oreMap.map().get(filler.get().getBlockHolder().getKey()).get().defaultBlockState();
		}).orElse(filler.get());
	}
}
