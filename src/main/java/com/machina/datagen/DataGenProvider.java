package com.machina.datagen;

import com.machina.Machina;
import com.machina.datagen.client.BlockStatesProvider;
import com.machina.datagen.client.ItemModelProvider;
import com.machina.datagen.client.lang.EnUsLangProvider;
import com.machina.datagen.common.BlockTagsProvider;
import com.machina.datagen.common.ItemTagsProvider;
import com.machina.datagen.common.LootTableProvider;
import com.machina.datagen.common.PlanetTraitPoolsProvider;
import com.machina.datagen.common.RecipesProvider;

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

		// Client
		gen.addProvider(new EnUsLangProvider(gen));
		gen.addProvider(new BlockStatesProvider(gen, existingFileHelper));
		gen.addProvider(new ItemModelProvider(gen, existingFileHelper));

		// Server
		BlockTagsProvider blockTags = new BlockTagsProvider(gen, existingFileHelper);
		gen.addProvider(blockTags);
		gen.addProvider(new ItemTagsProvider(gen, blockTags, existingFileHelper));
		gen.addProvider(new PlanetTraitPoolsProvider(gen));
		gen.addProvider(new LootTableProvider(gen));
		gen.addProvider(new RecipesProvider(gen));
	}

}
