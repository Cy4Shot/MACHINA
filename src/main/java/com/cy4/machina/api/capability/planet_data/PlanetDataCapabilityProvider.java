package com.cy4.machina.api.capability.planet_data;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlanetDataCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

	private final DefaultPlanetDataCapability planetTraitCapability = new DefaultPlanetDataCapability();
	private final LazyOptional<IPlanetDataCapability> planetTraitCapabilityOptional = LazyOptional.of(() -> planetTraitCapability);

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
		if (CapabilityPlanetData.PLANET_DATA_CAPABILITY == null) {
			return new CompoundNBT();
		} else {
			return (CompoundNBT) CapabilityPlanetData.PLANET_DATA_CAPABILITY.writeNBT(planetTraitCapability, null);
		}
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (CapabilityPlanetData.PLANET_DATA_CAPABILITY != null) {
			CapabilityPlanetData.PLANET_DATA_CAPABILITY.readNBT(planetTraitCapability, null, nbt);
		}
	}

}
