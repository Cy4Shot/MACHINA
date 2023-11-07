package com.machina.api.network.s2c;

import java.util.HashSet;
import java.util.Set;

import com.machina.api.network.S2CMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record S2CUpdateDimensionList(Set<ResourceKey<Level>> newDim, Set<ResourceKey<Level>> oldDim)
		implements S2CMessage {

	public static S2CUpdateDimensionList decode(FriendlyByteBuf buf) {
		Set<ResourceKey<Level>> n = new HashSet<>();
		Set<ResourceKey<Level>> o = new HashSet<>();

		final int newDimensionCount = buf.readVarInt();
		for (int i = 0; i < newDimensionCount; i++) {
			final ResourceLocation worldID = buf.readResourceLocation();
			n.add(ResourceKey.create(Registries.DIMENSION, worldID));
		}

		final int removedDimensionCount = buf.readVarInt();
		for (int i = 0; i < removedDimensionCount; i++) {
			final ResourceLocation worldID = buf.readResourceLocation();
			o.add(ResourceKey.create(Registries.DIMENSION, worldID));
		}

		return new S2CUpdateDimensionList(n, o);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.newDim.size());
		for (final ResourceKey<Level> key : this.newDim) {
			buf.writeResourceLocation(key.location());
		}

		buf.writeVarInt(this.oldDim.size());
		for (final ResourceKey<Level> key : this.oldDim) {
			buf.writeResourceLocation(key.location());
		}
	}

	@Override
	public void handle() {
		Set<ResourceKey<Level>> newDimensions = newDim();
		Set<ResourceKey<Level>> oldDimensions = oldDim();

		Minecraft mc = Minecraft.getInstance();
		mc.execute(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				LocalPlayer player = Minecraft.getInstance().player;
				if (player != null) {
					final Set<ResourceKey<Level>> commandSuggesterLevels = player.connection.levels();
					commandSuggesterLevels.addAll(newDimensions);
					for (final ResourceKey<Level> key : oldDimensions) {
						commandSuggesterLevels.remove(key);
					}
				}
			}
		});
	}
}