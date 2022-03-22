package com.machina.datagen.common;

import static com.machina.Machina.MOD_ID;

import com.machina.registration.init.BlockInit;
import com.machina.registration.init.TagInit;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider {

	public BlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
		super(pGenerator, MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(TagInit.Blocks.CARVEABLE_BLOCKS).add(BlockInit.ALIEN_STONE.get(), BlockInit.TWILIGHT_DIRT.get());
	}

}
