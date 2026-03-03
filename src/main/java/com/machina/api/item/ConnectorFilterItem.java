package com.machina.api.item;

import com.machina.Machina;
import com.machina.api.cap.IConnectorStorage;
import com.machina.api.util.reflect.MachinaCodecs;
import com.machina.api.util.reflect.MachinaStreamCodecs;
import com.machina.api.util.reflect.MachinaStreamCodecs.HasId;
import com.machina.registration.init.DataComponentsInit;
import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ConnectorFilterItem<U, T extends IConnectorStorage<U>> extends Item {

	public ConnectorFilterItem(Properties props) {
		super(props.component(DataComponentsInit.FILTER_MODE, Mode.WHITELIST));
	}

	public abstract boolean filter(ItemStack stack, U original);

	public static Mode getMode(ItemStack stack) {
        Mode mode = stack.get(DataComponentsInit.FILTER_MODE);
		return mode == null ? Mode.WHITELIST : mode;
	}

	public enum Mode implements HasId {
		WHITELIST, BLACKLIST;

		public static final Codec<Mode> CODEC = MachinaCodecs.enumCodec(Mode.class);
		public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = MachinaStreamCodecs.enumCodec(Mode.class);

		public Mode opposite() {
			return this == WHITELIST ? BLACKLIST : WHITELIST;
		}

		public MutableComponent comp() {
			return Component.translatable(Machina.MOD_ID + ".filter." + name().toLowerCase());
		}

		@Override
		public int getId() {
			return this == WHITELIST ? 1 : 0;
		}
	}

}