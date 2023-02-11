package com.machina.blueprint;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

public class Blueprint {

	private final String id;

	private final ItemStack bp;
	
	private final BlueprintCategory category;

	public Blueprint(String id, IItemProvider in, BlueprintCategory cat) {
		this.id = id;
		this.bp = new ItemStack(in);
		this.category = cat;
	}

	public boolean has(ItemStack stack) {
		return stack.getItem().equals(bp.getItem());
	}

	public String getId() {
		return id;
	}

	public ItemStack getItem() {
		return bp;
	}
	
	public BlueprintCategory getCategory() {
		return category;
	}

	public enum BlueprintCategory {
		COMP, MACH, TOOL, SUIT
	}
}
