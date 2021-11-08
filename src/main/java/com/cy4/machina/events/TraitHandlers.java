package com.cy4.machina.events;

import java.util.Random;

import com.cy4.machina.Machina;
import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.planet.PlanetDimensionModIds;
import com.cy4.machina.config.MachinaConfig;
import com.cy4.machina.init.PlanetTraitInit;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class TraitHandlers {
	
	private static final Random rand = new Random();
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void handleGravity(LivingJumpEvent event) {
		if (CapabilityPlanetTrait.worldHasTrait(event.getEntityLiving().level, PlanetTraitInit.LOW_GRAVITY) && PlanetDimensionModIds.isDimensionPlanet(event.getEntityLiving().level.dimension())) {
			event.getEntityLiving().setNoGravity(true);
			try {
				Thread.sleep(MachinaConfig.LOW_GRAVITY_AIR_TIME.get() * 50);
				event.getEntityLiving().setNoGravity(false);
			} catch (InterruptedException e) {
				Machina.LOGGER.warn("The thread that was supposed to wait in order to give the player the gravity it lost by jumping in a planet with the Low Gravity trait was interrupted. The gravity was given back earlier! {}", e);
				event.getEntityLiving().setNoGravity(false);
			}
		}
		//event.getEntityLiving().setNoGravity(CapabilityPlanetTrait.worldHasTrait(event.getEntityLiving().level, PlanetTraitInit.LOW_GRAVITY));
	}
	
	public static void handleSuperHot(TickEvent.PlayerTickEvent event) {
		//event.player.setNoGravity(false);
		if (!PlanetDimensionModIds.isDimensionPlanet(event.player.level.dimension()))
			return;
		if (CapabilityPlanetTrait.worldHasTrait(event.player.level, PlanetTraitInit.SUPERHOT) && rand.nextInt(100) < 10) {
			event.player.setSecondsOnFire(2);
		}
	}

}
