package com.machina.events;

import com.machina.Machina;
import com.machina.api.starchart.Starchart;
import com.machina.registration.Registration;
import com.machina.world.PlanetRegistrationHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class CommonForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Registration.MULTIBLOCK_LOADER);
	}

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent e) {
		if (e.getEntity().level().isClientSide())
			return;

		Starchart.syncClient((ServerPlayer) e.getEntity());
	}

	@SubscribeEvent
	public static void onDebug(final ItemTossEvent event) {
		if (!event.getPlayer().level().isClientSide()) {
			ServerLevel planet = PlanetRegistrationHandler.createPlanet(event.getPlayer().getServer(), "1");
			PlanetRegistrationHandler.sendPlayerToDimension((ServerPlayer) event.getPlayer(), planet,
					new BlockPos(0, 100, 0));
		}
	}
}
