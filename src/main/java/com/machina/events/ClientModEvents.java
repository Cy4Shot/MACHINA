package com.machina.events;

import com.machina.Machina;
import com.machina.api.client.ClientTimer;
import com.machina.api.client.cinema.CinematicHandler;
import com.machina.api.client.cinema.effect.renderer.CinematicTextOverlay;
import com.machina.api.client.cinema.effect.renderer.CinematicTextureOverlay;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.client.color.item.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ClientTimer.setup();
		CinematicHandler.setup();

		FluidInit.setRenderLayers();
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent e) {
		e.registerAboveAll("cinematic_overlay",
				(gui, graphics, partialTick, width, height) -> CinematicTextureOverlay.renderOverlay());
		e.registerAboveAll("cinematic_title", (gui, graphics, partialTick, width, height) -> CinematicTextOverlay
				.renderOverlay(graphics, graphics.pose(), width, height));
	}

	@SubscribeEvent
	public static void itemColors(RegisterColorHandlersEvent.Item event) {
		ItemColors colors = event.getItemColors();

		for (FluidObject obj : FluidInit.OBJS) {
			colors.register((stack, index) -> index == 1 ? obj.chem().getColor() : -1, obj.bucket());
		}
	}
}