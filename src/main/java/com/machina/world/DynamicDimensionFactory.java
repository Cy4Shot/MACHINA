package com.machina.world;

import com.machina.Machina;
import com.machina.util.MachinaRL;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;

public class DynamicDimensionFactory {

	public static final RegistryKey<DimensionType> TYPE_KEY = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY,
			new MachinaRL(Machina.MOD_ID));

	public static Dimension createDimension(MinecraftServer server, RegistryKey<Dimension> key) {
		return new Dimension(() -> getDimensionType(server), new DynamicDimensionChunkGenerator(server, key));
	}

	public static DimensionType getDimensionType(MinecraftServer server) {
		return server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrThrow(TYPE_KEY);
	}
}
