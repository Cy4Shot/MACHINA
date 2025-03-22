package com.machina.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.api.cap.item.ConduitItemStorage;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.item.menu.ItemFilterMenu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemFilterItem extends ConnectorFilterItem<ItemStack, ConduitItemStorage> {

	private static final String ITEM = "item";
	private static final String MODE = "mode";

	public ItemFilterItem(Properties props) {
		super(props);
	}

	public static Item getItem(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (nbt.contains(ITEM)) {
			ResourceLocation fluidName = new ResourceLocation(nbt.getString(ITEM));
			Item f = ForgeRegistries.ITEMS.getValue(fluidName);
			if (f != null)
				return f;
		}
		return Items.AIR;
	}

	public static Mode getMode(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (nbt.contains(MODE)) {
			try {
				return Mode.valueOf(nbt.getString(MODE));
			} catch (IllegalArgumentException e) {
				// Do nothing here :)
			}
		}
		return Mode.BLACKLIST;
	}

	public static ItemStack set(ItemStack stack, Item type, Mode mode) {
		CompoundTag tag = stack.getOrCreateTag();
		if (type != null)
			tag.putString(ITEM, ForgeRegistries.ITEMS.getKey(type).toString());
		if (mode != null)
			tag.putString(MODE, mode.name());
		stack.setTag(tag);
		return stack;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip,
			@NotNull TooltipFlag flag) {
		Item item = getItem(stack);
		if (item != Items.AIR) {
			tooltip.add(Component.translatable(Machina.MOD_ID + ".tooltip.item_filter.configured")
					.setStyle(Style.EMPTY.withColor(65278)));
		} else {
			tooltip.add(Component.translatable(Machina.MOD_ID + ".tooltip.item_filter.empty")
					.setStyle(Style.EMPTY.withColor(65278)));
		}

		super.appendHoverText(stack, level, tooltip, flag);
	}

	@Override
	public boolean filter(ItemStack stack, ItemStack original) {
		Mode mode = getMode(stack);
		Item item = getItem(stack);

		switch (mode) {
		case BLACKLIST:
			return !original.getItem().equals(item);
		default:
			return original.getItem().equals(item);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide())
			return super.use(level, player, hand);

		NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
			@Override
			public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
				return new ItemFilterMenu(id, inv, hand);
			}

			@Override
			public Component getDisplayName() {
				return Component.empty();
			}
		}, buf -> {
			buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
		});
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}
}