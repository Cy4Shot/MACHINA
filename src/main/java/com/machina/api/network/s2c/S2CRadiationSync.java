package com.machina.api.network.s2c;

import java.util.HashSet;
import java.util.Set;

import com.machina.api.network.S2CMessage;
import com.machina.client.ClientRadiationData;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CRadiationSync(Set<ChunkPos> irradiatedChunks) implements S2CMessage<S2CRadiationSync> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CRadiationSync> streamCodec() {
		return new StreamCodec<>() {
			@Override
			public S2CRadiationSync decode(RegistryFriendlyByteBuf buf) {
				int count = buf.readVarInt();
				Set<ChunkPos> chunks = new HashSet<>();
				for (int i = 0; i < count; i++) {
					int x = buf.readInt();
					int z = buf.readInt();
					chunks.add(new ChunkPos(x, z));
				}
				return new S2CRadiationSync(chunks);
			}

			@Override
			public void encode(RegistryFriendlyByteBuf buf, S2CRadiationSync msg) {
				buf.writeVarInt(msg.irradiatedChunks.size());
				for (ChunkPos pos : msg.irradiatedChunks) {
					buf.writeInt(pos.x);
					buf.writeInt(pos.z);
				}
			}
		};
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		ClientRadiationData.setIrradiatedChunks(irradiatedChunks);
	}
}
