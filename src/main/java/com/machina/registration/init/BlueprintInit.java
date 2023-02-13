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

	public static final BlueprintGroup ANALYSIS = create("analysis", BlockInit.COMPONENT_ANALYZER.get(), COMPONENT_ANALYZER);
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
