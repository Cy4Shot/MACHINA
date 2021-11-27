/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.item;

import java.util.List;

import com.machina.init.TagInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class WrenchItem extends Item {

	public WrenchItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext pContext) {
		if (pContext.getLevel().isClientSide()) { return super.useOn(pContext); }
		BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos());
		BlockPos pos = pContext.getClickedPos();
		if (state.is(TagInit.Blocks.WRENCH_EFFECTIVE)) {
			List<ItemStack> drops = state.getDrops(new LootContext.Builder((ServerWorld) pContext.getLevel())
					.withParameter(LootParameters.TOOL, ItemStack.EMPTY)
					.withParameter(LootParameters.ORIGIN, new Vector3d(pos.getX(), pos.getY(), pos.getZ())));
			drops.forEach(stack -> {
				ItemEntity item = new ItemEntity(pContext.getLevel(), pContext.getClickedPos().getX(),
						pContext.getClickedPos().getY(), pContext.getClickedPos().getZ(), stack);
				pContext.getLevel().addFreshEntity(item);
			});
			pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), Blocks.AIR.defaultBlockState());
		}
		return super.useOn(pContext);
	}

}
