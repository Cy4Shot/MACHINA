package com.machina.util.helper;

import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ServerHelper {

	public static Optional<ServerWorld> getLevelFromServer(MinecraftServer server, ResourceLocation dimensionID) {
		return Lists.newArrayList(server.getAllLevels()).stream()
				.filter(level -> level.dimension().location().equals(dimensionID)).findFirst();
	}
	
	public static Optional<ServerWorld> getLevelFromServer(ResourceLocation dimensionID) {
		return getLevelFromServer(server(), dimensionID);
	}
	
	public static long getSeed() {
		return server().getAllLevels().iterator().next().getSeed();
	}
	
	public static MinecraftServer server() {
		return ServerLifecycleHooks.getCurrentServer();
	}
}