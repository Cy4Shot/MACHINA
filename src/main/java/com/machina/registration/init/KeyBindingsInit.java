package com.machina.registration.init;

import java.awt.event.KeyEvent;

import com.machina.Machina;
import com.machina.util.reflection.ClassHelper;

import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public final class KeyBindingsInit {

	public static final KeyBinding DEV_SCREEN = create("dev_screen", KeyEvent.VK_C);
	public static final KeyBinding STARCHART = create("starchart", KeyEvent.VK_P);

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ClassHelper.<KeyBinding>doWithStatics(KeyBindingsInit.class,
				(name, data) -> ClientRegistry.registerKeyBinding(data));
	}

	private static KeyBinding create(String name, int key) {
		return new KeyBinding("key." + Machina.MOD_ID + "." + name, key, "key.category." + Machina.MOD_ID);
	}

	public static boolean isKeyDown(KeyBinding key) {
		return key != null && key.isDown();
	}
}
