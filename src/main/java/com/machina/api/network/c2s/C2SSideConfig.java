package com.machina.api.network.c2s;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.network.C2SMessage;
import com.machina.api.util.block.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SSideConfig(String id, BlockPos pos, byte[] config) implements C2SMessage<C2SSideConfig> {
	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SSideConfig> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.STRING_UTF8, C2SSideConfig::id, BlockPos.STREAM_CODEC,
				C2SSideConfig::pos, ByteBufCodecs.BYTE_ARRAY, C2SSideConfig::config, C2SSideConfig::new);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> BlockHelper.doWithTe(player.level(), pos(), MachinaBlockEntity.class,
				te -> te.updateSideConfig(id(), config())));
	}
}