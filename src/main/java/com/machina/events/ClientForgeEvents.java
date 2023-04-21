package com.machina.events;

import com.machina.Machina;
import com.machina.client.ClientStarchart;
import com.machina.client.screen.DevScreen;
import com.machina.client.screen.StarchartScreen;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.screen.overlay.ResearchToastOverlay;
import com.machina.client.shader.renderer.ScannerRenderer;
import com.machina.planet.PlanetData;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.KeyBindingsInit;
import com.machina.util.Color;
import com.machina.util.server.PlanetHelper;
import com.machina.world.gen.PlanetPaletteGenerator;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

	private static final Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public static void fogSetup(FogColors event) {
		RegistryKey<World> dim = mc.level.dimension();
		if (PlanetHelper.isDimensionPlanet(dim)) {
			PlanetData data = ClientStarchart.getPlanetData(PlanetHelper.getId(dim));
			Color color = PlanetPaletteGenerator.getPalette(data.getAttribute(AttributeInit.PALETTE))[4];
			Float density = data.getAttribute(AttributeInit.FOG_DENSITY);
			event.setRed(color.getRed() / 255f * density);
			event.setGreen(color.getGreen() / 255f * density);
			event.setBlue(color.getBlue() / 255f * density);
		}
	}
	
	// TODO: Config!
	@SubscribeEvent
	public static void fogDensity(FogDensity event) {
		RegistryKey<World> dim = mc.level.dimension();
		if (PlanetHelper.isDimensionPlanet(dim)) {
			event.setDensity(0F);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		if (mc.screen != null || mc.level == null)
			return;

		if (KeyBindingsInit.isKeyPressed(KeyBindingsInit.DEV_SCREEN) && Machina.isDevEnvironment()) {
			mc.setScreen(new DevScreen());
		}

		if (KeyBindingsInit.isKeyPressed(KeyBindingsInit.STARCHART)) {
			mc.setScreen(new StarchartScreen());
		}

		if (KeyBindingsInit.isKeyPressed(KeyBindingsInit.SCAN)) {
			ScannerRenderer.INSTANCE.ping(new Vector3f(mc.player.position()));
		}

		// Ugly hack so that music plays!
		if (PlanetHelper.isDimensionPlanet(mc.player.level.dimension()) && mc.getMusicManager().nextSongDelay > 1200) {
			mc.getMusicManager().nextSongDelay = 1200;
		}
	}

	@SubscribeEvent
	public static void drawTooltipEvent(RenderTooltipEvent.Color event) {
		Screen s = mc.screen;
		if (s instanceof NoJeiContainerScreen
				|| event.getStack().getItem().getRegistryName().getNamespace().equals(Machina.MOD_ID)) {
			event.setBackground(0xFF_202020);
			event.setBorderEnd(0xFF_1bcccc);
			event.setBorderStart(0xFF_00fefe);
		}
	}

	@SubscribeEvent
	public static void drawOverlayEvent(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ALL)
			return;
		MatrixStack stack = event.getMatrixStack();
		MainWindow window = event.getWindow();
		ResearchToastOverlay.render(stack, window);
	}
}
