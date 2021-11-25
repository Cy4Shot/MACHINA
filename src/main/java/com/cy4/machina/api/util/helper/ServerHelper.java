package com.cy4.machina.api.util.helper;

import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

public class ServerHelper {

	private ServerHelper() {
	}

	public static Optional<ServerWorld> getLevelFromServer(MinecraftServer server, ResourceLocation dimensionID) {
		return Lists.newArrayList(server.getAllLevels()).stream()
				.filter(level -> level.dimension().location().equals(dimensionID)).findFirst();
	}
}
