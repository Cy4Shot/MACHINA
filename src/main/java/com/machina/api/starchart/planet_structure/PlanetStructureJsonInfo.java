package com.machina.api.starchart.planet_structure;

import com.machina.api.util.loader.JsonInfo;

import net.minecraft.resources.ResourceLocation;

public record PlanetStructureJsonInfo(
	String id,
	String type,
	Integer spacing,
	Integer separation,
	Integer salt
) implements JsonInfo<PlanetStructure> {

	@Override
	public PlanetStructure cast() {
		return new PlanetStructure(
			ResourceLocation.parse(id),
			type,
			spacing != null ? spacing : PlanetStructure.DEFAULT_SPACING,
			separation != null ? separation : PlanetStructure.DEFAULT_SEPARATION,
			salt != null ? salt : id.hashCode()
		);
	}
}
