package com.machina.api.client.cinema.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

public record SoundEffect(RegistryObject<SoundEvent> event) implements CameraEffect {

    @Override
    public void tickEffect(int tick) {
        if (tick == 1) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(event.get(), 1f));
        }
    }
}