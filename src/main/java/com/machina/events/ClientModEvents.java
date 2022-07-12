package com.machina.events;

import com.machina.Machina;
import com.machina.client.ClientStarchart;
import com.machina.client.cinema.CinematicHandler;
import com.machina.client.cinema.effect.ShakeManager;
import com.machina.client.renderer.CargoCrateRenderer;
import com.machina.client.renderer.ComponentAnalyzerRenderer;
import com.machina.client.renderer.ShipConsoleRenderer;
import com.machina.client.screen.BatteryScreen;
import com.machina.client.screen.ComponentAnalyzerScreen;
import com.machina.client.screen.PuzzleScreen;
import com.machina.client.screen.ScannerScreen;
import com.machina.client.screen.ShipConsoleScreen;
import com.machina.client.util.ClientTimer;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerTypesInit;
import com.machina.registration.init.PlanetAttributeTypesInit;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.color.Color;
import com.machina.util.server.PlanetUtils;
import com.machina.world.data.PlanetData;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.chunk.ChunkRenderCache;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

	public static IBlockColor getPlanetColor(int paletteId) {

		final int defVal = 8947848;

		return (state, reader, pos, num) -> {

			World world = null;

			if (reader instanceof World)
				world = (World) reader;

			if (reader instanceof ChunkRenderCache)
				world = ((ChunkRenderCache) reader).level;

			if (world != null) {
				RegistryKey<World> dim = world.dimension();
				if (PlanetUtils.isDimensionPlanet(dim)) {
					PlanetData data = ClientStarchart.getPlanetData(PlanetUtils.getId(dim));
					Color color = data.getAttribute(PlanetAttributeTypesInit.PALETTE)[paletteId];
					return color.getRGB();
				}
			}

			return defVal;
		};
	}

	@SubscribeEvent
	public static void registerBlockColorsEvent(ColorHandlerEvent.Block event) {
		BlockColors col = event.getBlockColors();

		// ID 0
		col.register(getPlanetColor(0), BlockInit.ALIEN_STONE.get());
		col.register(getPlanetColor(0), BlockInit.ALIEN_STONE_SLAB.get());
		col.register(getPlanetColor(0), BlockInit.ALIEN_STONE_STAIRS.get());
		col.register(getPlanetColor(0), BlockInit.TWILIGHT_DIRT.get());
		col.register(getPlanetColor(0), BlockInit.TWILIGHT_DIRT_SLAB.get());
		col.register(getPlanetColor(0), BlockInit.TWILIGHT_DIRT_STAIRS.get());
		col.register(getPlanetColor(0), BlockInit.WASTELAND_DIRT.get());
		col.register(getPlanetColor(0), BlockInit.WASTELAND_DIRT_SLAB.get());
		col.register(getPlanetColor(0), BlockInit.WASTELAND_DIRT_STAIRS.get());

		// ID 1
		col.register(getPlanetColor(1), BlockInit.WASTELAND_SAND.get());
		col.register(getPlanetColor(1), BlockInit.WASTELAND_SANDSTONE.get());
		col.register(getPlanetColor(1), BlockInit.WASTELAND_SANDSTONE_SLAB.get());
		col.register(getPlanetColor(1), BlockInit.WASTELAND_SANDSTONE_STAIRS.get());
		col.register(getPlanetColor(1), BlockInit.WASTELAND_SANDSTONE_WALL.get());
	}

	@SubscribeEvent
	public static void registerRenderers(final FMLClientSetupEvent event) {
		ClientTimer.setup();
		ShakeManager.setup();
		CinematicHandler.setup();
		
		RenderTypeLookup.setRenderLayer(BlockInit.CARGO_CRATE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.SHIP_CONSOLE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.COMPONENT_ANALYZER.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.STEEL_CHASSIS.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.IRON_CHASSIS.get(), RenderType.cutout());

		ScreenManager.register(ContainerTypesInit.SHIP_CONSOLE_CONTAINER_TYPE.get(), ShipConsoleScreen::new);
		ScreenManager.register(ContainerTypesInit.COMPONENT_ANALYZER_CONTAINER_TYPE.get(), ComponentAnalyzerScreen::new);
		ScreenManager.register(ContainerTypesInit.BATTERY_CONTAINER_TYPE.get(), BatteryScreen::new);
		ScreenManager.register(ContainerTypesInit.SCANNER_CONTAINER_TYPE.get(), ScannerScreen::new);
		ScreenManager.register(ContainerTypesInit.PUZZLE_CONTAINER_TYPE.get(), PuzzleScreen::new);
		
		ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.CARGO_CRATE.get(), CargoCrateRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.COMPONENT_ANALYZER.get(), ComponentAnalyzerRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.SHIP_CONSOLE.get(), ShipConsoleRenderer::new);
	}
}
