package com.cy4.machina.api.capability.planet_data;

import java.util.List;

import com.cy4.machina.api.planet.trait.PlanetTrait;

public interface IPlanetDataCapability {

	/**
	 * Adds a new trait to the planet. Duplicate traits <strong>should not</strong> exist.
	 * @param trait the trait to add
	 */
	void addTrait(PlanetTrait trait);

	/**
	 * Removes a trait from the planet
	 * @param trait the trait to remove
	 */
	void removeTrait(PlanetTrait trait);

	/**
	 * @return all the traits a plant has
	 */
	List<PlanetTrait> getTraits();
	
	/**
	 * @return the name of the planet
	 */
	String getName();
	
	/**
	 * Sets the name of the planet
	 * @param name the new name
	 */
	void setName(String name);
	
	/**
	 * Called in order to clear / reset to default any data the capability holds
	 */
	void clear();

}
