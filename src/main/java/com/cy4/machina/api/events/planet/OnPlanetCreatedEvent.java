package com.cy4.machina.api.events.planet;

import net.minecraft.world.World;

/**
 * Called when a planet is created
 * @author matyrobbrt
 *
 */
public class OnPlanetCreatedEvent extends PlanetEvent {

	protected OnPlanetCreatedEvent(World planet) {
		super(planet);
	}

}
