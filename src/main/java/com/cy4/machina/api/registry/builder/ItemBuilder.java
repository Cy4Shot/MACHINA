/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.registry.builder;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;

import net.minecraftforge.common.util.NonNullFunction;

public class ItemBuilder<T extends Item> implements IBuilder<Item, T> {

	private final NonNullFunction<Item.Properties, T> factory;
	private ItemGroup tab = ItemGroup.TAB_MISC;
	
	protected ItemBuilder(NonNullFunction<Item.Properties, T> factory) {
		this.factory = factory;
	}
	
	public static <T extends Item> ItemBuilder<T> create(NonNullFunction<Item.Properties, T> factory) {
		return new ItemBuilder<>(factory);
	}
	
	@Override
	public T build() {
		return factory.apply(getProperties());
	}
	
	public ItemBuilder<T> tab(ItemGroup tab) {
		this.tab = tab;
		return this;
	}
	
	public Properties getProperties() {
		return new Properties().tab(tab);
	}

}
