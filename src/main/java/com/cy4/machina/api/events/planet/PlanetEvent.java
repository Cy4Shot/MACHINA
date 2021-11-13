package com.cy4.machina.api.events.planet;

import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public abstract class PlanetEvent extends Event {

	private final World planet;

	protected PlanetEvent(World planet) {
		this.planet = planet;
	}

	public World getPlanet() { return planet; }

	/**
	 * Call it when a planet is being created
	 * @param planet
	 */
	public static void onPlanetCreated(World planet) {
		MinecraftForge.EVENT_BUS.post(new PlanetCreatedEvent(planet));
	}

}
