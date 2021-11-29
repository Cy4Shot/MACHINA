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

package com.machina.api.inventory;

import static net.minecraft.item.ItemStack.EMPTY;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IFluidInventory extends IInventory, IFluidHandler {

	@Override
	default void clearContent() {
	}

	@Override
	default int getContainerSize() { return 0; }

	@Override
	default ItemStack getItem(int pIndex) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isEmpty() { return true; }

	@Override
	default ItemStack removeItem(int pIndex, int pCount) {
		return EMPTY;
	}

	@Override
	default ItemStack removeItemNoUpdate(int pIndex) {
		return EMPTY;
	}

	@Override
	default void setChanged() {
	}

	@Override
	default void setItem(int pIndex, ItemStack pStack) {
	}

	@Override
	default boolean stillValid(PlayerEntity pPlayer) {
		return false;
	}

	@Override
	default boolean canPlaceItem(int pIndex, ItemStack pStack) {
		return false;
	}

	/**
	 * @return Current amount of fluid in the tank.
	 */
	default int getFluidAmount(int tank) {
		return getFluidInTank(tank).getAmount();
	}

	/**
	 * @return Remaining capacity in tank
	 */
	default int getRemainingCapacity(int tank) {
		return getTankCapacity(tank) - getFluidInTank(tank).getAmount();
	}

}
