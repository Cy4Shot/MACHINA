package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.events.planet.PlanetCreatedEvent;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.starchart.Starchart;
import com.cy4.machina.world.data.StarchartData;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class PlanetEvents {

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onPlanetCreated(PlanetCreatedEvent event) {
		if (event.getPlanet().isClientSide())
			return;

		event.getPlanet().getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY).ifPresent(cap -> {
			Starchart sc = StarchartData.getDefaultInstance(event.getPlanet().getServer()).starchart;
			try {
				int id = Integer.parseInt(event.getPlanet().dimension().location().getPath());
				if (sc.planets.size() - 1 >= id) {
					CapabilityPlanetTrait.addTrait(event.getPlanet(),
							sc.planets.get(id).traits.toArray(new PlanetTrait[] {}));
				} else {
//					PlanetData.getTraits(new Random(seed)).forEach(cap::addTrait);
					// MATY WHAT IS THIS??? SHOULDNT HAPPEN
					// Yes it should cy4
				}
			} catch (NumberFormatException e) {
				// Error
			}
		});
	}

}
