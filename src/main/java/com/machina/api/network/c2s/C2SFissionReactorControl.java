package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.api.util.block.BlockHelper;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record C2SFissionReactorControl(BlockPos pos, Action action) implements C2SMessage<C2SFissionReactorControl> {

	public enum Action {
		START, SCRAM
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SFissionReactorControl> streamCodec() {
		return StreamCodec.composite(
				BlockPos.STREAM_CODEC, C2SFissionReactorControl::pos,
				ByteBufCodecs.VAR_INT.map(i -> Action.values()[i], Enum::ordinal), C2SFissionReactorControl::action,
				C2SFissionReactorControl::new);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> BlockHelper.doWithTe(player.level(), pos, FissionReactorControllerBlockEntity.class, be -> {
			switch (action) {
			case START -> be.startReactor();
			case SCRAM -> be.scram();
			}
		}));
	}
}
