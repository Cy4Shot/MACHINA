package com.cy4.machina.datagen.client;

import com.cy4.machina.Machina;
import com.cy4.machina.init.FluidInit;

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
		createBucketModel(FluidInit.OXYGEN.get());
		createBucketModel(FluidInit.HYDROGEN.get());
		createBucketModel(FluidInit.LIQUID_HYDROGEN.get());
	}

	protected void createBucketModel(FlowingFluid stillFluid) {
		DynamicBucketModelBuilder<ItemModelBuilder> builder = withExistingParent(stillFluid.getBucket().getRegistryName().getPath(), new ResourceLocation("forge", "item/bucket"))
				.customLoader(DynamicBucketModelBuilder::begin);
		builder.fluid(stillFluid);
	}

}
