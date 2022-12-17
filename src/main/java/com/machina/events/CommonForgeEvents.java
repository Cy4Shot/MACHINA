package com.machina.events;

import com.machina.Machina;
import com.machina.registration.Registration;
import com.machina.world.data.ResearchData;
import com.machina.world.data.StarchartData;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
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
		event.addListener(Registration.TRAIT_POOL_MANAGER);
	}

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent e) {
		if (e.getEntity().level.isClientSide())
			return;
		MinecraftServer server = e.getEntity().getServer();
		ServerPlayerEntity player = (ServerPlayerEntity) e.getPlayer();
		StarchartData.getDefaultInstance(server).syncClient(player);
		ResearchData.getDefaultInstance(server).syncClient(player);
	}

	// Debug Event.
	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if (event.getEntity().level.isClientSide())
			return;
	}
}
