package com.machina.client.tts;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import com.machina.Machina;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TTSPlayer {

	private static final Minecraft mc = Minecraft.getInstance();
	private static Clip clip;
	public static LocalMaryInterface mary = null;

	public static void play(String inputText) {

		new Thread(() -> {
			if (mary == null) {
				if (!setup())
					return;
			}

			for (String voice : mary.getAvailableVoices()) {
				mary.setVoice(voice);
				try {
					AudioInputStream audio = mary.generateAudio(inputText);
					if (audio != null) {

						clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audio.getFormat()));
						clip.open(audio);

						float volume = mc.options.getSoundSourceVolume(SoundCategory.VOICE);
						FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(20f * (float) Math.log10(volume));

						clip.start();
					}
				} catch (SynthesisException | IOException | LineUnavailableException e) {
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}

	public static boolean setup() {
		try {
			mary = new LocalMaryInterface();
			Machina.LOGGER.info("TTS successfully constructed.");
			return true;
		} catch (MaryConfigurationException e) {
			Machina.LOGGER.error("Error when constructing TTS engine. Oopsie! ?*!@");
			e.printStackTrace();
			return false;
		}
	}
}
