package com.machina.datagen.client;

import com.machina.Machina;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class DatagenBlockStates extends BlockStateProvider {
	public DatagenBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Machina.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
	}

	@SuppressWarnings("unused")
	private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
		simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
	}
}