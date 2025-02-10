package com.machina.api.network.c2s;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.network.C2SMessage;
import com.machina.api.util.block.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SSideConfig(String id, BlockPos pos, byte[] config) implements C2SMessage {
	public static C2SSideConfig decode(FriendlyByteBuf buf) {
		return new C2SSideConfig(buf.readUtf(), buf.readBlockPos(), buf.readByteArray(6));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(id);
		buf.writeBlockPos(pos);
		buf.writeByteArray(config);
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> BlockHelper.doWithTe(player.level(), pos(), MachinaBlockEntity.class, te -> te.updateSideConfig(id(), config())));
	}
}