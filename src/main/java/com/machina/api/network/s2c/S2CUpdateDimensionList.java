package com.machina.api.network.s2c;

import java.util.Set;

import com.machina.api.network.S2CMessage;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CUpdateDimensionList(ResourceKey<Level> key) implements S2CMessage<S2CUpdateDimensionList> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CUpdateDimensionList> streamCodec() {
		return ResourceKey.streamCodec(Registries.DIMENSION)
				.map(S2CUpdateDimensionList::new, S2CUpdateDimensionList::key).cast();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		LocalPlayer p = (LocalPlayer) player;
		if (p != null) {
			final Set<ResourceKey<Level>> dl = p.connection.levels();
			dl.add(key);
		}
	}
}