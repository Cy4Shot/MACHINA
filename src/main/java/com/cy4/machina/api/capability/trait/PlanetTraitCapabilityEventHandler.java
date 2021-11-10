package com.cy4.machina.api.capability.trait;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class PlanetTraitCapabilityEventHandler {

	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
		// pretty sure that it is only put on the server if i don't check for client side
		if (event.getObject().isClientSide()) {
			attachPlanetTraitCap(event);
		} else {
			attachPlanetTraitCap(event);
		}
	}
	
	private static void attachPlanetTraitCap(AttachCapabilitiesEvent<World> event) {
		if (PlanetUtils.isDimensionPlanet(event.getObject().dimension())) {
			PlanetTraitCapabilityProvider provider = new PlanetTraitCapabilityProvider();
			event.addCapability(new ResourceLocation(Machina.MOD_ID, "traits"), provider);
			event.addListener(provider::invalidate);
		}
	}

}
