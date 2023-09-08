package com.machina.api.starchart;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CSyncStarchart;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class Starchart {
	private static Starchart INSTANCE = null;

	public static Starchart get(@NotNull Level l) {
		if (INSTANCE == null) {
			if (!l.isClientSide()) {
				INSTANCE = generate(getSeed(((ServerLevel) l).getSeed()));
			}
		}
		return INSTANCE;
	}
	
	public static void syncClient(ServerPlayer player) {
		long seed = getSeed(player.serverLevel().getSeed());
		PacketSender.sendToClient(player, new S2CSyncStarchart(seed));
	}

	public static long getSeed(long levelseed) {
		return new Random(levelseed).nextLong();
	}

	private static Starchart generate(long seed) {
		return new Starchart();
	}
}
