package com.machina.api.util;

import java.util.Random;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class SoundHelper {
	public static void playSoundFromServer(ServerPlayer p, SoundEvent s) {
		if (s == null || p == null)
			return;
		p.connection.send(new ClientboundSoundPacket(Holder.direct(s), SoundSource.BLOCKS, p.xOld, p.yOld, p.zOld, 1.0f,
				1.0f, new Random().nextLong()));
	}
}
