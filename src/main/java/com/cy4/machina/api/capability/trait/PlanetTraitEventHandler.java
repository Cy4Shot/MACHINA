package com.cy4.machina.api.capability.trait;

import com.cy4.machina.Machina;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class PlanetTraitEventHandler {

	// @SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
		// TODO check if the world is a planet dimension
		PlanetTraitCapabilityProvider provider = new PlanetTraitCapabilityProvider();
		event.addCapability(new ResourceLocation(Machina.MOD_ID, "traits"), provider);
		event.addListener(provider::invalidate);
	}

}
