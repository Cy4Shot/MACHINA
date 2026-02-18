package com.machina.api.client.cinema.effect;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;

public record SoundEffect(Supplier<SoundEvent> event) implements CameraEffect {

	@Override
	public void tickEffect(int tick) {
		if (tick == 1) {
			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(event.get(), 1f));
		}
	}
}