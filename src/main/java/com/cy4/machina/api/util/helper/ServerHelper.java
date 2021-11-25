/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
