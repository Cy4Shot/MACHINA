package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.starchart.PlanetData;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {
	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Machina.TRAIT_POOL_MANAGER);
	}
	
	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		new PlanetData(100L);
	}
}
