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
	public static final Blueprint c1 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c2 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c3 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c4 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c5 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c6 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c7 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c8 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c9 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c10 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c11 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c12 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c13 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint c14 = create(BlueprintCategory.MACH, BlockInit.COMPONENT_ANALYZER.get());
	public static final Blueprint d1 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d2 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d3 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d4 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d5 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d6 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d7 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d8 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());
	public static final Blueprint d9 = create(BlueprintCategory.MACH, BlockInit.BATTERY.get());

	public static final BlueprintGroup ANALYSIS = create("analysis", BlockInit.COMPONENT_ANALYZER.get(), COMPONENT_ANALYZER, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14);
	public static final BlueprintGroup B = create("b", BlockInit.BATTERY.get(), d1, d2, d3, d4, d5 ,d6, d7, d8, d9);

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
