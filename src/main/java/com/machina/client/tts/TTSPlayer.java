package com.machina.client.tts;

import java.io.IOException;
import java.util.Enumeration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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

	private static final String VOICE = "cmu-slt-hsmm";
	private static final Minecraft mc = Minecraft.getInstance();
	private static Clip clip;
	public static LocalMaryInterface mary = null;

	public static void play(String inputText) {

		hideLoggers();

		new Thread(() -> {
			if (mary == null) {
				if (!setup())
					return;
			}
			mary.setVoice(VOICE);
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
		}).start();
	}

	public static boolean setup() {
		try {
			mary = new LocalMaryInterface();
			hideLoggers();
			Machina.LOGGER.info("TTS successfully constructed.");
			return true;
		} catch (MaryConfigurationException e) {
			Machina.LOGGER.error("Error when constructing TTS engine. Oopsie! ?*!@");
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private static void hideLoggers() {
		Enumeration<Logger> a = LogManager.getCurrentLoggers();
		while (a.hasMoreElements()) {
			Logger n = a.nextElement();
			if (n.getName().startsWith("marytts")) {
				n.setLevel(Level.OFF);
			}
		}
	}
}
