package com.machina.api.client.particle;

import java.awt.Color;
import java.util.Random;

import net.minecraft.world.phys.Vec2;
import team.lodestar.lodestone.setup.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

public class GUIParticles {
	public static void planetGlow(Vec2 xy, ScreenParticleHolder target, Color col) {
		Random random = new Random();
		//@formatter:off
//		if(random.nextInt(5) == 0)
		ScreenParticleBuilder
			.create(LodestoneScreenParticleRegistry.SMOKE, target)
			.setTransparencyData(GenericParticleData.create(0.4f, 0f).setEasing(Easing.SINE_IN_OUT).build())
			.setScaleData(GenericParticleData.create(0.8f + random.nextFloat() * 0.1f, 0)
			.setEasing(Easing.SINE_IN_OUT, Easing.BOUNCE_IN_OUT).build())
			.setColorData(ColorParticleData.create(col, col).build())
			.setLifetime(20 + random.nextInt(10))
			.spawn(xy.x, xy.y);
		//@formatter:on
	}
}
