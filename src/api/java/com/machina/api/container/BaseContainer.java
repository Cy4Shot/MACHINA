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

package com.machina.api.container;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.machina.api.container.tracker.IDataTracker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public abstract class BaseContainer extends Container {
	
	private final List<IDataTracker> trackers = new LinkedList<>();

	/**
	 * Define this in the constructor. Will be used by {@link #quickMoveStack}
	 */
	public int slotsNumber;
	/**
	 * Define this in the constructor. Will be used to render the inventory name
	 */
	public ITextComponent containerName;

	protected BaseContainer(ContainerType<?> pMenuType, int pContainerId) {
		super(pMenuType, pContainerId);
	}
	
	/**
	 * Adds a {@link IDataTracker} to the container
	 * @param tracker
	 */
	protected <T extends IDataTracker> T addTracker(T tracker) {
		trackers.add(tracker);
		tracker.addDataSlots(this::addDataSlot);
		return tracker;
	}

	/**
	 * Handles shift-clicking in the inventory, based on {@link #slotsNumber}
	 */
	@Override
	public ItemStack quickMoveStack(PlayerEntity pPlayer, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (!slot.isActive()) { return ItemStack.EMPTY; }
		if (slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if ((index < slotsNumber && !this.moveItemStackTo(stack1, slotsNumber, slots.size(), true))
					|| !this.moveItemStackTo(stack1, 0, slotsNumber, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return stack;
	}
	
	public static <T extends TileEntity> T getTileEntity(final PlayerInventory playerInv, final PacketBuffer data, Class<T> tileClass) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null.");
		Objects.requireNonNull(data, "Packet Buffer cannot be null.");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if (tileClass.isInstance(te)) {
			return tileClass.cast(te);
		}
		throw new IllegalStateException("Tile Entity Is Not Correct");
	}

}
