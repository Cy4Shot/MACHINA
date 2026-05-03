package com.machina.events;

import org.joml.Vector3f;

import com.machina.Machina;
import com.machina.api.client.cinema.CinematicHandler;
import com.machina.api.client.cinema.effect.renderer.CinematicTextOverlay;
import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.api.client.shader.ShaderHandler;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.reflect.ClassHelper;
import com.machina.client.PlanetSpecialEffects;
import com.machina.client.ber.RocketPartBenchRenderer;
import com.machina.client.ber.TankRenderer;
import com.machina.client.entity.RocketRenderer;
import com.machina.client.model.rocket.RocketPartModels;
import com.machina.client.model.rocket.part.AdvancedChassisModel;
import com.machina.client.model.rocket.part.BreakerShieldModel;
import com.machina.client.model.rocket.part.PressurizedTankModel;
import com.machina.client.model.rocket.part.ReinforcedLifeSupportModel;
import com.machina.client.model.rocket.part.SimpleChassisModel;
import com.machina.client.model.rocket.part.SimpleFuelTankModel;
import com.machina.client.model.rocket.part.SimpleLifeSupportModel;
import com.machina.client.model.rocket.part.SimpleShieldModel;
import com.machina.client.model.rocket.part.SimpleThrusterModel;
import com.machina.client.model.rocket.part.TriTallThrusterModel;
import com.machina.client.particle.DustStormParticle.DustStormParticleProvider;
import com.machina.client.screen.menu.AtmosphericSeparatorScreen;
import com.machina.client.screen.menu.BatteryScreen;
import com.machina.client.screen.menu.ChemicalGeneratorScreen;
import com.machina.client.screen.menu.ComposterVatScreen;
import com.machina.client.screen.menu.CompressorScreen;
import com.machina.client.screen.menu.CreativeBatteryScreen;
import com.machina.client.screen.menu.ElectricPumpScreen;
import com.machina.client.screen.menu.ElectricSmelterScreen;
import com.machina.client.screen.menu.ElectrolyzerScreen;
import com.machina.client.screen.menu.FurnaceGeneratorScreen;
import com.machina.client.screen.menu.GeothermalGeneratorScreen;
import com.machina.client.screen.menu.GrinderScreen;
import com.machina.client.screen.menu.MelterScreen;
import com.machina.client.screen.menu.ReactionChamberScreen;
import com.machina.client.screen.menu.RocketAssemblyStationScreen;
import com.machina.client.screen.menu.RocketPartBenchScreen;
import com.machina.client.screen.menu.RocketRefuelingStationScreen;
import com.machina.client.screen.menu.SawmillScreen;
import com.machina.client.screen.menu.SolidifierScreen;
import com.machina.client.screen.menu.TankScreen;
import com.machina.client.screen.menu.connector.FluidPipeScreen;
import com.machina.client.screen.menu.connector.ItemConduitScreen;
import com.machina.client.screen.menu.entity.RocketScreen;
import com.machina.client.screen.menu.item.AdvancedItemFilterScreen;
import com.machina.client.screen.menu.item.FluidFilterScreen;
import com.machina.client.screen.menu.item.ItemFilterScreen;
import com.machina.client.weather.AuroraWeatherRenderer;
import com.machina.client.weather.RainWeatherRenderer;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.KeyBindingInit;
import com.machina.registration.init.MenuTypeInit;
import com.machina.registration.init.ParticleTypeInit;
import com.machina.registration.init.RocketPartInit;
import com.machina.registration.init.WeatherEventInit;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.world.PlanetFactory;

