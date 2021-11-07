package com.cy4.machina.datagen;

import com.cy4.machina.Machina;
import com.cy4.machina.datagen.client.lang.EnUsLangProvider;

import net.minecraft.data.DataGenerator;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD)
public class DataGenProvider {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();

		gen.addProvider(new EnUsLangProvider(gen));
	}

}
