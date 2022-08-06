package com.machina.datagen.common.tags;

import static com.machina.Machina.MOD_ID;

import com.machina.registration.init.BlockInit;
import com.machina.registration.init.TagInit;
import com.machina.util.server.TagHelper;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider {

	public BlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
		super(pGenerator, MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(TagInit.Blocks.WRENCH_EFFECTIVE).add(BlockInit.SHIP_CONSOLE.get());
		tag(TagInit.Blocks.CARVEABLE_BLOCKS).add(BlockInit.ALIEN_STONE.get(), BlockInit.TWILIGHT_DIRT.get(),
				BlockInit.WASTELAND_DIRT.get(), BlockInit.WASTELAND_SAND.get());
		tag(BlockTags.SLABS).add(BlockInit.WASTELAND_SANDSTONE_SLAB.get(), BlockInit.ALIEN_STONE_SLAB.get(),
				BlockInit.TWILIGHT_DIRT_SLAB.get(), BlockInit.WASTELAND_DIRT_SLAB.get());
		tag(BlockTags.STAIRS).add(BlockInit.WASTELAND_SANDSTONE_STAIRS.get(), BlockInit.ALIEN_STONE_STAIRS.get(),
				BlockInit.TWILIGHT_DIRT_STAIRS.get(), BlockInit.WASTELAND_DIRT_STAIRS.get());
		tag(BlockTags.WALLS).add(BlockInit.WASTELAND_SANDSTONE_WALL.get());
		tag(BlockTags.SAND).add(BlockInit.WASTELAND_SAND.get());
		tag(TagHelper.getForgeBlockTag("storage_blocks")).add(BlockInit.STEEL_BLOCK.get(),
				BlockInit.ALUMINUM_BLOCK.get(), BlockInit.COPPER_BLOCK.get());
		tag(TagHelper.getForgeBlockTag("ores")).add(BlockInit.ALUMINUM_ORE.get(), BlockInit.COPPER_ORE.get());
		tag(TagHelper.getForgeBlockTag("storage_blocks/steel")).add(BlockInit.STEEL_BLOCK.get());
		tag(TagHelper.getForgeBlockTag("storage_blocks/aluminum")).add(BlockInit.ALUMINUM_BLOCK.get());
		tag(TagHelper.getForgeBlockTag("storage_blocks/copper")).add(BlockInit.COPPER_BLOCK.get());
		tag(TagHelper.getForgeBlockTag("ores/aluminum")).add(BlockInit.ALUMINUM_ORE.get());
		tag(TagHelper.getForgeBlockTag("ores/copper")).add(BlockInit.COPPER_ORE.get());
	}

}
