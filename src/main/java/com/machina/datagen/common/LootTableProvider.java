package com.machina.datagen.common;

import com.machina.datagen.BaseLootTableProvider;
import com.machina.registration.init.BlockInit;

import net.minecraft.data.DataGenerator;

public class LootTableProvider extends BaseLootTableProvider {

    public LootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        dropSelf(BlockInit.TWILIGHT_DIRT.get());
        dropSelf(BlockInit.ALIEN_STONE.get());
        dropSelf(BlockInit.SHIP_CONSOLE.get());
    }
}