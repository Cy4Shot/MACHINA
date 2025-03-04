package com.machina.events;

import com.machina.Machina;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.starchart.StarchartGenerator;
import com.machina.client.screen.StarchartScreen;
import com.machina.registration.init.KeyBindingInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

	private static final Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public static void onClientTick(final ClientTickEvent event) {
		if (mc.screen != null || mc.level == null)
			return;

		if (KeyBindingInit.STARCHART_KEY.isDown()) {
			mc.setScreen(new StarchartScreen(StarchartGenerator.gen(10, "Test")));
		}
	}

	@SubscribeEvent
	public static void tooltipCol(final RenderTooltipEvent.Color event) {
		Screen s = mc.screen;
		if (s instanceof MachinaMenuScreen<?>) {
			event.setBackground(0xFF_202020);
			event.setBorderEnd(0xFF_1bcccc);
			event.setBorderStart(0xFF_00fefe);
		}
	}
}
