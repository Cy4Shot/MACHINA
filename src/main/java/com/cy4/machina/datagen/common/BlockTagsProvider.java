/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.datagen.common;

import static com.cy4.machina.Machina.MOD_ID;

import com.cy4.machina.init.BlockInit;
import com.cy4.machina.init.TagInit;

import net.minecraft.data.DataGenerator;

import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider {

	public BlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
		super(pGenerator, MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(TagInit.Blocks.WRENCH_EFFECTIVE).add(BlockInit.CONSOLE).add(BlockInit.ROCKET).add(BlockInit.ROCKET_MOUNT);
	}

}
