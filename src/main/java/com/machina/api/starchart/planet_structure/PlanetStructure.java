package com.machina.api.starchart.planet_structure;

import net.minecraft.resources.ResourceLocation;

public record PlanetStructure(ResourceLocation id, String type, int spacing, int separation, int salt) {
	public static final int DEFAULT_SPACING = 16;
	public static final int DEFAULT_SEPARATION = 8;
}
