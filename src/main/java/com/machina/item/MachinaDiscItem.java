package com.machina.item;

import java.util.function.Supplier;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;

public class MachinaDiscItem extends MusicDiscItem {

	public MachinaDiscItem(Supplier<SoundEvent> soundSupplier) {
		super(1, soundSupplier, new Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE));
	}

}
