package com.machina.registration.init;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.machina.research.Research;
import com.machina.research.ResearchTreeNode;
import com.machina.util.text.StringUtils;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

public class ResearchInit {

	public static Map<String, Research> RESEARCHES = new HashMap<>();

	public static Research ROOT = create("root", null);
	public static Research ANALYSIS = create("analysis", ROOT, ItemInit.SHIP_COMPONENT.get());
	public static Research ANALYSIS_2 = create("analysis2", ROOT, ItemInit.AMMONIUM_NITRATE.get());

	static {
		ResearchTreeNode.run(ROOT);
	}

	private static Research create(String id, Research parent, IItemProvider... i) {
		List<Ingredient> req = Arrays.asList(i).stream().map(o -> Ingredient.of(o)).collect(Collectors.toList());
		Research r = new Research(id, parent, req);
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