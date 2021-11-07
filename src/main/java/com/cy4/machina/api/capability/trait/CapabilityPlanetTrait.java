package com.cy4.machina.api.capability.trait;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityPlanetTrait {

	@CapabilityInject(IPlanetTraitCapability.class)
	public static Capability<IPlanetTraitCapability> PLANET_TRAIT_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlanetTraitCapability.class, new Storage(), DefaultPlanetTraitCapability::new);
	}

	public static class Storage implements Capability.IStorage<IPlanetTraitCapability> {

		@Override
		public INBT writeNBT(Capability<IPlanetTraitCapability> capability, IPlanetTraitCapability instance,
				Direction side) {
			CompoundNBT tag = new CompoundNBT();

			// TODO serialize traits

			return tag;
		}

		@Override
		public void readNBT(Capability<IPlanetTraitCapability> capability, IPlanetTraitCapability instance,
				Direction side, INBT nbt) {
			// TODO deserialize traits
		}

	}

}
