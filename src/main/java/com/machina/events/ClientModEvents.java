package com.machina.events;

import com.machina.Machina;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.client.color.item.ItemColors;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		FluidInit.setRenderLayers();
	}

	@SubscribeEvent
	public static void itemColors(RegisterColorHandlersEvent.Item event) {
		ItemColors colors = event.getItemColors();

		for (FluidObject obj : FluidInit.OBJS) {
			colors.register((stack, index) -> index == 1 ? obj.chem().getColor() : -1, obj.bucket());
		}
	}
}
