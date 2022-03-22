package com.machina.datagen.client;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.util.MachinaRL;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesProvider extends BlockStateProvider {

	public BlockStatesProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Machina.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleTintedBlock(BlockInit.ALIEN_STONE.get());
		simpleTintedBlock(BlockInit.TWILIGHT_DIRT.get());
	}

	public void fluid(Block block) {
		getVariantBuilder(block).partialState().modelForState()
				.modelFile(models().cubeAll(name(block), new ResourceLocation("block/water_still"))).addModel();
	}

	public void simpleTintedBlock(Block block) {
		simpleBlock(block, tinted(block));
	}

	public ModelFile tinted(Block block) {
		return models().singleTexture(name(block), new MachinaRL("block/tinted_cube_all"), "all", blockTexture(block));
	}

	private String name(Block block) {
		return block.getRegistryName().getPath();
	}

}
