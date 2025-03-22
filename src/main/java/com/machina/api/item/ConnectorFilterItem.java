package com.machina.api.item;

import com.machina.Machina;
import com.machina.api.cap.IConnectorStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ConnectorFilterItem<U, T extends IConnectorStorage<U>> extends Item {

	protected static final String MODE = "mode";

	public ConnectorFilterItem(Properties props) {
		super(props);
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

	public static ItemStack setMode(ItemStack stack, Mode mode) {
		CompoundTag tag = stack.getOrCreateTag();
		if (mode != null)
			tag.putString(MODE, mode.name());
		stack.setTag(tag);
		return stack;
	}

	public abstract boolean filter(ItemStack stack, U original);

	public enum Mode {
		WHITELIST,
		BLACKLIST;

		public Mode opposite() {
			return this == WHITELIST ? BLACKLIST : WHITELIST;
		}

		public MutableComponent comp() {
			return Component.translatable(Machina.MOD_ID + ".filter." + name().toLowerCase());
		}
	}

}