package com.cy4.machina.api.capability.planet_data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPlanetDataStorage {
	
	public static final Map<ResourceLocation, IPlanetDataCapability> PLANET_DATAS = new HashMap<>();
	
	public static Optional<IPlanetDataCapability> getFromWorld(World level) {
		ResourceLocation name = level.dimension().location();
		if (PLANET_DATAS.containsKey(name)) {
			return Optional.of(PLANET_DATAS.get(name));
		}
		return Optional.empty();
	}

}
