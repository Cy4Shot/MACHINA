package com.machina.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
	public static final ModConfigSpec COMMON_SPEC;

	static {
		ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
		setupConfig(configBuilder);
		COMMON_SPEC = configBuilder.build();
	}

	public static ModConfigSpec.IntValue basicCapacitorSize;
	public static ModConfigSpec.IntValue advancedCapacitorSize;
	public static ModConfigSpec.IntValue supremeCapacitorSize;

	public static ModConfigSpec.IntValue cableTransferRate;
	public static ModConfigSpec.IntValue pipeTransferRate;
	public static ModConfigSpec.IntValue conduitTransferRate;
	public static ModConfigSpec.IntValue batteryTransferRate;
	public static ModConfigSpec.IntValue batteryChargeRate;
	public static ModConfigSpec.IntValue batteryDischargeRate;
	public static ModConfigSpec.IntValue furnaceGeneratorCapacity;
	public static ModConfigSpec.IntValue furnaceGeneratorRate;
	public static ModConfigSpec.IntValue furnaceGeneratorTransferRate;
	public static ModConfigSpec.IntValue chemicalGeneratorCapacity;
	public static ModConfigSpec.IntValue chemicalGeneratorTransferRate;
	public static ModConfigSpec.IntValue weatherFadeTicks;

	private static void setupConfig(ModConfigSpec.Builder builder) {
		builder.push("items");

		builder.push("capacitors");
		builder.comment("Capacitor settings");
		basicCapacitorSize = builder.defineInRange("basic_capacitor_size", 100_000, 1, 999_999_999);
		advancedCapacitorSize = builder.defineInRange("advanced_capacitor_size", 1_000_000, 1, 999_999_999);
		supremeCapacitorSize = builder.defineInRange("supreme_capacitor_size", 10_000_000, 1, 999_999_999);
		builder.pop();

		builder.pop();
		builder.push("machines");

		builder.push("cable");
		builder.comment("EnergyCable settings");
		cableTransferRate = builder.defineInRange("cable_transfer_rate", 5_000, 1, 999_999);
		builder.pop();

		builder.push("pipe");
		builder.comment("FluidPipe settings");
		pipeTransferRate = builder.defineInRange("pipe_transfer_rate", 100, 1, 999_999);
		builder.pop();

		builder.push("conduit");
		builder.comment("ItemConduit settings");
		conduitTransferRate = builder.defineInRange("conduit_transfer_rate", 1, 1, 64);
		builder.pop();

		builder.push("battery");
		builder.comment("Battery settings");
		batteryTransferRate = builder.defineInRange("battery_transfer_rate", 1_000, 1, 999_999);
		batteryChargeRate = builder.defineInRange("battery_charge_rate", 10_000, 1, 999_999);
		batteryDischargeRate = builder.defineInRange("battery_discharge_rate", 10_000, 1, 999_999);
		builder.pop();

		builder.push("furnace_generator");
		builder.comment("FurnaceGenerator settings");
		furnaceGeneratorCapacity = builder.defineInRange("furnace_generator_capacity", 10_000, 1, 999_999_999);
		furnaceGeneratorRate = builder.defineInRange("furnace_generator_rate", 60, 1, 9999);
		furnaceGeneratorTransferRate = builder.defineInRange("furnace_generator_transfer_rate", 1_000, 1, 999_999);
		builder.pop();

		builder.push("chemical_generator");
		builder.comment("ChemicalGenerator settings");
		chemicalGeneratorCapacity = builder.defineInRange("chemical_generator_capacity", 10_000, 1, 999_999_999);
		chemicalGeneratorTransferRate = builder.defineInRange("chemical_generator_transfer_rate", 1_000, 1, 999_999);
		builder.pop();

		builder.push("weather");
		builder.comment("Weather settings");
		weatherFadeTicks = builder.defineInRange("weather_fade_ticks", 50, 0, 20_000);
		builder.pop();

		builder.pop();
	}
}
