package com.machina.datagen;

import com.machina.Machina;
import com.machina.datagen.client.DatagenBlockStates;
import com.machina.datagen.client.DatagenItemModels;
import com.machina.datagen.client.lang.DatagenLangEnUs;
import com.machina.datagen.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

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

        // Server
        DatagenBlockTags blocks = gen.addProvider(event.includeServer(), new DatagenBlockTags(po, lookup, files));
        gen.addProvider(event.includeServer(), new DatagenItemTags(po, lookup, blocks.contentsGetter(), files));
        gen.addProvider(event.includeServer(), new DatagenFluidTags(po, lookup, files));
        gen.addProvider(event.includeServer(), new DatagenLootTables(po));
        gen.addProvider(event.includeServer(), new DatagenRecipes(po));
    }
}