package com.machina.api.util;

import com.machina.api.client.ClientStarchart;
import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.Planet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

public class PlanetHelper {
	public static int getIdDim(ResourceKey<LevelStem> dim) {
		return Integer.parseInt(dim.location().getPath());
	}

	public static int getIdLevel(ResourceKey<Level> dim) {
		return Integer.parseInt(dim.location().getPath());
	}

	public static Integer getIdLevelOr(ResourceKey<Level> dim, Integer or) {
		String path = dim.location().getPath();
		try {
			return Integer.parseInt(path);
		} catch (NumberFormatException e) {
			return or;
		}
	}

	public static boolean isPlanetDim(ResourceKey<LevelStem> dim) {
		try {
			getIdDim(dim);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isPlanetLevel(ResourceKey<Level> dim) {
		try {
			getIdLevel(dim);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static Planet getPlanetFor(ServerLevel level) {
		ResourceKey<Level> dim = level.dimension();
		if (isPlanetLevel(dim)) {
			return Starchart.system(level).planets().get(getIdLevel(dim));
		}
		return null;
	}

	public static Planet getPlanetFor(Level level) {
		ResourceKey<Level> dim = level.dimension();
		if (isPlanetLevel(dim)) {
			int id = getIdLevel(dim);
			if (level.isClientSide()) {
				return ClientStarchart.system.planets().get(id);
			} else {
				return Starchart.system(level).planets().get(id);
			}
		}
		return null;
	}
}
