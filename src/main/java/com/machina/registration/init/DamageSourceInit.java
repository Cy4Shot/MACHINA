package com.machina.registration.init;

import com.machina.Machina;

import net.minecraft.util.DamageSource;

public class DamageSourceInit {

	public static final DamageSource SUFFOCATE = create("suffocate").bypassArmor().bypassMagic();

	public static DamageSource create(String name) {
		return new DamageSource(Machina.MOD_ID + "." + name);
	}
}
