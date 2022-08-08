package com.machina.events;

import java.util.Random;

import com.machina.Machina;
import com.machina.registration.Registration;
import com.machina.world.data.StarchartData;
import com.machina.world.feature.planet.PlanetTreeFeature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.ISeedReader;
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
		StarchartData.getDefaultInstance(e.getEntity().getServer()).syncClient((ServerPlayerEntity) e.getPlayer());
	}

	// Debug Event.
	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if(event.getEntity().level.isClientSide()) return;
		PlayerEntity player = event.getPlayer();
		new PlanetTreeFeature().config().place((ISeedReader) player.level, null, new Random(), player.blockPosition());
	}
}
