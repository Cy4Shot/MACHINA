package com.machina.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WindCompassItem extends Item {

	public WindCompassItem(Properties props) {
		super(props.stacksTo(1));
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player,
			@NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.isClientSide()) {
			openClientScreen();
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	private void openClientScreen() {
		try {
			Class<?> screenClass = Class.forName("com.machina.client.screen.WindCompassScreen");
			screenClass.getMethod("open").invoke(null);
		} catch (ReflectiveOperationException ignored) {
		}
	}
}
