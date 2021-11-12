package com.cy4.machina.api.capability.trait;

import java.util.concurrent.atomic.AtomicBoolean;

import com.cy4.machina.api.network.BaseNetwork;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.network.MachinaNetwork;
import com.cy4.machina.network.message.to_client.SyncTraitsCapabilityMessage;

import net.minecraft.nbt.CollectionNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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

	public static boolean worldHasTrait(World world, PlanetTrait trait) {
		AtomicBoolean value = new AtomicBoolean(false);
		world.getCapability(PLANET_TRAIT_CAPABILITY).ifPresent(cap -> value.set(cap.getTraits().contains(trait)));
		return value.get();
	}

	/**
	 * Call in order to sync the traits from the capability to all the players that
	 * are connected to the server
	 * 
	 * @param world
	 */
	public static void syncCapabilityWithClients(World world) {
		world.getCapability(PLANET_TRAIT_CAPABILITY).ifPresent(
				cap -> BaseNetwork.sendToAll(MachinaNetwork.CHANNEL, new SyncTraitsCapabilityMessage(cap), world));
	}

	/**
	 * This method adds the specified traits to the world, whilst also calling
	 * {@link #syncCapabilityWithClients(World)}<br>
	 * It is preferred to use this method instead of
	 * {@link IPlanetTraitCapability#addTrait(PlanetTrait)}
	 * 
	 * @param level
	 * @param traits
	 */
	public static void addTrait(World level, PlanetTrait... traits) {
		level.getCapability(PLANET_TRAIT_CAPABILITY).ifPresent(cap -> {
			for (PlanetTrait trait : traits) {
				cap.addTrait(trait);
			}
			if (!level.isClientSide()) {
				syncCapabilityWithClients(level);
			}
		});
	}

	/**
	 * This method removes the specified traits from the world, whilst also calling
	 * {@link #syncCapabilityWithClients(World)} <br>
	 * It is preferred to use this method instead of
	 * {@link IPlanetTraitCapability#removeTrait(PlanetTrait)}
	 * 
	 * @param level
	 * @param traits
	 */
	public static void removeTrait(World level, PlanetTrait... traits) {
		level.getCapability(PLANET_TRAIT_CAPABILITY).ifPresent(cap -> {
			for (PlanetTrait trait : traits) {
				if (cap.getTraits().contains(trait)) {
					cap.removeTrait(trait);
				}
			}
			if (!level.isClientSide()) {
				syncCapabilityWithClients(level);
			}
		});
	}

	/**
	 * Weird way of loading/saving, but it works. TODO figure out a better way to do
	 * it. Maybe using a custom {@link CollectionNBT} of {@link String}s?
	 * 
	 * @author matyrobbrt
	 *
	 */
	public static class Storage implements Capability.IStorage<IPlanetTraitCapability> {

		@Override
		public INBT writeNBT(Capability<IPlanetTraitCapability> capability, IPlanetTraitCapability instance,
				Direction side) {
			return serialize(instance);
		}

		public static CompoundNBT serialize(IPlanetTraitCapability instance) {
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
			deserialize(nbt, instance);

		}

		public static void deserialize(CompoundNBT nbt, IPlanetTraitCapability instance) {
			if (nbt.contains("traits")) {
				CompoundNBT traitsNbt = nbt.getCompound("traits");
				int size = traitsNbt.getInt("size");
				for (int i = 0; i < size; i++) {
					if (traitsNbt.contains(String.valueOf(i))) {
						instance.addTrait(PlanetTrait.REGISTRY
								.getValue(new ResourceLocation(traitsNbt.getString(String.valueOf(i)))));
					}
				}
			}
		}
	}

}
