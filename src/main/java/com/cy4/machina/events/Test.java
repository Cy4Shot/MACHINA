package com.cy4.machina.events;

import com.cy4.machina.api.planet.PlanetTrait;

import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = "machina", bus = Bus.FORGE)
public class Test {
	@SubscribeEvent
	public static void onTest(ItemTossEvent event) {
		
		System.out.println("TEST");
		System.out.println(PlanetTrait.registry.getEntries().size());
		
		PlanetTrait.registry.getEntries().forEach(entry -> {
			System.out.println(entry.getKey().getRegistryName());
			System.out.println(entry.getValue().test);
		});
	}

}
