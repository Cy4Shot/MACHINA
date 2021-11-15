package com.cy4.machina.api.capability.trait;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlanetTraitCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

	private final DefaultPlanetTraitCapability planetTraitCapability = new DefaultPlanetTraitCapability();
	private final LazyOptional<IPlanetTraitCapability> planetTraitCapabilityOptional = LazyOptional.of(() -> planetTraitCapability);

	public void invalidate() {
		planetTraitCapabilityOptional.invalidate();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return planetTraitCapabilityOptional.cast();
	}

	@Override
	public CompoundNBT serializeNBT() {
		if (CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY == null)
			return new CompoundNBT();
		else
			return (CompoundNBT) CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY.writeNBT(planetTraitCapability, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY != null) {
			CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY.readNBT(planetTraitCapability, null, nbt);
		}
	}

}
