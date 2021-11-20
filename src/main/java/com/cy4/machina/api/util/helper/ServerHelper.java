package com.cy4.machina.api.util.helper;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

public class ServerHelper {
	
	private ServerHelper() {
	}

	public static Optional<ServerWorld> getLevelFromServer(MinecraftServer server, ResourceLocation dimensionID) {
		AtomicReference<ServerWorld> levelFound = new AtomicReference<>(null);
		server.getAllLevels().forEach(level -> {
			if (level.dimension().location().equals(dimensionID)) {
				levelFound.set(level);
			}
		});
		return Optional.ofNullable(levelFound.get());
	}
}
