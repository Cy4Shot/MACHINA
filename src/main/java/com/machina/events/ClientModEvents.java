package com.machina.events;

import org.joml.Vector3f;

import com.machina.Machina;
import com.machina.api.client.ClientTimer;
import com.machina.api.client.cinema.CinematicHandler;
import com.machina.api.client.cinema.effect.renderer.CinematicTextOverlay;
import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.api.client.shader.ShaderHandler;
import com.machina.api.util.reflect.ClassHelper;
import com.machina.client.PlanetSpecialEffects;
import com.machina.client.ber.RocketPartBenchRenderer;
import com.machina.client.ber.TankRenderer;
import com.machina.client.entity.RocketRenderer;
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
import com.machina.client.screen.menu.GrinderScreen;
import com.machina.client.screen.menu.MachineCaseScreen;
import com.machina.client.screen.menu.MelterScreen;
import com.machina.client.screen.menu.ReactionChamberScreen;
import com.machina.client.screen.menu.RocketAssemblyStationScreen;
import com.machina.client.screen.menu.RocketPartBenchScreen;
import com.machina.client.screen.menu.SawmillScreen;
import com.machina.client.screen.menu.SolidifierScreen;
import com.machina.client.screen.menu.TankScreen;
import com.machina.client.screen.menu.connector.FluidPipeScreen;
import com.machina.client.screen.menu.connector.ItemConduitScreen;
import com.machina.client.screen.menu.entity.RocketScreen;
import com.machina.client.screen.menu.item.AdvancedItemFilterScreen;
import com.machina.client.screen.menu.item.FluidFilterScreen;
import com.machina.client.screen.menu.item.ItemFilterScreen;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.KeyBindingInit;
import com.machina.registration.init.MenuTypeInit;
import com.machina.world.PlanetFactory;

import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientTimer.setup();
        CinematicHandler.setup();

        FluidInit.setRenderLayers();

        event.enqueueWork(() -> {
            MenuScreens.register(MenuTypeInit.ROCKET.get(), RocketScreen::new);
            MenuScreens.register(MenuTypeInit.FLUID_PIPE.get(), FluidPipeScreen::new);
            MenuScreens.register(MenuTypeInit.ITEM_CONDUIT.get(), ItemConduitScreen::new);
            MenuScreens.register(MenuTypeInit.FLUID_FILTER.get(), FluidFilterScreen::new);
            MenuScreens.register(MenuTypeInit.ITEM_FILTER.get(), ItemFilterScreen::new);
            MenuScreens.register(MenuTypeInit.ADVANCED_ITEM_FILTER.get(), AdvancedItemFilterScreen::new);
            MenuScreens.register(MenuTypeInit.BATTERY.get(), BatteryScreen::new);
            MenuScreens.register(MenuTypeInit.TANK.get(), TankScreen::new);
            MenuScreens.register(MenuTypeInit.CREATIVE_BATTERY.get(), CreativeBatteryScreen::new);
            MenuScreens.register(MenuTypeInit.MACHINE_CASE.get(), MachineCaseScreen::new);
            MenuScreens.register(MenuTypeInit.FURNACE_GENERATOR.get(), FurnaceGeneratorScreen::new);
            MenuScreens.register(MenuTypeInit.CHEMICAL_GENERATOR.get(), ChemicalGeneratorScreen::new);
            MenuScreens.register(MenuTypeInit.ELECTRIC_SMELTER.get(), ElectricSmelterScreen::new);
            MenuScreens.register(MenuTypeInit.GRINDER.get(), GrinderScreen::new);
            MenuScreens.register(MenuTypeInit.COMPRESSOR.get(), CompressorScreen::new);
            MenuScreens.register(MenuTypeInit.SOLIDIFIER.get(), SolidifierScreen::new);
            MenuScreens.register(MenuTypeInit.MELTER.get(), MelterScreen::new);
            MenuScreens.register(MenuTypeInit.REACTION_CHAMBER.get(), ReactionChamberScreen::new);
            MenuScreens.register(MenuTypeInit.COMPOSTER_VAT.get(), ComposterVatScreen::new);
            MenuScreens.register(MenuTypeInit.SAWMILL.get(), SawmillScreen::new);
            MenuScreens.register(MenuTypeInit.ELECTROLYZER.get(), ElectrolyzerScreen::new);
            MenuScreens.register(MenuTypeInit.ELECTRIC_PUMP.get(), ElectricPumpScreen::new);
            MenuScreens.register(MenuTypeInit.ATMOSPHERIC_SEPARATOR.get(), AtmosphericSeparatorScreen::new);
            MenuScreens.register(MenuTypeInit.ROCKET_PART_BENCH.get(), RocketPartBenchScreen::new);
            MenuScreens.register(MenuTypeInit.ROCKET_ASSEMBLY_STATION.get(), RocketAssemblyStationScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerRenderers(RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.TANK.get(), TankRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.ROCKET_PART_BENCH.get(), RocketPartBenchRenderer::new);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        ClassHelper.<KeyMapping>doWithStatics(KeyBindingInit.class, (name, map) -> event.register(map));
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("cinematic_overlay",
                (gui, graphics, partialTick, width, height) -> CinematicTextureOverlay.renderOverlay());
        event.registerAboveAll("cinematic_title", (gui, graphics, partialTick, width, height) -> CinematicTextOverlay
                .renderOverlay(graphics, graphics.pose(), width, height));
    }

    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        ItemColors colors = event.getItemColors();

        for (FluidObject obj : FluidInit.OBJS) {
            colors.register((stack, index) -> index == 1 ? obj.chem() : -1, obj.bucket());
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
            }, obj.fluid().getFluidType());
        });
    }
}