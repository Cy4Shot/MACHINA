package com.machina.datagen.client;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
import com.machina.util.MachinaRL;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

	public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Machina.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		makeSimpleBlockItem(BlockInit.SHIP_CONSOLE.get());
		makeSimpleBlockItem(BlockInit.COMPONENT_ANALYZER.get());
		makeSimpleBlockItem(BlockInit.ALIEN_STONE.get());
		makeSimpleBlockItem(BlockInit.ALIEN_STONE_SLAB.get());
		makeSimpleBlockItem(BlockInit.ALIEN_STONE_STAIRS.get());
		makeSimpleBlockItem(BlockInit.TWILIGHT_DIRT.get());
		makeSimpleBlockItem(BlockInit.TWILIGHT_DIRT_SLAB.get());
		makeSimpleBlockItem(BlockInit.TWILIGHT_DIRT_STAIRS.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_DIRT.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_DIRT_SLAB.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_DIRT_STAIRS.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_SAND.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_SANDSTONE.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_SANDSTONE_SLAB.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_SANDSTONE_STAIRS.get());
		makeSimpleBlockItem(BlockInit.WASTELAND_SANDSTONE_WALL.get());
		makeSimpleBlockItem(BlockInit.STEEL_BLOCK.get());
		makeSimpleBlockItem(BlockInit.STEEL_CHASSIS.get());
		makeSimpleBlockItem(BlockInit.IRON_CHASSIS.get());
		makeSimpleBlockItem(BlockInit.PUZZLE_BLOCK.get());
		makeSimpleBlockItem(BlockInit.BATTERY.get());
		makeSimpleBlockItem(BlockInit.CABLE.get());
		makeSimpleBlockItem(BlockInit.CREATIVE_BATTERY.get());
		makeSimpleBlockItem(BlockInit.REINFORCED_TILE.get());
		
		oneLayerItem(ItemInit.SHIP_COMPONENT.get());
		oneLayerItem(ItemInit.REINFORCED_STICK.get());
		oneLayerItem(ItemInit.STEEL_INGOT.get());
		oneLayerItem(ItemInit.STEEL_NUGGET.get());
		oneLayerItem(ItemInit.PROCESSOR.get());
		oneLayerItem(ItemInit.SILICON.get());
		oneLayerItem(ItemInit.SCANNER.get());
		oneLayerItem(ItemInit.TRANSISTOR.get());
	}

	protected void createBucketModel(FlowingFluid stillFluid) {
		DynamicBucketModelBuilder<ItemModelBuilder> builder = withExistingParent(
				stillFluid.getBucket().getRegistryName().getPath(), new ResourceLocation("forge", "item/bucket"))
						.customLoader(DynamicBucketModelBuilder::begin);
		if (stillFluid.getAttributes().isGaseous()) {
			builder.flipGas(true);
		}
		builder.fluid(stillFluid);
	}

	protected void makeSimpleBlockItem(Block block) {
		getBuilder(block.asItem().getRegistryName().toString())
				.parent(getExistingFile(new MachinaRL("block/" + block.asItem().getRegistryName().getPath())));
	}
	
	protected void oneLayerItem(Item item, ResourceLocation texture) {
		ResourceLocation itemTexture = new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath());
		if (existingFileHelper.exists(itemTexture, ResourcePackType.CLIENT_RESOURCES, ".png", "textures")) {
			getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/generated")))
					.texture("layer0", itemTexture);
		} else {
			System.out.println(
					"Texture for " + item.getRegistryName().toString() + " not present at " + itemTexture.toString());
		}
	}
	
	protected void oneLayerItem(Item item) {
		oneLayerItem(item, item.getRegistryName());
	}

}
