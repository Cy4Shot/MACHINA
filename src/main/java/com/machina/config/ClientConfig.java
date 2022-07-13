package com.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		CLIENT_SPEC = configBuilder.build();
	}

	// Terminal Options
	public static ForgeConfigSpec.IntValue minPuzzleSize;
	public static ForgeConfigSpec.IntValue maxPuzzleSize;
	public static ForgeConfigSpec.IntValue puzzleLoadDuration;
	public static ForgeConfigSpec.IntValue starchartLoadDuration;
	public static ForgeConfigSpec.IntValue refuelDuration;

	private static void setupConfig(ForgeConfigSpec.Builder builder) {

		// Terminal Options
		builder.comment(" This category holds options for Terminals.");
		builder.push("Terminal Options");
		{
			// Puzzle Block Options
			builder.comment(" Settings for the crashed ship interface.");
			builder.push("Puzzle Block Options");
			minPuzzleSize = builder.defineInRange("min_puzzle_size", 5, 3, 10);
			maxPuzzleSize = builder.defineInRange("max_puzzle_size", 10, 6, 20);
			puzzleLoadDuration = builder.defineInRange("puzzle_load_duration", 40, 10, 100);
			builder.pop();

			// Ship Console Options
			builder.comment(" Settings for the ship launch interface.");
			builder.push("Ship Console Options");
			starchartLoadDuration = builder.defineInRange("starchart_load_duration", 40, 10, 100);
			refuelDuration = builder.defineInRange("refuel_duration", 40, 10, 100);
			builder.pop();
		}
		builder.pop();
	}

}
