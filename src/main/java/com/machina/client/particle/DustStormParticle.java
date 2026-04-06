package com.machina.client.particle;

import com.machina.api.client.RenderTypes;
import com.machina.particle.DustStormParticleType.DustStormParticleOptions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.entity.player.Player;

public class DustStormParticle extends TextureSheetParticle {

	private final SpriteSet spriteSet;

	protected DustStormParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed,
			double zSpeed, SpriteSet spriteSet) {
		super(level, x, y, z, 0.0, 0.0, 0.0);
		this.friction = 0.96F;
		this.spriteSet = spriteSet;
		this.xd *= 0.1F;
		this.yd *= 0.1F;
		this.zd *= 0.1F;
		this.xd += xSpeed;
		this.yd += ySpeed;
		this.zd += zSpeed;
		float f1 = 1.0F - (float) (Math.random() * 0.3F);
		this.rCol = f1;
		this.gCol = f1;
		this.bCol = f1;
		this.alpha = 0.3f;
		this.quadSize *= 50.875F;
		int i = (int) (8.0 / (Math.random() * 0.8 + 0.3));
		this.lifetime = (int) Math.max((float) i * 2.5F, 1.0F);
		this.hasPhysics = false;
		this.setSpriteFromAge(spriteSet);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.removed) {
			this.setSpriteFromAge(this.spriteSet);
			Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0, false);
			if (player != null) {
				double d0 = player.getY();
				if (this.y > d0) {
					this.y = this.y + (d0 - this.y) * 0.2;
					this.yd = this.yd + (player.getDeltaMovement().y - this.yd) * 0.2;
					this.setPos(this.x, this.y, this.z);
				}
			}
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return RenderTypes.PARTICLE_SHEET_TRANSLUCENT_DEPTH;
	}

	public static class DustStormParticleProvider implements ParticleProvider<DustStormParticleOptions> {
		private final SpriteSet spriteSet;

		public DustStormParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle createParticle(DustStormParticleOptions type, ClientLevel level, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			return new DustStormParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
		}
	}
}
