package com.machina.registration.init;

import com.machina.util.MachinaRL;

import net.minecraft.util.SoundEvent;

public class SoundInit {

	public static final SoundEvent ROCKET_LAUNCH = create("rocket_launch");

	public static final SoundEvent create(String name) {
		return new SoundEvent(new MachinaRL(name));
	}

}
