package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.util.block.BlockHelper;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.registration.init.RegistryInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SPartBenchCraft(RocketPart<?> part, BlockPos pos) implements C2SMessage<C2SPartBenchCraft> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SPartBenchCraft> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.registry(RegistryInit.ROCKET_PART_REGISTRY.key()),
				C2SPartBenchCraft::part, BlockPos.STREAM_CODEC, C2SPartBenchCraft::pos, C2SPartBenchCraft::new);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> BlockHelper.doWithTe(player.level(), pos, RocketPartBenchBlockEntity.class,
				te -> te.startCrafting(player, part())));
	}
}