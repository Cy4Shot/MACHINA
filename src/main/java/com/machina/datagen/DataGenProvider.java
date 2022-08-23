package com.machina.datagen;

import com.machina.Machina;
import com.machina.datagen.client.BlockStatesProvider;
import com.machina.datagen.client.ItemModelProvider;
import com.machina.datagen.client.anim.AnimationProvider;
import com.machina.datagen.client.lang.EnUsLangProvider;
import com.machina.datagen.common.TraitPoolsProvider;
import com.machina.datagen.common.loot.LootTableProvider;
import com.machina.datagen.common.recipe.RecipesProvider;
import com.machina.datagen.common.tags.BlockTagsProvider;
import com.machina.datagen.common.tags.FluidTagsProvider;
import com.machina.datagen.common.tags.ItemTagsProvider;

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
		gen.addProvider(new AnimationProvider(gen));

		// Server
		BlockTagsProvider blockTags = new BlockTagsProvider(gen, existingFileHelper);
		gen.addProvider(blockTags);
		gen.addProvider(new ItemTagsProvider(gen, blockTags, existingFileHelper));
		gen.addProvider(new FluidTagsProvider(gen, existingFileHelper));
		gen.addProvider(new TraitPoolsProvider(gen));
		gen.addProvider(new LootTableProvider(gen));
		gen.addProvider(new RecipesProvider(gen));
	}

}
