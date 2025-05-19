package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.util.block.BlockHelper;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.registration.init.RegistryInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SPartBenchCraft(RocketPart<?> part, BlockPos pos) implements C2SMessage {
	public static C2SPartBenchCraft decode(FriendlyByteBuf buf) {
		return new C2SPartBenchCraft(RegistryInit.ROCKET_PARTS_REGISTRY.get().getValue(buf.readResourceLocation()),
				buf.readBlockPos());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeResourceLocation(part.getLoc());
		buf.writeBlockPos(pos);
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> {
			BlockHelper.doWithTe(player.level(), pos, RocketPartBenchBlockEntity.class, te -> {
				te.startCrafting(player, part());
			});
		});
	}
}