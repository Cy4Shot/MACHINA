package com.machina.api.starchart.planet_biome.placement;

import com.machina.registration.init.PlanetPlacementModifierInit;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import net.minecraft.resources.ResourceLocation;

public class PlacementModifierCodec implements Codec<PlacementModifier> {

	@Override
	public <T> DataResult<Pair<PlacementModifier, T>> decode(DynamicOps<T> ops, T input) {
		return ops.getMap(input).flatMap(map -> {
			T typeElement = map.get(ops.createString("type"));
			if (typeElement == null) {
				return DataResult.error(() -> "Missing type");
			}

			DataResult<ResourceLocation> typeResult = ResourceLocation.CODEC.parse(ops, typeElement);

			return typeResult.flatMap(id -> {
				Codec<? extends PlacementModifier> codec = PlanetPlacementModifierInit.CODECS.get(id);
				T paramsElement = map.get(ops.createString("params"));
				if (paramsElement == null) {
					return DataResult.error(() -> "Missing params");
				}
				return codec.decode(ops, paramsElement).map(pair -> pair.mapFirst(m -> (PlacementModifier) m));
			});
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> DataResult<T> encode(PlacementModifier input, DynamicOps<T> ops, T prefix) {
		ResourceLocation id = null;
		Codec<? extends PlacementModifier> foundCodec = null;

		for (var entry : PlanetPlacementModifierInit.CODECS.entrySet()) {
			if (entry.getValue().getClass().equals(input.getClass())) {
				id = entry.getKey();
				foundCodec = entry.getValue();
				break;
			}
		}

		if (id == null || foundCodec == null) {
			return DataResult.error(() -> "Unknown modifier type: " + input.getClass());
		}

		final ResourceLocation loc = id;
		DataResult<T> paramsResult = ((Codec<PlacementModifier>) foundCodec).encode(input, ops, ops.empty());
		return paramsResult.flatMap(params -> {
			var map = ops.mapBuilder();
			map.add("type", ResourceLocation.CODEC.encodeStart(ops, loc).result().orElseThrow());
			map.add("params", params);
			return map.build(prefix);
		});
	}
}