package com.machina.world.feature.placement;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machina.api.starchart.planet_biome.PlanetBiomeJsonInfo;
import com.machina.api.starchart.planet_biome.placement.PlacementModifier;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;

public record OnlyPlaceOnModifier(List<Block> state) implements PlacementModifier {

	public static final Codec<OnlyPlaceOnModifier> CODEC = BuiltInRegistries.BLOCK.byNameCodec().listOf()
			.fieldOf("block").xmap(OnlyPlaceOnModifier::new, OnlyPlaceOnModifier::state).codec();

	private static List<Block> parse(JsonArray arr) {
		List<Block> states = new ArrayList<>();
		for (int i = 0; i < arr.size(); i++) {
			states.add(PlanetBiomeJsonInfo.getBlock(arr.get(i).getAsString()).getBlock());
		}
		return states;
	}

	public OnlyPlaceOnModifier(JsonObject params) {
		this(parse(params.get("block").getAsJsonArray()));
	}

	@Override
	public boolean apply(BlockPos.MutableBlockPos pos, RandomSource rand, WorldGenLevel level) {
		return state.contains(level.getBlockState(pos.below()).getBlock());
	}
}