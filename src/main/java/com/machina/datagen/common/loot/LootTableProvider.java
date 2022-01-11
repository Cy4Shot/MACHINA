package com.machina.datagen.common.loot;

import com.machina.init.BlockInit;

import net.minecraft.data.DataGenerator;

public class LootTableProvider extends BaseLootTableProvider {

    public LootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        dropSelf(BlockInit.TWILIGHT_DIRT);
        dropSelf(BlockInit.ALIEN_STONE);
    }
}