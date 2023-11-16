package com.machina.api.network.s2c;

import java.util.Set;

import com.machina.api.network.S2CMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record S2CUpdateDimensionList(ResourceKey<Level> key) implements S2CMessage {

	public static S2CUpdateDimensionList decode(FriendlyByteBuf buf) {
		return new S2CUpdateDimensionList(ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation()));
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeResourceLocation(key.location());
	}

	@Override
	public void handle() {
		ResourceKey<Level> nd = key();
		Minecraft mc = Minecraft.getInstance();

		mc.execute(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				LocalPlayer player = Minecraft.getInstance().player;
				if (player != null) {
					final Set<ResourceKey<Level>> dl = player.connection.levels();
					if (dl == null)
						return;
					dl.add(nd);
				}
			}
		});
	}
}