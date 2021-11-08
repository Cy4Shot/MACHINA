package com.cy4.machina.api.capability.trait;

import com.cy4.machina.api.planet.PlanetTrait;

import net.minecraft.nbt.CollectionNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
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

	/**
	 * Weird way of loading/saving, but it works.
	 * TODO figure out a better way to do it. Maybe using a custom {@link CollectionNBT} of {@link String}s?
	 * @author matyrobbrt
	 *
	 */
	public static class Storage implements Capability.IStorage<IPlanetTraitCapability> {

		@Override
		public INBT writeNBT(Capability<IPlanetTraitCapability> capability, IPlanetTraitCapability instance,
				Direction side) {
			CompoundNBT tag = new CompoundNBT();
			
			CompoundNBT traitsNBT = new CompoundNBT();
			traitsNBT.putInt("size", instance.getTraits().size());
			for (int i = 0; i < instance.getTraits().size(); i++) {
				traitsNBT.putString(String.valueOf(i), instance.getTraits().get(i).getRegistryName().toString());
			}
			
			tag.put("traits", traitsNBT);

			return tag;
		}

		@Override
		public void readNBT(Capability<IPlanetTraitCapability> capability, IPlanetTraitCapability instance,
				Direction side, INBT inbt) {
			CompoundNBT nbt = (CompoundNBT) inbt;
			
			if (nbt.contains("traits")) {
				CompoundNBT traitsNbt = nbt.getCompound("traits");
				int size = traitsNbt.getInt("size");
				for (int i = 0; i < size; i++) {
					if (traitsNbt.contains(String.valueOf(i))) {
						instance.addTrait(PlanetTrait.REGISTRY.getValue(new ResourceLocation(traitsNbt.getString(String.valueOf(i)))));
					}
				}
			}

		}

	}

}
