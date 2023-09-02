package com.machina.datagen;

import java.util.concurrent.CompletableFuture;

import com.machina.Machina;
import com.machina.datagen.client.DatagenBlockStates;
import com.machina.datagen.client.DatagenItemModels;
import com.machina.datagen.client.lang.DatagenLangEnUs;
import com.machina.datagen.server.DatagenBlockTags;
import com.machina.datagen.server.DatagenItemTags;
import com.machina.datagen.server.DatagenRecipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		PackOutput po = gen.getPackOutput();
		ExistingFileHelper files = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

		// Client
		gen.addProvider(event.includeClient(), new DatagenLangEnUs(po));
		gen.addProvider(event.includeClient(), new DatagenItemModels(po, files));
		gen.addProvider(event.includeClient(), new DatagenBlockStates(po, files));

		// Server
		DatagenBlockTags blocks = gen.addProvider(event.includeServer(), new DatagenBlockTags(po, lookup, files));
		gen.addProvider(event.includeServer(), new DatagenItemTags(po, lookup, blocks.contentsGetter(), files));
//		gen.addProvider(event.includeServer(), DatagenLootTables.create(po)); // OMEGA BUGGY :((
		gen.addProvider(event.includeClient(), new DatagenRecipes(po));
	}
}