package com.machina.client.particle;

import com.machina.particle.BiomeAshParticleType.BiomeAshParticleOptions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BiomeAshParticle extends BaseAshSmokeParticle {

	protected BiomeAshParticle(
		ClientLevel level,
		double x,
		double y,
		double z,
		double xSpeed,
		double ySpeed,
		double zSpeed,
		float quadSizeMultiplier,
		SpriteSet sprites,
		int color
	) {
		super(level, x, y, z, 0.1F, -0.1F, 0.1F, xSpeed, ySpeed, zSpeed, quadSizeMultiplier, sprites, 0.0F, 20, 0.0125F, false);
		this.rCol = (float) FastColor.ARGB32.red(color) / 255.0F;
		this.gCol = (float) FastColor.ARGB32.green(color) / 255.0F;
		this.bCol = (float) FastColor.ARGB32.blue(color) / 255.0F;
		this.alpha = (float) FastColor.ARGB32.alpha(color) / 255.0F;
		if (this.alpha == 0) {
			this.alpha = 1.0F;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class BiomeAshParticleProvider implements ParticleProvider<BiomeAshParticleOptions> {
		private final SpriteSet sprites;

		public BiomeAshParticleProvider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(
			BiomeAshParticleOptions options,
			ClientLevel level,
			double x,
			double y,
			double z,
			double xSpeed,
			double ySpeed,
			double zSpeed
		) {
			RandomSource random = level.random;
			double d0 = (double) random.nextFloat() * -1.9 * (double) random.nextFloat() * 0.1;
			double d1 = (double) random.nextFloat() * -0.5 * (double) random.nextFloat() * 0.1 * 5.0;
			double d2 = (double) random.nextFloat() * -1.9 * (double) random.nextFloat() * 0.1;
			return new BiomeAshParticle(level, x, y, z, d0, d1, d2, 1.0F, this.sprites, options.color());
		}
	}
}
