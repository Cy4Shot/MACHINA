package com.machina.util.helper;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundHelper {
	public static void playSoundFromServer(ServerPlayerEntity p, SoundEvent s) {
		if (s == null || p == null)
			return;
		p.connection.send(new SPlaySoundEffectPacket(s, SoundCategory.BLOCKS, p.xOld, p.yOld, p.zOld, 1.0f, 1.0f));
	}
}
