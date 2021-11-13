package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.client.gui.DevPlanetCreationScreen;
import com.cy4.machina.client.gui.StarchartScreen;
import com.cy4.machina.init.KeyBindingsInit;
import com.cy4.machina.world.data.StarchartData;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {
	
	@SubscribeEvent
	public static void onKeyPress(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		onInput(mc, event.getKey(), event.getAction());
	}

	@SubscribeEvent
	public static void onMouseClick(InputEvent.MouseInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		onInput(mc, event.getButton(), event.getAction());
	}
	
	@SuppressWarnings("resource")
	private static void onInput(Minecraft mc, int key, int action) {
		if (KeyBindingsInit.isKeyDown(KeyBindingsInit.DEV_PLANET_CREATION_SCREEN)) {
			mc.setScreen(new DevPlanetCreationScreen());
		}
		
		if (mc.options.keyDrop.isDown()) {
			mc.setScreen(new StarchartScreen(StarchartData.getDefaultInstance(ServerLifecycleHooks.getCurrentServer()).starchart));
		}
	}

}
