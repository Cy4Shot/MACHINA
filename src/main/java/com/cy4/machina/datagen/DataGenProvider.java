package com.cy4.machina.datagen;

import com.cy4.machina.Machina;
import com.cy4.machina.datagen.client.ItemModelProvider;
import com.cy4.machina.datagen.client.lang.EnUsLangProvider;
import com.cy4.machina.datagen.common.PlanetTraitPoolsProvider;

import net.minecraft.data.DataGenerator;

import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.MOD)
public class DataGenProvider {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		gen.addProvider(new EnUsLangProvider(gen));
		
		gen.addProvider(new ItemModelProvider(gen, existingFileHelper));
		
		gen.addProvider(new PlanetTraitPoolsProvider(gen));
	}

}
