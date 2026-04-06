package com.machina.datagen.client;

import com.machina.registration.init.ParticleTypeInit;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DatagenParticleDescriptions extends ParticleDescriptionProvider {

	public DatagenParticleDescriptions(PackOutput po, ExistingFileHelper fileHelper) {
		super(po, fileHelper);
	}

	@Override
	protected void addDescriptions() {
		spriteSet(ParticleTypeInit.DUST_STORM, 21);
	}
	
	private void spriteSet(DeferredHolder<ParticleType<?>, ? extends ParticleType<?>> particle, int count) {
		spriteSet(particle.get(), particle.getId(), count, false);
	}
}
