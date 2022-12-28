package com.machina.item;

import java.util.List;

import com.machina.util.text.StringUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class BlueprintItem extends Item {

	public BlueprintItem(Properties props) {
		super(props);
	}

	public static String getType(ItemStack stack) {
		return fromNBT(stack.getOrCreateTag(), "of");
	}

	public static String fromNBT(CompoundNBT nbt, String tag) {
		if (nbt != null && nbt.contains(tag))
			return nbt.getString(tag);
		return "empty";
	}

	public static void setType(ItemStack stack, String type) {
		stack.getOrCreateTag().putString("of", type);
	}

	public static TranslationTextComponent getNameForType(String type) {
		return StringUtils.translateComp(Registry.ITEM.get(new ResourceLocation(type)).getDescriptionId());
	}

	@Override
	public void appendHoverText(ItemStack pStack, World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
		String type = getType(pStack);
		if (!type.equals("empty"))
			pTooltip.add(getNameForType(type).setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_00fefe))));

		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		setType(stack, "empty");
		return stack;
	}
}