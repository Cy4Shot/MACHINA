package com.machina.client.tts;

import com.mojang.text2speech.Narrator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TTSPlayer {
	public static void play(String inputText) {
		Narrator.getNarrator().say(inputText, false);
	}
}
