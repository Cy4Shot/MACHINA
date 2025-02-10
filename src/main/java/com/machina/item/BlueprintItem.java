package com.machina.item;

import java.util.List;

import com.machina.api.blueprint.Blueprint;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlueprintItem extends Item {

	public BlueprintItem(Properties props) {
		super(props);
	}

	public static Blueprint get(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (nbt.contains("of"))
			return Blueprint.fromNBT(nbt.getCompound("of"));
		return Blueprint.EMPTY;
	}

	public static void set(ItemStack stack, Blueprint type) {
		stack.getOrCreateTag().put("of", type.toNBT());
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip,
			@NotNull TooltipFlag flag) {
		Blueprint bp = get(stack);
		if (!bp.getId().equals(Blueprint.EMPTY.getId()))
			tooltip.add(bp.getName().setStyle(Style.EMPTY.withColor(65278)));

		super.appendHoverText(stack, level, tooltip, flag);
	}

	@Override
	public @NotNull ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		set(stack, Blueprint.EMPTY);
		return stack;
	}

	public static boolean isEtched(ItemStack stack) {
		if (!(stack.getItem() instanceof BlueprintItem))
			return false;

		return get(stack) != Blueprint.EMPTY;
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return isEtched(stack) ? 1 : 16;
	}
}