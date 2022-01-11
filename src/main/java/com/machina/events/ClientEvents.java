package com.machina.events;

import com.machina.Machina;
import com.machina.api.client.ClientDataHolder;
import com.machina.api.util.Color;
import com.machina.api.util.PlanetUtils;
import com.machina.api.world.data.PlanetData;
import com.machina.init.BlockInit;
import com.machina.init.PlanetAttributeTypesInit;

import net.minecraft.client.renderer.chunk.ChunkRenderCache;
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
		event.getBlockColors().register((state, reader, pos, num) -> {

			World world = null;

			if (reader instanceof World)
				world = (World) reader;

			if (reader instanceof ChunkRenderCache)
				world = ((ChunkRenderCache) reader).level;

			if (world != null) {
				RegistryKey<World> dim = world.dimension();
				if (PlanetUtils.isDimensionPlanet(dim)) {
					PlanetData data = ClientDataHolder.getPlanetDataByID(PlanetUtils.getId(dim));
					Color color = data.getAttribute(PlanetAttributeTypesInit.PRIMARY_COLOUR);
					return color.getRGB();
				}
			}

			return 8947848;
		}, BlockInit.ALIEN_STONE, BlockInit.TWILIGHT_DIRT);
	}
}
