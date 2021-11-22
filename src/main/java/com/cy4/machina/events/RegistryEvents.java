package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * @author matyrobbrt
 */
@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD)
public class RegistryEvents {

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event) {
		PlanetTrait.createRegistry(event);
		AdvancedCraftingFunctionSerializer.createRegistry();
	}

}
