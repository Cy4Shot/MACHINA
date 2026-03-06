package com.machina.api.starchart;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.ClientStarchart;
import com.machina.api.starchart.obj.SolarSystem;

import net.minecraft.world.level.Level;

public class Starchart {
	private static Starchart INSTANCE = null;

	final SolarSystem system;

	public Starchart(long seed) {
		system = StarchartGenerator.gen(seed);
	}

	public static SolarSystem system(@NotNull Level l) {
		if (l.isClientSide()) {
			return ClientStarchart.system;
		} else {
			return system(l.getServer().overworld().getSeed());
		}
	}

	public static SolarSystem system(long seed) {
		return get(seed).system;
	}

	private static Starchart get(long seed) {
		if (INSTANCE == null) {
			INSTANCE = generate(seed);
		}
		return INSTANCE;
	}

	private static Starchart generate(long seed) {
		return new Starchart(seed);
	}
}
