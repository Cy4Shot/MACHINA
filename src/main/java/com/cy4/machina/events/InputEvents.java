package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.api.client.ClientStarchartHolder;
import com.cy4.machina.client.gui.DevPlanetCreationScreen;
import com.cy4.machina.client.gui.StarchartScreen;
import com.cy4.machina.init.KeyBindingsInit;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {

	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen != null || mc.level == null) {
			return;
		}

		if (KeyBindingsInit.isKeyDown(KeyBindingsInit.DEV_PLANET_CREATION_SCREEN)) {
			mc.setScreen(new DevPlanetCreationScreen());
		}

		if (mc.options.keyChat.isDown()) {
			mc.setScreen(new StarchartScreen());
//			ClientStarchartHolder.getStarchart().debugStarchart();
		}
	}

}
