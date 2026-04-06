package com.machina.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.machina.Machina;
import com.machina.datagen.client.DatagenBlockStates;
import com.machina.datagen.client.DatagenItemModels;
import com.machina.datagen.client.DatagenParticleDescriptions;
import com.machina.datagen.client.lang.DatagenLangEnUs;
import com.machina.datagen.server.DatagenBlockTags;
import com.machina.datagen.server.DatagenDatamaps;
import com.machina.datagen.server.DatagenFluidTags;
import com.machina.datagen.server.DatagenItemTags;
import com.machina.datagen.server.DatagenLootTables;
import com.machina.datagen.server.DatagenRecipes;
import com.machina.datagen.server.DatagenDatapack;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Machina.MOD_ID)
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
		gen.addProvider(event.includeClient(), new DatagenParticleDescriptions(po, files));

		// Server
		DatagenBlockTags blocks = gen.addProvider(event.includeServer(), new DatagenBlockTags(po, lookup, files));
		gen.addProvider(event.includeServer(), new DatagenItemTags(po, lookup, blocks.contentsGetter(), files));
		gen.addProvider(event.includeServer(), new DatagenFluidTags(po, lookup, files));
		gen.addProvider(event.includeServer(), new LootTableProvider(po, Set.of(),
				List.of(new SubProviderEntry(DatagenLootTables::new, LootContextParamSets.BLOCK)), lookup));
		gen.addProvider(event.includeServer(), new DatagenRecipes(po, lookup));
		gen.addProvider(event.includeServer(), new DatagenDatamaps(po, lookup));
		gen.addProvider(event.includeServer(), new DatagenDatapack(po, lookup));
	}
}