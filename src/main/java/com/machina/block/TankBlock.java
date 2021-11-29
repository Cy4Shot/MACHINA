/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.block;

import java.util.concurrent.atomic.AtomicReference;

import com.machina.init.TileEntityTypesInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TankBlock extends Block {

	public TankBlock(Properties p_i48440_1_) {
		super(p_i48440_1_);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypesInit.TANK_TILE_ENTITY_TYPE.create();
	}

	@Override
	public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity player, Hand hand,
			BlockRayTraceResult pHit) {
		if (!pLevel.getBlockEntity(pPos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()
				|| pLevel.isClientSide()) {
			return ActionResultType.PASS;
		}
		ItemStack stack = player.getItemInHand(hand);
		AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
		pLevel.getBlockEntity(pPos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
				.ifPresent(tileCap -> {
					stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(stackCap -> {
						for (int i = 0; i < stackCap.getTanks(); i++) {
							if (stackCap.getFluidInTank(i).isEmpty() && !tileCap.getFluidInTank(0).isEmpty()) {
								stackCap.fill(tileCap.drain(tileCap.getTankCapacity(0), FluidAction.EXECUTE),
										FluidAction.EXECUTE);
								continue;
							}

							if (tileCap.isFluidValid(0, stackCap.getFluidInTank(i))) {
								tileCap.fill(stackCap.drain(stackCap.getFluidInTank(i), FluidAction.EXECUTE),
										FluidAction.EXECUTE);
								result.set(ActionResultType.CONSUME);
								return;
							}
						}
					});
				});

		return result.get();
	}

}
