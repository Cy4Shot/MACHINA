package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.world.data.StarchartData;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Machina.traitPoolManager);
	}

	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if (event.getEntity().level.isClientSide)
			return;
		MinecraftServer s = ServerLifecycleHooks.getCurrentServer();
		StarchartData.getStarchartForServer(s).generateStarchart(s.getLevel(World.OVERWORLD).getSeed());
		StarchartData.getDefaultInstance(s).syncClients();
		StarchartData.getStarchartForServer(s).debugStarchart();
	}

//	@SubscribeEvent
//	public static void onWorldLoaded(PlayerEvent.PlayerLoggedInEvent event) {
//		if (!event.getPlayer().level.isClientSide()) {
//			StarchartHelper.syncCapabilityWithClients(event.getPlayer().level);
//		}
//	}

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent e) {
		if (e.getEntity().level.isClientSide)
			return;
		System.out.println("SYNCEY TIME");
		StarchartData.getDefaultInstance(e.getEntity().getServer()).syncClient((ServerPlayerEntity) e.getPlayer());
	}

	// @SubscribeEvent
	// public static void handleEffectBan(LivingEntityAddEffectEvent event) {
	// World level = event.getEntity().level;
	// if (PlanetUtils.isDimensionPlanet(level.dimension())) {
	// if (CapabilityPlanetTrait.worldHasTrait(level, PlanetTraitInit.SUPERHOT)
	// && event.getEffect().getEffect() == Effects.FIRE_RESISTANCE) {
	// event.setCanceled(true);
	// }
	// }
	// }
}
