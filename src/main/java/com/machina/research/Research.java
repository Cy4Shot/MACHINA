package com.machina.research;

import java.util.ArrayList;
import java.util.List;

import com.machina.Machina;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.vector.Vector2f;

public class Research {
	
	private String id;
	private Research parent;
	private Vector2f location = new Vector2f(0, 0);
	private List<Research> children = new ArrayList<>();
	private List<Ingredient> unlock;
	
	public Research(String id, Research parent, List<Ingredient> req) {
		this.id = id;
		this.parent = parent;
		this.unlock = req;
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

	public List<Ingredient> getUnlock() {
		return unlock;
	}
	
	public boolean isComplete(PlayerInventory inv) {
		for (Ingredient i : unlock) {
			for (ItemStack s : i.getItems()) {
				if (inv.countItem(s.getItem()) != s.getCount())
					return false;
			}
		}
		return true;
	}
}
