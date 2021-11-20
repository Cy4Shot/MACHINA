package com.cy4.machina.api.util.helper;

import java.util.concurrent.atomic.AtomicBoolean;

import com.cy4.machina.api.capability.planet_data.CapabilityPlanetData;
import com.cy4.machina.api.capability.planet_data.IPlanetDataCapability;
import com.cy4.machina.api.events.planet.PlanetEvent;
import com.cy4.machina.api.network.BaseNetwork;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.network.MachinaNetwork;
import com.cy4.machina.network.message.S2CSyncTraitsCapability;

import net.minecraft.world.World;

/**
 * Utility class which should redirect all the direct calls of
 * {@link CapabilityPlanetData#PLANET_DATA_CAPABILITY}
 * 
 * @author matyrobbrt
 *
 */
public class StarchartHelper {

	private StarchartHelper() {
	}

	public static boolean worldHasTrait(World world, PlanetTrait trait) {
		AtomicBoolean value = new AtomicBoolean(false);
		world.getCapability(CapabilityPlanetData.PLANET_DATA_CAPABILITY)
				.ifPresent(cap -> value.set(cap.getTraits().contains(trait)));
		return value.get();
	}

	/**
	 * Call in order to sync the traits from the capability to all the players that
	 * are connected to the server
	 *
	 * @param world
	 */
	public static void syncCapabilityWithClients(World world) {
		world.getCapability(CapabilityPlanetData.PLANET_DATA_CAPABILITY).ifPresent(cap -> BaseNetwork
				.sendToAll(MachinaNetwork.CHANNEL, new S2CSyncTraitsCapability(cap, world.dimension().location())));
	}

	/**
	 * This method adds the specified traits to the world, whilst also calling
	 * {@link syncCapabilityWithClients}<br>
	 * It is preferred to use this method instead of
	 * {@link IPlanetDataCapability#addTrait(PlanetTrait)}
	 *
	 * @param level
	 * @param traits
	 */
	public static void addTrait(World level, PlanetTrait... traits) {
		level.getCapability(CapabilityPlanetData.PLANET_DATA_CAPABILITY).ifPresent(cap -> {
			for (PlanetTrait trait : traits) {
				if (!PlanetEvent.onTraitAdded(level, trait)) {
					cap.addTrait(trait);
				}
			}
		});
		syncCapabilityWithClients(level);
	}

	/**
	 * This method removes the specified traits from the world, whilst also calling
	 * {@link syncCapabilityWithClients} <br>
	 * It is preferred to use this method instead of
	 * {@link IPlanetDataCapability#removeTrait(PlanetTrait)}
	 *
	 * @param level
	 * @param traits
	 */
	public static void removeTrait(World level, PlanetTrait... traits) {
		level.getCapability(CapabilityPlanetData.PLANET_DATA_CAPABILITY).ifPresent(cap -> {
			for (PlanetTrait trait : traits) {
				if (cap.getTraits().contains(trait)) {
					cap.removeTrait(trait);
				}
			}
		});
		syncCapabilityWithClients(level);
	}

}
