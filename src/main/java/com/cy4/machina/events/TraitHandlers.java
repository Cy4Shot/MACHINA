package com.cy4.machina.events;

import java.util.Random;

import javax.swing.Timer;

import com.cy4.machina.Machina;
import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.planet.PlanetDimensionModIds;
import com.cy4.machina.config.ServerConfig;
import com.cy4.machina.init.EffectInit;
import com.cy4.machina.init.PlanetTraitInit;

import net.minecraft.potion.Effects;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class TraitHandlers {

	private static final Random rand = new Random();

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void handleGravity(LivingJumpEvent event) {
		if (CapabilityPlanetTrait.worldHasTrait(event.getEntityLiving().level, PlanetTraitInit.LOW_GRAVITY)
				&& PlanetDimensionModIds.isDimensionPlanet(event.getEntityLiving().level.dimension())) {
			event.getEntityLiving().setNoGravity(true);
			Timer timer = new Timer(ServerConfig.LOW_GRAVITY_AIR_TIME.get() * 50,
					actionEvent -> event.getEntityLiving().setNoGravity(false));
			timer.setRepeats(false);
			timer.start();
		}
	}
	
	public static void handleSuperHot(TickEvent.PlayerTickEvent event) {
		if ((event.side != LogicalSide.SERVER)
				|| !PlanetDimensionModIds.isDimensionPlanet(event.player.level.dimension()))
			return;
		if (CapabilityPlanetTrait.worldHasTrait(event.player.level, PlanetTraitInit.SUPERHOT)) {
			if (rand.nextInt(100) < ServerConfig.SUPERHOT_FIRE_CHANCE.get() && !event.player.hasEffect(EffectInit.SUPERHOT_RESISTANCE)) {
				event.player.setSecondsOnFire(1);
			}
			if (event.player.hasEffect(Effects.FIRE_RESISTANCE)) {
				event.player.removeEffect(Effects.FIRE_RESISTANCE);
			}
		}
	}

}
