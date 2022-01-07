package com.machina.api.starchart;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

import com.machina.api.api_extension.ApiExtensions;
import com.machina.api.api_extension.IApiExtendable;
import com.machina.api.planet.trait.type.IPlanetTraitType;
import com.machina.api.world.data.PlanetData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface Starchart {

	static Starchart getStarchartForServer(@Nonnull @WillNotClose MinecraftServer server) {
		return ApiExtensions.withExtension(StarchartGetter.class, getter -> getter.getStarchart(server));
	}

	default <TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(World level, Class<TYPE> typeClass) {
		return getTraitsForType(level.dimension().location(), typeClass);
	}

	<TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(ResourceLocation dimensionId, Class<TYPE> typeClass);

	@Nullable
	default PlanetData getDataForLevel(World level) {
		return getDataForLevel(level.dimension().location());
	}

	@Nullable
	PlanetData getDataForLevel(ResourceLocation dimensionId);

	@FunctionalInterface
	interface StarchartGetter extends IApiExtendable {

		Starchart getStarchart(MinecraftServer server);
	}

}
