package com.cy4.machina.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;

public abstract class BaseParticle extends SpriteTexturedParticle {

	protected BaseParticle(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		float f = random.nextFloat() * 1.0f;
		rCol = f;
		gCol = f;
		bCol = f;

		this.setSize(12f, 12f);
		quadSize *= random.nextFloat() * 1.1F;
		xd *= 0.02f;
		yd *= 0.02f;
		zd *= 0.02f;
		lifetime = (int)(20.0D / (Math.random() * 1.0D));

	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

}
