package com.cy4.machina.datagen.client;

import com.cy4.machina.Machina;
import com.cy4.machina.init.FluidInit;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;

import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesProvider extends BlockStateProvider {

	public BlockStatesProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Machina.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		fluid(FluidInit.LIQUID_HYDROGEN_BLOCK);
	}
	
	public void fluid(Block block) {
		getVariantBuilder(block).partialState().modelForState().modelFile(models().cubeAll(name(block), FluidInit.STILL_RL)).addModel();
	}
	
    private String name(Block block) {
        return block.getRegistryName().getPath();
    }

}
