package com.machina.registration;

import java.util.function.Supplier;

import com.machina.Machina;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MachinaItemGroup extends ItemGroup {

	Supplier<Item> icon;

	public MachinaItemGroup(Supplier<Item> icon, String pLangId) {
		super(ItemGroup.TABS.length, Machina.MOD_ID + "." + pLangId);
		this.icon = icon;
	}

	@Override
	public ItemStack makeIcon() {
		return this.icon.get().getDefaultInstance();
	}

}
