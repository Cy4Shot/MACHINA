package com.machina.api.item;

import com.machina.api.cap.IConnectorStorage;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ConnectorFilterItem<U, T extends IConnectorStorage<U>> extends Item {

	public ConnectorFilterItem(Properties props) {
		super(props);
	}

	public abstract boolean filter(ItemStack stack, U original);

	public enum Mode {
		WHITELIST, BLACKLIST;
	}

}