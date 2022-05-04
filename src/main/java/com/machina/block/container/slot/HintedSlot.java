package com.machina.block.container.slot;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class HintedSlot extends Slot {

	public HintedSlot(IInventory pContainer, int pIndex, int pX, int pY, ItemStack noItemIcon) {
		super(pContainer, pIndex, pX, pY);
		this.setBackground(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation("minecraft:item/" + noItemIcon.getItem().getRegistryName().getPath()));
	}

}
