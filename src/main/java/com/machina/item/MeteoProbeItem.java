package com.machina.item;

import org.jetbrains.annotations.NotNull;

import com.machina.client.screen.MeteoProbeScreen;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MeteoProbeItem extends Item {

	public MeteoProbeItem(Properties props) {
		super(props.stacksTo(1));
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player,
			@NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.isClientSide()) {
			MeteoProbeScreen.open();
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
