package com.cy4.machina.events;

import java.util.Random;

import com.cy4.machina.Machina;
import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.events.planet.PlanetCreatedEvent;
import com.cy4.machina.starchart.PlanetData;
import com.cy4.machina.starchart.Starchart;

import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID)
public class PlanetEvents {
	
	@SubscribeEvent
	public static void onPlanetCreated(PlanetCreatedEvent event) {
		if (event.getPlanet().isClientSide())
			return;
		
		event.getPlanet().getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY).ifPresent(cap -> {
			long seed = ((ServerWorld) event.getPlanet()).getSeed();
			Starchart chart = new Starchart(seed);
			chart.planets.get(chart.planets.indexOf(new PlanetData(new Random(seed)))).traits.forEach(cap::addTrait);
		});
	}

}
