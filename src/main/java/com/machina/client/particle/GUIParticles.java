package com.machina.client.particle;

import java.awt.Color;
import java.util.Random;

import team.lodestar.lodestone.setup.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

public class GUIParticles {
	public static void planetGlow(float x, float y, ScreenParticleHolder target) {
		Random random = new Random();
		//@formatter:off
		ScreenParticleBuilder
			.create(LodestoneScreenParticleRegistry.SMOKE, target)
			.setTransparencyData(GenericParticleData.create(0.4f, 0f).setEasing(Easing.SINE_IN_OUT).build())
			.setScaleData(GenericParticleData.create(0.8f + random.nextFloat() * 0.1f, 0)
			.setEasing(Easing.SINE_IN_OUT, Easing.BOUNCE_IN_OUT).build())
			.setColorData(ColorParticleData.create(Color.WHITE, Color.WHITE).build())
			.setLifetime(10 + random.nextInt(10))
			.setRandomOffset(0.05f)
			.setRandomMotion(0.05f, 0.05f)
			.spawn(x, y);
		//@formatter:on
	}
}
