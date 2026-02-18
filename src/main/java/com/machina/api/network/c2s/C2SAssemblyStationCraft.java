package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.api.util.block.BlockHelper;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SAssemblyStationCraft(BlockPos pos) implements C2SMessage<C2SAssemblyStationCraft> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SAssemblyStationCraft> streamCodec() {
		return BlockPos.STREAM_CODEC.map(C2SAssemblyStationCraft::new, C2SAssemblyStationCraft::pos).cast();
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> BlockHelper.doWithTe(player.level(), pos, RocketAssemblyStationBlockEntity.class,
				RocketAssemblyStationBlockEntity::startCrafting));
	}
}