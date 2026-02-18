package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.api.util.reflect.MachinaStreamCodecs;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.connection.ConnectionType;

public record S2COpenDirectionalContainer(int id, int windowId, Component name, FriendlyByteBuf additional)
		implements S2CMessage<S2COpenDirectionalContainer> {
	public S2COpenDirectionalContainer(MenuType<?> type, int openContainerId, FriendlyByteBuf output) {
		this(BuiltInRegistries.MENU.getId(type), openContainerId, Component.empty(), output);
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2COpenDirectionalContainer> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.INT, S2COpenDirectionalContainer::id, ByteBufCodecs.INT,
				S2COpenDirectionalContainer::windowId, ComponentSerialization.TRUSTED_STREAM_CODEC,
				S2COpenDirectionalContainer::name, MachinaStreamCodecs.FRIENDLY_BYTE_BUF,
				S2COpenDirectionalContainer::additional, S2COpenDirectionalContainer::new);
	}

	public MenuType<?> getType() {
		return BuiltInRegistries.MENU.byId(this.id);
	}

	@Override
	public void handle() {
		mc.execute(() -> {
			try {
				MenuType<?> type = getType();
				MenuScreens.getScreenFactory(type).ifPresent(f -> {
					if (mc.player == null)
						return;

					RegistryAccess access = mc.player.registryAccess();
					AbstractContainerMenu c = type.create(windowId, mc.player.getInventory(),
							new RegistryFriendlyByteBuf(additional, access, ConnectionType.OTHER));

					@SuppressWarnings("unchecked")
					Screen s = ((MenuScreens.ScreenConstructor<AbstractContainerMenu, ?>) f).create(c,
							mc.player.getInventory(), name);
					mc.player.containerMenu = ((MenuAccess<?>) s).getMenu();
					mc.setScreen(s);
				});
			} finally {
				additional.release();
			}
		});
	}
}