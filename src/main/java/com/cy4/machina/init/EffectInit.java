package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterEffect;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

@RegistryHolder
public class EffectInit {

	@RegisterEffect("superhot_resistance")
	public static final Effect SUPERHOT_RESISTANCE = new Effect(EffectType.BENEFICIAL, 0xFF5A28) {};

}
