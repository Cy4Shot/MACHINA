package com.machina.datagen.server;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import com.machina.Machina;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenItemTags extends ItemTagsProvider {
	public DatagenItemTags(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
			CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
		super(p_275343_, p_275729_, p_275322_, Machina.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {

	}
}