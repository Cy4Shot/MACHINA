package com.machina.item;

import java.util.List;

import com.machina.blueprint.Blueprint;
import com.machina.util.StringUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

public class BlueprintItem extends Item {

	public BlueprintItem(Properties props) {
		super(props);
	}

	public static Blueprint get(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null && nbt.contains("of"))
			return Blueprint.fromNBT(nbt.getCompound("of"));
		return Blueprint.EMPTY;
	}

	public static void set(ItemStack stack, Blueprint type) {
		stack.getOrCreateTag().put("of", type.toNBT());
	}

	@Override
	public void appendHoverText(ItemStack pStack, World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
		Blueprint bp = get(pStack);
		if (!bp.getId().equals(Blueprint.EMPTY.getId()))
			pTooltip.add(StringUtils.toComp(bp.getItem().getHoverName().getString())
					.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_00fefe))));

		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		set(stack, Blueprint.EMPTY);
		return stack;
	}

	public static boolean isEtched(ItemStack stack) {
		if (!(stack.getItem() instanceof BlueprintItem))
			return false;

		return get(stack) != Blueprint.EMPTY;
	}
}