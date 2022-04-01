package com.machina.util.server;

import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ServerHelper {

	public static Optional<ServerWorld> getLevelFromServer(MinecraftServer server, ResourceLocation dimensionID) {
		return Lists.newArrayList(server.getAllLevels()).stream()
				.filter(level -> level.dimension().location().equals(dimensionID)).findFirst();
	}
	
	public static long getSeed() {
		return ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getSeed();
	}
}
