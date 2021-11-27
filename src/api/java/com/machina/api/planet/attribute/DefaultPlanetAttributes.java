package com.machina.api.planet.attribute;

import static com.machina.api.planet.attribute.PlanetAttributeType.REGISTRY;

import com.machina.api.util.Color;
import com.machina.api.util.MachinaRL;

@SuppressWarnings("unchecked")
public class DefaultPlanetAttributes {
	
	public static final PlanetAttributeType<Double> GRAVITY = (PlanetAttributeType<Double>) REGISTRY.getValue(new MachinaRL("gravity"));
	
	public static final PlanetAttributeType<String> PLANET_NAME = (PlanetAttributeType<String>) REGISTRY.getValue(new MachinaRL("planet_name"));
	
	public static final PlanetAttributeType<Color> COLOUR = (PlanetAttributeType<Color>) REGISTRY.getValue(new MachinaRL("colour"));
	
	public static final PlanetAttributeType<Float> ATMOSPHERIC_PRESSURE = (PlanetAttributeType<Float>) REGISTRY.getValue(new MachinaRL("atmospheric_pressure"));
	
	public static final PlanetAttributeType<Float> TEMPERATURE = (PlanetAttributeType<Float>) REGISTRY.getValue(new MachinaRL("temperature"));

}
