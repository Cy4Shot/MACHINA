package com.cy4.machina.api.events.planet;

import java.util.concurrent.atomic.AtomicReference;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.capability.trait.IPlanetTraitCapability;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.util.NullSafe;

import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class PlanetEvent extends Event {

	protected final World planet;

	protected PlanetEvent(World planet) {
		this.planet = planet;
	}

	public World getPlanet() { return planet; }

	/**
	 * Call it when a planet is being created
	 *
	 * @param planet
	 */
	public static void onPlanetCreated(World planet) {
		MinecraftForge.EVENT_BUS.post(new PlanetCreatedEvent(planet));
	}

	/**
	 * Call it when a planet is about to receive a trait
	 *
	 * @param planet
	 * @param trait
	 * @return if true, the event was cancelled
	 */
	public static boolean onTraitAdded(World planet, PlanetTrait trait) {
		return MinecraftForge.EVENT_BUS.post(new TraitAdded(planet, trait));
	}

	/**
	 * Called when a planet is about to receive a trait. If {@link #isCanceled()}
	 * the trait will not be added
	 *
	 * @author matyrobbrt
	 *
	 */
	@Cancelable
	public static class TraitAdded extends PlanetEvent {

		private final PlanetTrait trait;

		private TraitAdded(World planet, PlanetTrait trait) {
			super(planet);
			this.trait = trait;
		}

		public PlanetTrait getTrait() { return trait; }

		public NullSafe<IPlanetTraitCapability> getTraitCapability() {
			AtomicReference<IPlanetTraitCapability> capAtomic = new AtomicReference<>(null);
			planet.getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY).ifPresent(capAtomic::set);
			return new NullSafe<>(capAtomic.get());
		}

	}

}
