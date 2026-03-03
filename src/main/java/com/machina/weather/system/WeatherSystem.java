package com.machina.weather.system;

import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_type.PlanetType;
import com.machina.api.util.PlanetHelper;
import com.machina.weather.WeatherEvent;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public abstract class WeatherSystem {

	final Level level;
	final Planet planet;
	final PlanetType type;

	public WeatherSystem(Level level) {
		this.level = level;
		this.planet = PlanetHelper.getPlanetFor(level);
		this.type = this.planet.type();
	}

	public void tick() {

	}

	public abstract WeatherEvent getCurrentEvent();
	
	public ResourceKey<Level> getDimension() {
		return this.level.dimension();
	}
}
