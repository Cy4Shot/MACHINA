package com.machina.item.filter;

import com.machina.Machina;
import com.machina.api.cap.item.ConduitItemStorage;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.item.menu.ItemFilterMenu;
import com.machina.registration.init.DataComponentsInit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemFilterItem extends ConnectorFilterItem<ItemStack, ConduitItemStorage> {

	public ItemFilterItem(Properties props) {
		super(props.component(DataComponentsInit.ITEM, Items.AIR));
	}

	public static Item getItem(ItemStack stack) {
        Item item = stack.get(DataComponentsInit.ITEM);
		return item == null ? Items.AIR : item;
	}

	public static ItemStack set(ItemStack stack, Item type, Mode mode) {
		stack.set(DataComponentsInit.ITEM, type);
		stack.set(DataComponentsInit.FILTER_MODE, mode);
		return stack;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, TooltipContext ctx, @NotNull List<Component> tooltip,
			@NotNull TooltipFlag flag) {
		Item item = getItem(stack);
		if (item != Items.AIR) {
			tooltip.add(Component.translatable(Machina.MOD_ID + ".tooltip.item_filter.configured")
					.setStyle(Style.EMPTY.withColor(65278)));
		} else {
			tooltip.add(Component.translatable(Machina.MOD_ID + ".tooltip.item_filter.empty")
					.setStyle(Style.EMPTY.withColor(65278)));
		}

		super.appendHoverText(stack, ctx, tooltip, flag);
	}

	@Override
	public boolean filter(ItemStack stack, ItemStack original) {
		Mode mode = getMode(stack);
		Item item = getItem(stack);

		if (mode == Mode.BLACKLIST) {
			return !original.getItem().equals(item);
		}
		return original.getItem().equals(item);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player,
			@NotNull InteractionHand hand) {
		if (level.isClientSide())
			return super.use(level, player, hand);

		((ServerPlayer) player).openMenu(new MenuProvider() {
			@Override
			public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
				return new ItemFilterMenu(id, inv, hand);
			}

			@Override
			public @NotNull Component getDisplayName() {
				return Component.empty();
			}
		}, buf -> buf.writeBoolean(hand == InteractionHand.MAIN_HAND));
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}
}