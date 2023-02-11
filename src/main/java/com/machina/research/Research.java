package com.machina.research;

import java.util.ArrayList;
import java.util.List;

import com.machina.Machina;
import com.machina.blueprint.BlueprintGroup;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.vector.Vector2f;

public class Research {

	private final String id;
	private final Research parent;
	private Vector2f location = new Vector2f(0, 0);
	private final List<Research> children = new ArrayList<>();
	private final Ingredient needs;
	private final BlueprintGroup unlock;

	public Research(String id, Research parent, IItemProvider in, BlueprintGroup bg) {
		this.id = id;
		this.parent = parent;
		this.needs = in == null ? null : Ingredient.of(in);
		this.unlock = bg == null ? BlueprintGroup.EMPTY : bg;
		if (parent != null) {
			parent.registerChild(this);
		}
	}

	public String getId() {
		return id;
	}

	public Research getParent() {
		return parent;
	}

	public List<Research> getChildren() {
		return children;
	}

	private void registerChild(Research child) {
		children.add(child);
	}

	public String getNameKey() {
		return "research." + Machina.MOD_ID + "." + id;
	}

	public String getDescKey() {
		return "research." + Machina.MOD_ID + ".desc." + id;
	}

	public Vector2f getLocation() {
		return location;
	}

	public void setLocation(Vector2f location) {
		this.location = location;
	}

	public Ingredient getNeeds() {
		return needs;
	}
	
	public BlueprintGroup getUnlock() {
		return unlock;
	}

	public boolean isComplete(PlayerInventory inv) {
		for (ItemStack s : needs.getItems()) {
			if (inv.countItem(s.getItem()) != s.getCount())
				return false;
		}
		return true;
	}
}
