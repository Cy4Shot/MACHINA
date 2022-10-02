package com.machina.registration.init;

import com.machina.Machina;
import com.machina.util.text.MachinaRL;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundInit {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
			Machina.MOD_ID);

	public static final Sound ROCKET_LAUNCH = create("rocket_launch");
	public static final Sound BEYOND = create("beyond");
	public static final Sound MUSIC = create("music");
	public static final Sound BOSS_01 = create("boss_01");
	public static final Sound BOSS_02 = create("boss_02");
	public static final Sound BOSS_03 = create("boss_03");

	
	public static final Sound create(String name) {
		return new Sound(name);
	}

	public static class Sound {
		private SoundEvent sound;
		private String name;

		public Sound(String name) {
			this.sound = new SoundEvent(new MachinaRL(name));
			this.name = name;
			SOUNDS.register(name, () -> this.sound);
		}

		public SoundEvent sound() {
			return sound;
		}

		public String name() {
			return name;
		}
	}

}
