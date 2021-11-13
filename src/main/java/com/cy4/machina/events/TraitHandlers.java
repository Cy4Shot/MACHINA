package com.cy4.machina.events;

import com.cy4.machina.Machina;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class TraitHandlers {

//	private static final Random rand = new Random();
//
//	@SubscribeEvent(priority = EventPriority.HIGH)
//	public static void handleGravity(LivingJumpEvent event) {
//		if (CapabilityPlanetTrait.worldHasTrait(event.getEntityLiving().level, PlanetTraitInit.LOW_GRAVITY)
//				&& PlanetUtils.isDimensionPlanet(event.getEntityLiving().level.dimension())) {
//			event.getEntityLiving().setNoGravity(true);
//			Timer timer = new Timer(ServerConfig.LOW_GRAVITY_AIR_TIME.get() * 50,
//					actionEvent -> event.getEntityLiving().setNoGravity(false));
//			timer.setRepeats(false);
//			timer.start();
//		}
//	}
//
//	public static void handleSuperHot(TickEvent.PlayerTickEvent event) {
//		if ((event.side != LogicalSide.SERVER) || !PlanetUtils.isDimensionPlanet(event.player.level.dimension()))
//			return;
//		if (CapabilityPlanetTrait.worldHasTrait(event.player.level, PlanetTraitInit.SUPERHOT)) {
//			if (rand.nextInt(100) <= ServerConfig.SUPERHOT_FIRE_CHANCE.get()) {
//				if (ThermalRegulatorSuit.isFullSuit(event.player)) {
//					if (rand.nextInt(100) <= ServerConfig.SUPERHOT_ARMOUR_DAMAGE_CHANCE.get()) {
//						PlayerHelper.damageAllArmour(event.player, 1);
//					}
//				} else {
//					event.player.setSecondsOnFire(1);
//				}
//			}
//			if (event.player.hasEffect(Effects.FIRE_RESISTANCE)) {
//				event.player.removeEffect(Effects.FIRE_RESISTANCE);
//			}
//		}
//	}

}
