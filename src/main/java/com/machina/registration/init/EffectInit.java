package com.machina.registration.init;

import com.machina.Machina;
import com.machina.effect.RadiationEffect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectInit {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT,
			Machina.MOD_ID);

	public static final DeferredHolder<MobEffect, RadiationEffect> RADIATION = EFFECTS.register("radiation",
			RadiationEffect::new);
}
