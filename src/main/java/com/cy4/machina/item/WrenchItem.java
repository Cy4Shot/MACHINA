/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.item;

import java.util.List;

import com.cy4.machina.init.TagInit;

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
