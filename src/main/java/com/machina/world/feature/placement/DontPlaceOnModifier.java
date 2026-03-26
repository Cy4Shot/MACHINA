package com.machina.world.feature.placement;

import com.google.gson.JsonObject;
import com.machina.api.starchart.planet_biome.PlanetBiomeJsonInfo;
import com.machina.api.starchart.planet_biome.placement.PlacementModifier;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public record DontPlaceOnModifier(BlockState state) implements PlacementModifier {

	public static final Codec<DontPlaceOnModifier> CODEC = BlockState.CODEC.fieldOf("block")
			.xmap(DontPlaceOnModifier::new, DontPlaceOnModifier::state).codec();

	public DontPlaceOnModifier(JsonObject params) {
		this(PlanetBiomeJsonInfo.getBlock(params.get("block").getAsString()));
	}

	@Override
	public boolean apply(BlockPos.MutableBlockPos pos, RandomSource rand, WorldGenLevel level) {
		return !level.getBlockState(pos.below()).is(state.getBlock());
	}
}