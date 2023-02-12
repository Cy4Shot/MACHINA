package com.machina.blueprint;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BlueprintGroup {

	public static final BlueprintGroup EMPTY = new BlueprintGroup(new ItemStack(Items.BARRIER));

	private final ItemStack icon;
	private final List<Blueprint> bps;

	public BlueprintGroup(ItemStack icon, Blueprint... bps) {
		this.icon = icon;
		this.bps = Arrays.asList(bps);
	}

	public ItemStack getIcon() {
		return icon;
	}

	public List<Blueprint> getBlueprints() {
		return bps;
	}

	public boolean has(ItemStack itemstack) {
		for (Blueprint bp : bps) {
			if (bp.has(itemstack))
				return true;
		}
		return false;
	}
}
