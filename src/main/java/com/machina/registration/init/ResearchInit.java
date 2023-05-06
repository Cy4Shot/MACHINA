package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.machina.blueprint.BlueprintGroup;
import com.machina.research.Research;
import com.machina.research.ResearchTreeNode;
import com.machina.util.StringUtils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

public class ResearchInit {

	public static Map<String, Research> RESEARCHES = new HashMap<>();

	//@formatter:off
	public static final Research ROOT = root("root", ItemInit.SCANNER.get());
	
	public static final Research ROCKETRY = create("rocketry", ROOT, ItemInit.SHIP_COMPONENT.get(), BlueprintInit.ROCKETRY);
	public static final Research IDENTIFICATION = create("identification", ROCKETRY, BlockInit.COMPONENT_ANALYZER.get(), BlueprintInit.IDENTIFICATION);
	public static final Research SHIP_CONSTRUCTION = create("ship_construction", IDENTIFICATION, BlockInit.SHIP_CONSOLE.get());
	public static final Research FUEL_1 = create("fuel_1", SHIP_CONSTRUCTION, ItemInit.AMMONIUM_NITRATE.get(), BlueprintInit.FUEL_1);
	public static final Research FUEL_2 = create("fuel_2", FUEL_1, FluidInit.HEXOGEN.bucket());
	public static final Research FUEL_3 = create("fuel_3", FUEL_2, FluidInit.HNIW.bucket());
	public static final Research COOLANT_1 = create("coolant_1", SHIP_CONSTRUCTION, Items.WATER_BUCKET, BlueprintInit.COOLANT_1);
	public static final Research COOLANT_2 = create("coolant_2", COOLANT_1, FluidInit.MOLTEN_LEAD.bucket());
	public static final Research COOLANT_3 = create("coolant_3", COOLANT_2, FluidInit.LEAD_BISMUTH_EUTECTIC.bucket());
	
	public static final Research AUTOMATION = create("automation", ROOT, BlockInit.BLUEPRINTER.get());
	public static final Research FABRICATION = create("fabrication", AUTOMATION, BlockInit.FABRICATOR.get());
	public static final Research ELECTRICITY = create("electricity", FABRICATION, BlockInit.CABLE.get(), BlueprintInit.ELECTRICITY);
	public static final Research CHEMISTRY = create("chemistry", FABRICATION, BlockInit.FLUID_HOPPER.get(), BlueprintInit.CHEMISTRY);
	public static final Research MELTING = create("melting", CHEMISTRY, () -> TagInit.Items.ORE_ITEMS, BlueprintInit.MELTING);
	public static final Research REACTING = create("reacting", CHEMISTRY, () -> TagInit.Items.BUCKETS, BlueprintInit.REACTING);
	public static final Research HABER = create("haber", REACTING, FluidInit.METHANE.bucket(), BlueprintInit.HABER);
	//@formatter:on

	static {
		ResearchTreeNode.run(ROOT);
	}

	private static Research root(String id, IItemProvider icon) {
		Research r = new Research(id, null, () -> null, null, icon);
		RESEARCHES.put(id, r);
		return r;
	}

	private static Research create(String id, Research parent, Supplier<ITag<Item>> in, @Nonnull BlueprintGroup out) {
		return reg(id, parent, () -> Ingredient.of(in.get()), out, null);
	}

	private static Research create(String id, Research parent, IItemProvider in, @Nonnull BlueprintGroup out) {
		return reg(id, parent, () -> Ingredient.of(in), out, null);
	}

	private static Research create(String id, Research parent, IItemProvider in) {
		return reg(id, parent, () -> Ingredient.of(in), null, in);
	}

	private static Research reg(String id, Research parent, Supplier<Ingredient> in, BlueprintGroup out,
			IItemProvider icon) {
		Research r = new Research(id, parent, in, out, icon);
		RESEARCHES.put(id, r);
		return r;
	}

	public static Research getById(String id) {
		return RESEARCHES.get(id);
	}

	// Debug Commands
	public static void debugResearchTree() {
		StringUtils.printlnUtf8(ROOT.getId() + getChildString(ROOT));
	}

	public static String getChildString(Research parent) {
		StringBuilder sb = new StringBuilder("");
		int num = parent.getChildren().size();
		for (int i = 0; i < num; i++) {
			Research child = parent.getChildren().get(i);
			sb.append("\n" + (i == num - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
					+ child.getId());
			sb.append(getChildString(child).replace("\n", "\n" + (i == num - 1 ? " " : StringUtils.TREE_V) + " "));
		}
		return sb.toString();
	}
}