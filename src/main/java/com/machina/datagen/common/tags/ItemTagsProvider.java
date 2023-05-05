package com.machina.datagen.common.tags;

import com.machina.Machina;
import com.machina.planet.OreType;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TagInit;
import com.machina.util.helper.TagHelper;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagsProvider extends net.minecraft.data.ItemTagsProvider {

	public ItemTagsProvider(DataGenerator generator, BlockTagsProvider blocks, ExistingFileHelper helper) {
		super(generator, blocks, Machina.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		tag(ItemTags.SLABS).add(BlockInit.WASTELAND_SANDSTONE_SLAB.get().asItem(),
				BlockInit.ALIEN_STONE_SLAB.get().asItem(), BlockInit.TWILIGHT_DIRT_SLAB.get().asItem(),
				BlockInit.WASTELAND_DIRT_SLAB.get().asItem());
		tag(ItemTags.STAIRS).add(BlockInit.WASTELAND_SANDSTONE_STAIRS.get().asItem(),
				BlockInit.ALIEN_STONE_STAIRS.get().asItem(), BlockInit.TWILIGHT_DIRT_STAIRS.get().asItem(),
				BlockInit.WASTELAND_DIRT_STAIRS.get().asItem());
		tag(ItemTags.WALLS).add(BlockInit.WASTELAND_SANDSTONE_WALL.get().asItem());
		tag(ItemTags.SAND).add(BlockInit.WASTELAND_SAND.get().asItem());
		tag(TagHelper.getForgeItemTag("ingots")).add(ItemInit.LOW_GRADE_STEEL_INGOT.get(),
				ItemInit.ALUMINUM_INGOT.get(), ItemInit.COPPER_INGOT.get());
		tag(TagHelper.getForgeItemTag("nuggets")).add(ItemInit.LOW_GRADE_STEEL_NUGGET.get(),
				ItemInit.ALUMINUM_NUGGET.get(), ItemInit.COPPER_NUGGET.get());
		tag(TagHelper.getForgeItemTag("storage_blocks")).add(BlockInit.LOW_GRADE_STEEL_BLOCK.get().asItem(),
				BlockInit.ALUMINUM_BLOCK.get().asItem(), BlockInit.COPPER_BLOCK.get().asItem());
		tag(TagHelper.getForgeItemTag("ores")).add(BlockInit.ALUMINUM_ORE.get().asItem(),
				BlockInit.COPPER_ORE.get().asItem());
		tag(TagHelper.getForgeItemTag("ingots/steel")).add(ItemInit.LOW_GRADE_STEEL_INGOT.get());
		tag(TagHelper.getForgeItemTag("nuggets/steel")).add(ItemInit.LOW_GRADE_STEEL_NUGGET.get());
		tag(TagHelper.getForgeItemTag("storage_blocks/steel")).add(BlockInit.LOW_GRADE_STEEL_BLOCK.get().asItem());
		tag(TagHelper.getForgeItemTag("ingots/aluminum")).add(ItemInit.ALUMINUM_INGOT.get());
		tag(TagHelper.getForgeItemTag("nuggets/aluminum")).add(ItemInit.ALUMINUM_NUGGET.get());
		tag(TagHelper.getForgeItemTag("storage_blocks/aluminum")).add(BlockInit.ALUMINUM_BLOCK.get().asItem());
		tag(TagHelper.getForgeItemTag("ores/aluminum")).add(BlockInit.ALUMINUM_ORE.get().asItem());
		tag(TagHelper.getForgeItemTag("ingots/copper")).add(ItemInit.COPPER_INGOT.get());
		tag(TagHelper.getForgeItemTag("nuggets/copper")).add(ItemInit.COPPER_NUGGET.get());
		tag(TagHelper.getForgeItemTag("storage_blocks/copper")).add(BlockInit.COPPER_BLOCK.get().asItem());
		tag(TagHelper.getForgeItemTag("ores/copper")).add(BlockInit.COPPER_ORE.get().asItem());

		for (OreType type : OreType.values()) {
			tag(TagInit.Items.ORE_TAGS.get(type)).add(
					BlockInit.ORE_MAP.get(type).values().stream().map(obj -> obj.get().asItem()).toArray(Item[]::new));
		}
	}
}
