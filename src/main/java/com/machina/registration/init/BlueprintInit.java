package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;

import com.machina.blueprint.Blueprint;
import com.machina.blueprint.Blueprint.BlueprintCategory;
import com.machina.blueprint.BlueprintGroup;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

public class BlueprintInit {

	public static Map<String, Blueprint> BLUEPRINTS = new HashMap<>();
	public static Map<String, BlueprintGroup> BLUEPRINT_GROUPS = new HashMap<>();

	//@formatter:off
	public static final Blueprint COMPONENT_ANALYZER = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint SHIP_CONSOLE = create(BlueprintCategory.MACH, BlockInit.SHIP_CONSOLE.get());
	public static final Blueprint FUEL_STORAGE_UNIT = create(BlueprintCategory.MACH, BlockInit.FUEL_STORAGE_UNIT.get());
	public static final Blueprint TEMPERATURE_REGULATOR = create(BlueprintCategory.MACH, BlockInit.TEMPERATURE_REGULATOR.get());
	public static final Blueprint BATTERY = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint FURNACE_GENERATOR = create(BlueprintCategory.MACH, BlockInit.FURNACE_GENERATOR.get());
	public static final Blueprint TANK = create(BlueprintCategory.MACH, BlockInit.TANK.get());
	public static final Blueprint ATMOSPHERIC_SEPARATOR = create(BlueprintCategory.MACH, BlockInit.ATMOSPHERIC_SEPARATOR.get());
	public static final Blueprint MELTER = create(BlueprintCategory.MACH, BlockInit.MELTER.get());
	public static final Blueprint MIXER = create(BlueprintCategory.MACH, BlockInit.MIXER.get());
	public static final Blueprint HABER_CONTROLLER = create(BlueprintCategory.MACH, BlockInit.HABER_CONTROLLER.get());
	public static final Blueprint HABER_PORT = create(BlueprintCategory.MACH, BlockInit.HABER_PORT.get());
	public static final Blueprint HABER_CASING = create(BlueprintCategory.MACH, BlockInit.HABER_CASING.get());
	public static final Blueprint PUMP_CASING = create(BlueprintCategory.MACH, BlockInit.PUMP_CASING.get());
	public static final Blueprint PUMP_PORT = create(BlueprintCategory.MACH, BlockInit.PUMP_PORT.get());
	public static final Blueprint PUMP_CONTROLLER  = create(BlueprintCategory.MACH, BlockInit.PUMP_CONTROLLER.get());
	public static final Blueprint PUMP_TANK = create(BlueprintCategory.MACH, BlockInit.PUMP_TANK.get());
	public static final Blueprint PUMP_HEAD = create(BlueprintCategory.MACH, BlockInit.PUMP_HEAD.get());

	public static final BlueprintGroup ROCKETRY = create("rocketry", ItemInit.SHIP_COMPONENT.get(), COMPONENT_ANALYZER);
	public static final BlueprintGroup IDENTIFICATION = create("identification", BlockInit.COMPONENT_ANALYZER.get(), SHIP_CONSOLE);
	public static final BlueprintGroup FUEL_1 = create("fuel_1", BlockInit.FUEL_STORAGE_UNIT.get(), FUEL_STORAGE_UNIT);
	public static final BlueprintGroup COOLANT_1 = create("coolant_1", BlockInit.TEMPERATURE_REGULATOR.get(), TEMPERATURE_REGULATOR);
	public static final BlueprintGroup ELECTRICITY = create("electricity", BlockInit.BATTERY.get(), BATTERY, FURNACE_GENERATOR);
	public static final BlueprintGroup CHEMISTRY = create("chemistry", BlockInit.TANK.get(), TANK, ATMOSPHERIC_SEPARATOR);
	public static final BlueprintGroup MELTING = create("melting", BlockInit.MELTER.get(), MELTER);
	public static final BlueprintGroup REACTING = create("reacting", BlockInit.MIXER.get(), MIXER);
	public static final BlueprintGroup HABER = create("haber", BlockInit.HABER_CONTROLLER.get(), HABER_CONTROLLER, HABER_PORT, HABER_CASING);
	public static final BlueprintGroup PUMP = create("pump", BlockInit.PUMP_CONTROLLER.get(), PUMP_CASING, PUMP_PORT, PUMP_CONTROLLER, PUMP_TANK, PUMP_HEAD);
	//@formatter:on

	private static Blueprint create(BlueprintCategory cat, IItemProvider unlock) {
		String id = unlock.asItem().getRegistryName().getPath();
		Blueprint r = new Blueprint(id, unlock, cat);
		BLUEPRINTS.put(id, r);
		return r;
	}

	private static BlueprintGroup create(String id, IItemProvider icon, Blueprint... bps) {
		BlueprintGroup r = new BlueprintGroup(new ItemStack(icon), bps);
		BLUEPRINT_GROUPS.put(id, r);
		return r;
	}
}
