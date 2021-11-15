package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Machina.traitPoolManager);
	}

	@SubscribeEvent
	public static void debug(ItemTossEvent event) {

	}

	@SubscribeEvent
	public static void onWorldLoaded(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().level.isClientSide()) {
			CapabilityPlanetTrait.syncCapabilityWithClients(event.getPlayer().level);
		}
	}

	//	@SubscribeEvent
	//	public static void handleEffectBan(LivingEntityAddEffectEvent event) {
	//		World level = event.getEntity().level;
	//		if (PlanetUtils.isDimensionPlanet(level.dimension())) {
	//			if (CapabilityPlanetTrait.worldHasTrait(level, PlanetTraitInit.SUPERHOT)
	//					&& event.getEffect().getEffect() == Effects.FIRE_RESISTANCE) {
	//				event.setCanceled(true);
	//			}
	//		}
	//	}
}
