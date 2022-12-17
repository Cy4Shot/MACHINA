package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;

import com.machina.research.Research;
import com.machina.research.ResearchTreeNode;
import com.machina.util.text.StringUtils;

public class ResearchInit {

	public static Map<String, Research> RESEARCHES = new HashMap<>();

	public static Research ROOT = create("root", null);
	public static Research TEST_A = create("research_branch_a", ROOT);
	public static Research TEST_B = create("research_branch_b", ROOT);
	public static Research TEST_C = create("research_part_1", TEST_A);
	public static Research TEST_D = create("multiple_children_a", TEST_B);
	public static Research TEST_E = create("multiple_children_b", TEST_B);
	public static Research TEST_F = create("research_part_2", TEST_C);

	static {
		ResearchTreeNode.run(ROOT);
	}

	private static Research create(String id, Research parent) {
		Research r = new Research(id, parent);
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