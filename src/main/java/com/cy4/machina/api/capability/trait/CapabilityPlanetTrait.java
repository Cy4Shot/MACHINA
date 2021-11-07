package com.cy4.machina.api.capability.trait;

import com.cy4.machina.api.planet.PlanetTrait;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityPlanetTrait {

	@CapabilityInject(IPlanetTraitCapability.class)
	public static Capability<IPlanetTraitCapability> PLANET_TRAIT_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlanetTraitCapability.class, new Storage(),
				DefaultPlanetTraitCapability::new);
	}

	public static class Storage implements Capability.IStorage<IPlanetTraitCapability> {

		@Override
		public INBT writeNBT(Capability<IPlanetTraitCapability> capability, IPlanetTraitCapability instance,
				Direction side) {
			CompoundNBT tag = new CompoundNBT();

			ListNBT traitsNbt = new ListNBT();

			instance.getTraits().forEach(trait -> {
				ResourceLocation name = trait.getRegistryName();
				CompoundNBT traitNbt = new CompoundNBT();
				traitNbt.putString("name", name.toString());
				traitsNbt.add(traitNbt);
			});

			tag.put("traits", traitsNbt);

			return tag;
		}

		
		// TODO Make it actually read
		@Override
		public void readNBT(Capability<IPlanetTraitCapability> capability, IPlanetTraitCapability instance,
				Direction side, INBT inbt) {
			CompoundNBT nbt = (CompoundNBT) inbt;
			ListNBT traitsNbt = nbt.getList("traits", 9);

			traitsNbt.forEach(INBT -> {
				CompoundNBT traitData = (CompoundNBT) INBT;
				ResourceLocation name = new ResourceLocation(traitData.getString("name"));
				instance.addTrait(PlanetTrait.registry.getValue(name));
			});

		}

	}

}
