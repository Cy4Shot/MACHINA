package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.api.util.block.BlockHelper;
import com.machina.block.entity.machine.RocketRefuelingStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SRocketRefuelingStationSetTank(BlockPos pos, int tank)
		implements C2SMessage<C2SRocketRefuelingStationSetTank> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SRocketRefuelingStationSetTank> streamCodec() {
		return StreamCodec.composite(BlockPos.STREAM_CODEC, C2SRocketRefuelingStationSetTank::pos, ByteBufCodecs.INT,
				C2SRocketRefuelingStationSetTank::tank, C2SRocketRefuelingStationSetTank::new);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> BlockHelper.doWithTe(player.level(), pos, RocketRefuelingStationBlockEntity.class,
				be -> be.setSelectedTank(tank)));
	}
}
