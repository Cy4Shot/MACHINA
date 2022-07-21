package com.machina.events;

import com.machina.Machina;
import com.machina.client.ClientStarchart;
import com.machina.client.cinema.CinematicHandler;
import com.machina.client.cinema.effect.ShakeManager;
import com.machina.client.renderer.CargoCrateRenderer;
import com.machina.client.renderer.ComponentAnalyzerRenderer;
import com.machina.client.renderer.ShipConsoleRenderer;
import com.machina.client.screen.AtmosphericSeparatorScreen;
import com.machina.client.screen.BatteryScreen;
import com.machina.client.screen.ComponentAnalyzerScreen;
import com.machina.client.screen.FuelStorageScreen;
import com.machina.client.screen.PressurizedChamberScreen;
import com.machina.client.screen.PuzzleScreen;
import com.machina.client.screen.ScannerScreen;
import com.machina.client.screen.ShipConstructScreen;
import com.machina.client.screen.ShipLaunchScreen;
import com.machina.client.screen.TankScreen;
import com.machina.client.screen.TemperatureRegulatorScreen;
import com.machina.client.util.ClientTimer;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.TileEntityInit;
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
					Color color = data.getAttribute(AttributeInit.PALETTE)[paletteId];
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
		//@formatter:off
		ClientTimer.setup();
		ShakeManager.setup();
		CinematicHandler.setup();
		
		RenderTypeLookup.setRenderLayer(BlockInit.CARGO_CRATE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.SHIP_CONSOLE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.COMPONENT_ANALYZER.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.STEEL_CHASSIS.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.IRON_CHASSIS.get(), RenderType.cutout());
		FluidInit.setRenderLayers();

		ScreenManager.register(ContainerInit.SHIP_CONSTRUCT.get(), ShipConstructScreen::new);
		ScreenManager.register(ContainerInit.SHIP_LAUNCH.get(), ShipLaunchScreen::new);
		ScreenManager.register(ContainerInit.COMPONENT_ANALYZER.get(), ComponentAnalyzerScreen::new);
		ScreenManager.register(ContainerInit.BATTERY.get(), BatteryScreen::new);
		ScreenManager.register(ContainerInit.TANK.get(), TankScreen::new);
		ScreenManager.register(ContainerInit.SCANNER.get(), ScannerScreen::new);
		ScreenManager.register(ContainerInit.PUZZLE.get(), PuzzleScreen::new);
		ScreenManager.register(ContainerInit.ATMOSPHERIC_SEPARATOR.get(), AtmosphericSeparatorScreen::new);
		ScreenManager.register(ContainerInit.TEMPERATURE_REGULATOR.get(), TemperatureRegulatorScreen::new);
		ScreenManager.register(ContainerInit.PRESSURIZED_CHAMBER.get(), PressurizedChamberScreen::new);
		ScreenManager.register(ContainerInit.FUEL_STORAGE_UNIT.get(), FuelStorageScreen::new);
		
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.CARGO_CRATE.get(), CargoCrateRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.COMPONENT_ANALYZER.get(), ComponentAnalyzerRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.SHIP_CONSOLE.get(), ShipConsoleRenderer::new);
		//@formatter:on
	}
}
