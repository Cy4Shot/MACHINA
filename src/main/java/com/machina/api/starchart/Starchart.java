package com.machina.api.starchart;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CSyncStarchart;
import com.machina.api.starchart.obj.SolarSystem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class Starchart {
	private static Starchart INSTANCE = null;

	SolarSystem system;

	public Starchart(long seed) {
		system = StarchartGenerator.gen(seed, "Example");
	}

	public static SolarSystem system(@NotNull ServerLevel l) {
		return get(l).system;
	}
	
	public static SolarSystem system(long seed) {
		return get(seed).system;
	}

	public static Starchart get(long seed) {
		if (INSTANCE == null) {
			INSTANCE = generate(getSeed(seed));
		}
		return INSTANCE;
	}

	public static Starchart get(@NotNull ServerLevel l) {
		if (INSTANCE == null) {
			if (!l.isClientSide()) {
				INSTANCE = generate(getSeed(l.getSeed()));
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
		return new Starchart(seed);
	}
}