import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		CinematicHandler.setup();
		FluidInit.setRenderLayers();

		RocketPartModels.register(RocketPartInit.SIMPLE_CHASSIS.get(), SimpleChassisModel::new);
		RocketPartModels.register(RocketPartInit.ADVANCED_CHASSIS.get(), AdvancedChassisModel::new);

		RocketPartModels.register(RocketPartInit.SIMPLE_FUEL_TANK.get(), SimpleFuelTankModel::new);
		RocketPartModels.register(RocketPartInit.PRESSURIZED_FUEL_TANK.get(), PressurizedTankModel::new);

		RocketPartModels.register(RocketPartInit.SIMPLE_LIFE_SUPPORT.get(), SimpleLifeSupportModel::new);
		RocketPartModels.register(RocketPartInit.REINFORCED_LIFE_SUPPORT.get(), ReinforcedLifeSupportModel::new);

		RocketPartModels.register(RocketPartInit.SIMPLE_SHIELD.get(), SimpleShieldModel::new);
		RocketPartModels.register(RocketPartInit.BREAKER_SHIELD.get(), BreakerShieldModel::new);

		RocketPartModels.register(RocketPartInit.SIMPLE_THRUSTER.get(), SimpleThrusterModel::new);
		RocketPartModels.register(RocketPartInit.TRI_TALL_THRUSTER.get(), TriTallThrusterModel::new);

		ClientWeatherManager.registerRenderer(WeatherEventInit.RAIN, RainWeatherRenderer::new);
		ClientWeatherManager.registerRenderer(WeatherEventInit.AURORA, AuroraWeatherRenderer::new);
	}

	@SubscribeEvent // on the mod event bus only on the physical client
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(MenuTypeInit.ROCKET.get(), RocketScreen::new);
		event.register(MenuTypeInit.FLUID_PIPE.get(), FluidPipeScreen::new);
		event.register(MenuTypeInit.ITEM_CONDUIT.get(), ItemConduitScreen::new);
		event.register(MenuTypeInit.FLUID_FILTER.get(), FluidFilterScreen::new);
		event.register(MenuTypeInit.ITEM_FILTER.get(), ItemFilterScreen::new);
		event.register(MenuTypeInit.ADVANCED_ITEM_FILTER.get(), AdvancedItemFilterScreen::new);
		event.register(MenuTypeInit.BATTERY.get(), BatteryScreen::new);
		event.register(MenuTypeInit.TANK.get(), TankScreen::new);
		event.register(MenuTypeInit.CREATIVE_BATTERY.get(), CreativeBatteryScreen::new);
		event.register(MenuTypeInit.FURNACE_GENERATOR.get(), FurnaceGeneratorScreen::new);
		event.register(MenuTypeInit.CHEMICAL_GENERATOR.get(), ChemicalGeneratorScreen::new);
		event.register(MenuTypeInit.ELECTRIC_SMELTER.get(), ElectricSmelterScreen::new);
		event.register(MenuTypeInit.GRINDER.get(), GrinderScreen::new);
		event.register(MenuTypeInit.COMPRESSOR.get(), CompressorScreen::new);
		event.register(MenuTypeInit.SOLIDIFIER.get(), SolidifierScreen::new);
		event.register(MenuTypeInit.MELTER.get(), MelterScreen::new);
		event.register(MenuTypeInit.REACTION_CHAMBER.get(), ReactionChamberScreen::new);
		event.register(MenuTypeInit.COMPOSTER_VAT.get(), ComposterVatScreen::new);
		event.register(MenuTypeInit.SAWMILL.get(), SawmillScreen::new);
		event.register(MenuTypeInit.ELECTROLYZER.get(), ElectrolyzerScreen::new);
		event.register(MenuTypeInit.ELECTRIC_PUMP.get(), ElectricPumpScreen::new);
		event.register(MenuTypeInit.ATMOSPHERIC_SEPARATOR.get(), AtmosphericSeparatorScreen::new);
		event.register(MenuTypeInit.ROCKET_PART_BENCH.get(), RocketPartBenchScreen::new);
		event.register(MenuTypeInit.ROCKET_ASSEMBLY_STATION.get(), RocketAssemblyStationScreen::new);
		event.register(MenuTypeInit.ROCKET_REFUELING_STATION.get(), RocketRefuelingStationScreen::new);
		event.register(MenuTypeInit.GEOTHERMAL_GENERATOR.get(), GeothermalGeneratorScreen::new);
	}

	@SubscribeEvent
	public static void registerRenderers(RegisterRenderers event) {
		event.registerBlockEntityRenderer(BlockEntityInit.TANK.get(), TankRenderer::new);
		event.registerBlockEntityRenderer(BlockEntityInit.ROCKET_PART_BENCH.get(), RocketPartBenchRenderer::new);
	}
	
	@SubscribeEvent
	public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
	    event.registerSpriteSet(ParticleTypeInit.DUST_STORM.get(), DustStormParticleProvider::new);
	}

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		ClassHelper.<KeyMapping>doWithStatics(KeyBindingInit.class, (name, map) -> event.register(map));
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
		event.registerAboveAll(MachinaRL.create("cinematic_overlay"),
				(gui, delta) -> CinematicTextureOverlay.renderOverlay());
		event.registerAboveAll(MachinaRL.create("cinematic_title"),
				(gui, delta) -> CinematicTextOverlay.renderOverlay(gui));
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void itemColors(RegisterColorHandlersEvent.Item event) {
		ItemColors colors = event.getItemColors();

		for (FluidObject obj : FluidInit.OBJS) {
			colors.register((stack, index) -> index == 1 ? obj.chem().getColor() : -1, obj.bucket());
		}
	}

	@SubscribeEvent
	public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
		event.register(PlanetFactory.TYPE_KEY.location(), new PlanetSpecialEffects());
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BlockEntityInit.SIGN.get(), SignRenderer::new);
		event.registerBlockEntityRenderer(BlockEntityInit.HANGING_SIGN.get(), HangingSignRenderer::new);
		event.registerEntityRenderer(EntityTypeInit.ROCKET.get(), RocketRenderer::new);
	}

	@SubscribeEvent
	public static void registerShaders(RegisterShadersEvent event) {
		ShaderHandler.register(event::registerShader, event.getResourceProvider());
	}

	@SubscribeEvent
	static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
		FluidInit.OBJS.forEach(obj -> {
			event.registerFluidType(new IClientFluidTypeExtensions() {
				private static final ResourceLocation UNDERWATER_LOCATION = ResourceLocation
						.withDefaultNamespace("textures/misc/underwater.png");
				private static final ResourceLocation WATER_STILL = ResourceLocation
						.withDefaultNamespace("block/water_still");
				private static final ResourceLocation WATER_FLOW = ResourceLocation
						.withDefaultNamespace("block/water_flow");
				private static final ResourceLocation WATER_OVERLAY = ResourceLocation
						.withDefaultNamespace("block/water_overlay");

				@Override
				public ResourceLocation getStillTexture() {
					return WATER_STILL;
				}

				@Override
				public ResourceLocation getFlowingTexture() {
					return WATER_FLOW;
				}

				@Override
				public ResourceLocation getOverlayTexture() {
					return WATER_OVERLAY;
				}

				@Override
				public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
					return UNDERWATER_LOCATION;
				}

				@Override
				public int getTintColor() {
					return obj.chem().getColor();
				}

				@Override
				public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance,
						float darkenWorldAmount, Vector3f fluidFogColor) {
					int color = getTintColor();
					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = (color) & 0xFF;
					return new Vector3f((float) r / 255, (float) g / 255, (float) b / 255);
				}
			}, obj.type());
		});
	}
}
