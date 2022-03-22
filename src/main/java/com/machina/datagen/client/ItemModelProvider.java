package com.machina.datagen.client;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.util.MachinaRL;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.FlowingFluid;
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
		makeSimpleBlockItem(BlockInit.ALIEN_STONE.get());
		makeSimpleBlockItem(BlockInit.TWILIGHT_DIRT.get());
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

}
