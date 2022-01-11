package com.machina.events;

import com.machina.Machina;
import com.machina.api.client.ClientDataHolder;
import com.machina.api.util.Color;
import com.machina.api.util.PlanetUtils;
import com.machina.api.world.data.PlanetData;
import com.machina.init.BlockInit;
import com.machina.init.PlanetAttributeTypesInit;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public static void registerBlockColorsEvent(ColorHandlerEvent.Block event) {

		final int defaultColor = 8947848;

		event.getBlockColors().register((state, reader, pos, num) -> {
			
			System.out.println("HMMMMMMMMMMMMMMMM IS READER WORLD???? >>> " + (reader instanceof World));
			
			if (reader instanceof World) {
				RegistryKey<World> dim = ((World) reader).dimension();
				System.out.println(dim);
				if (PlanetUtils.isDimensionPlanet(dim)) {
					PlanetData data = ClientDataHolder.getPlanetDataByID(PlanetUtils.getId(dim));
					Color color = data.getAttribute(PlanetAttributeTypesInit.COLOUR);
					System.out.println("Correct dim." + color.toString());
					return color.getRGB();
				} else
					return defaultColor;
			} else {
				System.out.println("Reader is not world. Very sad.");
				return defaultColor;
			}
		}, BlockInit.ALIEN_STONE);
	}
}
