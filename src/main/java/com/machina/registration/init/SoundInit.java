package com.machina.registration.init;

import com.machina.util.text.MachinaRL;

import net.minecraft.util.SoundEvent;

public class SoundInit {

	public static final Sound ROCKET_LAUNCH = create("rocket_launch");

	public static final Sound create(String name) {
		return new Sound(name);
	}
	
	public static class Sound {
		private SoundEvent sound;
		private String name;
		
		public Sound(String name) {
			this.sound = new SoundEvent(new MachinaRL(name));
			this.name = name;
		}
		
		public SoundEvent sound() {
			return sound;
		}
		
		public String name() {
			return name;
		}
	}

}
