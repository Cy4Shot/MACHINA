package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record S2COpenDirectionalContainer(int id, int windowId, Component name, FriendlyByteBuf additional)
		implements S2CMessage {
	public S2COpenDirectionalContainer(MenuType<?> type, int openContainerId, FriendlyByteBuf output) {
		this(BuiltInRegistries.MENU.getId(type), openContainerId, Component.empty(), output);
	}

	public static S2COpenDirectionalContainer decode(FriendlyByteBuf buf) {
		return new S2COpenDirectionalContainer(buf.readVarInt(), buf.readVarInt(), buf.readComponent(),
				new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray(32600))));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(id);
		buf.writeVarInt(windowId);
		buf.writeComponent(name);
		buf.writeByteArray(additional.readByteArray());
	}

	public final MenuType<?> getType() {
		return BuiltInRegistries.MENU.byId(this.id);
	}

	@Override
	public void handle() {
		mc.execute(() -> {
			try {
				MenuType<?> type = getType();
				MenuScreens.getScreenFactory(type, Minecraft.getInstance(), windowId, name).ifPresent(f -> {
					AbstractContainerMenu c = type.create(windowId, mc.player.getInventory(), additional);

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