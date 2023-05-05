package com.machina.events;

import java.util.Arrays;

import com.machina.Machina;
import com.machina.block.tile.basic.TintedTileEntity;
import com.machina.block.tinted.ITinted;
import com.machina.client.ClientStarchart;
import com.machina.client.cinema.CinematicHandler;
import com.machina.client.model.OreModelLoader;
import com.machina.client.screen.AtmosphericSeparatorScreen;
import com.machina.client.screen.BatteryScreen;
import com.machina.client.screen.BlueprinterScreen;
import com.machina.client.screen.ComponentAnalyzerScreen;
import com.machina.client.screen.FabricatorScreen;
import com.machina.client.screen.FuelStorageUnitScreen;
import com.machina.client.screen.FurnaceGeneratorScreen;
import com.machina.client.screen.HaberScreen;
import com.machina.client.screen.MixerScreen;
import com.machina.client.screen.PuzzleScreen;
import com.machina.client.screen.ScannerScreen;
import com.machina.client.screen.ShipConstructScreen;
import com.machina.client.screen.ShipLaunchScreen;
import com.machina.client.screen.StateConverterScreen;
import com.machina.client.screen.TankScreen;
import com.machina.client.screen.TemperatureRegulatorScreen;
import com.machina.client.shader.ShaderHandler;
import com.machina.client.ter.CargoCrateRenderer;
import com.machina.client.ter.ComponentAnalyzerRenderer;
import com.machina.client.ter.CustomModelRenderer;
import com.machina.client.ter.ShipConsoleRenderer;
import com.machina.client.ter.TankRenderer;
import com.machina.client.util.ClientTimer;
import com.machina.item.ShipComponentItem;
import com.machina.item.TintedItem;
import com.machina.planet.OreType;
import com.machina.planet.PlanetData;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.Color;
import com.machina.util.MachinaRL;
import com.machina.util.helper.PlanetHelper;
import com.machina.world.gen.PlanetPaletteGenerator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.chunk.ChunkRenderCache;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

	@SubscribeEvent
	public static void registerRenderers(final FMLClientSetupEvent event) {
		//@formatter:off
		ClientTimer.setup();
		CinematicHandler.setup();
		ShaderHandler.setup();
		
		RenderTypeLookup.setRenderLayer(BlockInit.CARGO_CRATE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.SHIP_CONSOLE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.COMPONENT_ANALYZER.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(BlockInit.STEEL_SCAFFOLDING.get(), RenderType.translucent());
		RenderTypeLookup.setRenderLayer(BlockInit.IRON_SCAFFOLDING.get(), RenderType.translucent());
		RenderTypeLookup.setRenderLayer(BlockInit.ALUMINUM_SCAFFOLDING.get(), RenderType.translucent());
		RenderTypeLookup.setRenderLayer(BlockInit.COPPER_SCAFFOLDING.get(), RenderType.translucent());
		RenderTypeLookup.setRenderLayer(BlockInit.TANK.get(), RenderType.translucent());
		BlockInit.ORE_MAP.values().forEach(m -> {
			m.values().forEach(b -> {
				RenderTypeLookup.setRenderLayer(b.get(), RenderType.cutout());
			});
		});
		FluidInit.setRenderLayers();

		ScreenManager.register(ContainerInit.BLUEPRINTER.get(), BlueprinterScreen::new);
		ScreenManager.register(ContainerInit.FABRICATOR.get(), FabricatorScreen::new);
		ScreenManager.register(ContainerInit.SHIP_CONSTRUCT.get(), ShipConstructScreen::new);
		ScreenManager.register(ContainerInit.SHIP_LAUNCH.get(), ShipLaunchScreen::new);
		ScreenManager.register(ContainerInit.COMPONENT_ANALYZER.get(), ComponentAnalyzerScreen::new);
		ScreenManager.register(ContainerInit.BATTERY.get(), BatteryScreen::new);
		ScreenManager.register(ContainerInit.TANK.get(), TankScreen::new);
		ScreenManager.register(ContainerInit.SCANNER.get(), ScannerScreen::new);
		ScreenManager.register(ContainerInit.PUZZLE.get(), PuzzleScreen::new);
		ScreenManager.register(ContainerInit.ATMOSPHERIC_SEPARATOR.get(), AtmosphericSeparatorScreen::new);
		ScreenManager.register(ContainerInit.TEMPERATURE_REGULATOR.get(), TemperatureRegulatorScreen::new);
		ScreenManager.register(ContainerInit.FUEL_STORAGE_UNIT.get(), FuelStorageUnitScreen::new);
		ScreenManager.register(ContainerInit.FURNACE_GENERATOR.get(), FurnaceGeneratorScreen::new);
		ScreenManager.register(ContainerInit.STATE_CONVERTER.get(), StateConverterScreen::new);
		ScreenManager.register(ContainerInit.HABER.get(), HaberScreen::new);
		ScreenManager.register(ContainerInit.MIXER.get(), MixerScreen::new);
		
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.CARGO_CRATE.get(), CargoCrateRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.COMPONENT_ANALYZER.get(), ComponentAnalyzerRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.SHIP_CONSOLE.get(), ShipConsoleRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.CUSTOM_MODEL.get(), CustomModelRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.TANK.get(), TankRenderer::new);
		
		event.enqueueWork(() -> {
			ItemModelsProperties.register(ItemInit.SHIP_COMPONENT.get(), new MachinaRL("type"), (stack, level, living) -> {
				return (int) ShipComponentItem.getType(stack).nbtID;
			});
		});
		//@formatter:on
	}

	static final int defVal = 8947848;

	public static int getColorFromId(int id, int paletteId) {
		if (id != -1) {
			PlanetData data = ClientStarchart.getPlanetData(id);
			Color color = PlanetPaletteGenerator.getPalette(data.getAttribute(AttributeInit.PALETTE))[paletteId];
			return color.getRGB();
		}
		return defVal;
	}

	// TODO: Find why this function breaks in so many different ways.
	public static IBlockColor getBlockColor(int paletteId) {

		return (state, reader, pos, num) -> {

			World world = null;

			if (reader instanceof World)
				world = (World) reader;

			if (reader instanceof ChunkRenderCache)
				world = ((ChunkRenderCache) reader).level;

			if (world != null && world.equals(Minecraft.getInstance().level)) {
				if (ITinted.class.isInstance(state.getBlock())) {
					if (!world.isLoaded(pos))
						return defVal;
					try {
						TileEntity te = world.getBlockEntity(pos);
						if (te != null && te instanceof TintedTileEntity) {
							int id = ((TintedTileEntity) te).id;
							if (id != -1) {
								return getColorFromId(id, paletteId);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						return defVal;
					}

				}

				RegistryKey<World> dim = world.dimension();
				if (PlanetHelper.isDimensionPlanet(dim)) {
					return getColorFromId(PlanetHelper.getId(dim), paletteId);
				}
			}

			return defVal;
		};
	}

	@SubscribeEvent
	public static void registerBlockColorsEvent(ColorHandlerEvent.Block event) {
		BlockColors col = event.getBlockColors();

		BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			if (ITinted.class.isInstance(block)) {
				int index = ((ITinted) block).getTintIndex();
				col.register(getBlockColor(index), block);
			}
		});
	}

	@SubscribeEvent
	public static void registerItemColorsEvent(ColorHandlerEvent.Item event) {
		ItemColors col = event.getItemColors();

		Registry.ITEM.forEach(item -> {
			if (item instanceof TintedItem) {
				col.register((stack, index) -> {
					ITinted block = (ITinted) ((TintedItem) item).getBlock();
					int id = TintedItem.getFromStack(stack);
					return getColorFromId(id, block.getTintIndex());
				}, item);
			}
		});
	}

	@SubscribeEvent
	public static void textureStitch(TextureStitchEvent.Pre event) {
		if (event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
			for (OreType ore : Arrays.asList(OreType.values())) {
				event.addSprite(ore.getTexturePath());
			}
		}
	}

	@SubscribeEvent
	public static void onModelRegistryEvent(ModelRegistryEvent event) {
		ModelLoaderRegistry.registerLoader(OreModelLoader.ID, new OreModelLoader());
	}
}
