package com.machina.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.blueprint.Blueprint;
import com.machina.blueprint.BlueprintGroup;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.vector.Vector2f;

public class Research {
	
	public static final Research EMPTY = new Research("empty", null, () -> null, BlueprintGroup.EMPTY, Items.BARRIER);

	private final String id;
	private final Research parent;
	private Vector2f location = new Vector2f(0, 0);
	private final List<Research> children = new ArrayList<>();
	private final Supplier<Ingredient> needs;
	private final BlueprintGroup unlock;
	private final ItemStack icon;

	public Research(String id, Research parent, Supplier<Ingredient> in, BlueprintGroup bg, IItemProvider icon) {
		this.id = id;
		this.parent = parent;
		this.needs = in;
		this.unlock = bg == null ? BlueprintGroup.EMPTY : bg;
		if (parent != null) {
			parent.registerChild(this);
		}
		this.icon = new ItemStack(icon);
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
		return needs.get();
	}

	public List<Blueprint> getBlueprints() {
		if (unlock.equals(BlueprintGroup.EMPTY)) {
			return Collections.emptyList();
		}
		return unlock.getBlueprints();
	}

	public ItemStack getIcon() {
		if (unlock.equals(BlueprintGroup.EMPTY)) {
			return this.icon;
		}
		return unlock.getIcon();
	}
	
	public boolean has(ItemStack stack) {
		if (unlock.equals(BlueprintGroup.EMPTY)) {
			return false;
		}
		return unlock.has(stack);
	}

	public boolean isComplete(PlayerInventory inv) {
		for (ItemStack s : needs.get().getItems()) {
			if (inv.countItem(s.getItem()) >= s.getCount())
				return true;
		}
		return false;
	}
}
