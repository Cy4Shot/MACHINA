package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.block.entity.machine.RocketRefuelingStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CRocketRefuelingStationSync(BlockPos pos, int trackedRocketId, int selectedTank)
		implements S2CMessage<S2CRocketRefuelingStationSync> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CRocketRefuelingStationSync> streamCodec() {
		return StreamCodec.composite(BlockPos.STREAM_CODEC, S2CRocketRefuelingStationSync::pos, ByteBufCodecs.INT,
				S2CRocketRefuelingStationSync::trackedRocketId, ByteBufCodecs.INT,
				S2CRocketRefuelingStationSync::selectedTank, S2CRocketRefuelingStationSync::new);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		if (player.level() == null) {
			return;
		}

		BlockEntity blockEntity = player.level().getBlockEntity(pos);
		if (blockEntity instanceof RocketRefuelingStationBlockEntity station) {
			station.clientSyncState(trackedRocketId, selectedTank);
		}
	}
}
