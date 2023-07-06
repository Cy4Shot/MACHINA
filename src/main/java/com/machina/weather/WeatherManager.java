package com.machina.weather;

import com.machina.weather.wind.WindManager;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public abstract class WeatherManager {
	public final WindManager wind = new WindManager(this);
	public final RegistryKey<World> dimension;
	
	public WeatherManager(RegistryKey<World> dim) {
		this.dimension = dim;
	}
	
	public abstract World getWorld();
	
	public WindManager getWindManager() {
		return this.wind;
	}
	
	public void tick() {
		World world = getWorld();
		if (world != null) {
			wind.tick(world);
		}
	}
}
