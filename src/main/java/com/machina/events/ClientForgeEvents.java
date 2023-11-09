package com.machina.events;

import com.machina.Machina;
import com.machina.api.starchart.StarchartGenerator;
import com.machina.client.screen.StarchartScreen;
import com.machina.registration.init.KeyBindingInit;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
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

		if (KeyBindingInit.starchartKey.isDown()) {
			mc.setScreen(new StarchartScreen(StarchartGenerator.gen(10, "Test")));
		}
	}
}
