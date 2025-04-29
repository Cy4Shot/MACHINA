package com.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
	public static final ForgeConfigSpec COMMON_SPEC;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		COMMON_SPEC = configBuilder.build();
	}

	public static ForgeConfigSpec.IntValue basicCapacitorSize;
	public static ForgeConfigSpec.IntValue advancedCapacitorSize;
	public static ForgeConfigSpec.IntValue supremeCapacitorSize;

	public static ForgeConfigSpec.IntValue cableTransferRate;
	public static ForgeConfigSpec.IntValue pipeTransferRate;
	public static ForgeConfigSpec.IntValue conduitTransferRate;
	public static ForgeConfigSpec.IntValue batteryTransferRate;
	public static ForgeConfigSpec.IntValue batteryChargeRate;
	public static ForgeConfigSpec.IntValue batteryDischargeRate;
	public static ForgeConfigSpec.IntValue furnaceGeneratorCapacity;
	public static ForgeConfigSpec.IntValue furnaceGeneratorRate;
	public static ForgeConfigSpec.IntValue furnaceGeneratorTransferRate;
	public static ForgeConfigSpec.IntValue chemicalGeneratorCapacity;
	public static ForgeConfigSpec.IntValue chemicalGeneratorRate;
	public static ForgeConfigSpec.IntValue chemicalGeneratorTransferRate;

	private static void setupConfig(ForgeConfigSpec.Builder builder) {
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
		chemicalGeneratorRate = builder.defineInRange("chemical_generator_rate", 70, 1, 9999);
		chemicalGeneratorTransferRate = builder.defineInRange("chemical_generator_transfer_rate", 1_000, 1, 999_999);
		builder.pop();

		builder.pop();
	}
}