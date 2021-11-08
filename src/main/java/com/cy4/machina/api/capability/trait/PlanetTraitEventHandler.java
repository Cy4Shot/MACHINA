package com.cy4.machina.api.capability.trait;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetDimensionModIds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class PlanetTraitEventHandler {

	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
		if (PlanetDimensionModIds.isDimensionPlanet(event.getObject().dimension())) {
			PlanetTraitCapabilityProvider provider = new PlanetTraitCapabilityProvider();
			event.addCapability(new ResourceLocation(Machina.MOD_ID, "traits"), provider);
			event.addListener(provider::invalidate);
		}
	}

}