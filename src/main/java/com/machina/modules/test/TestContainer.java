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

package com.machina.modules.test;

import com.machina.api.container.BaseContainer;
import com.machina.api.container.tracker.EnergyTracker;
import com.machina.api.util.InventoryUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TestContainer extends BaseContainer {
	
	private final IWorldPosCallable canInteractWithCallable;
	public EnergyTracker energyTracker; 
	
	public IIntArray data;

	public TestContainer(final int windowId, final PlayerInventory playerInv, final TestTileEntity te) {
		super(TestModule.TEST_CONTAINER_TYPE, windowId);
		this.canInteractWithCallable = IWorldPosCallable.create(te.getLevel(), te.getBlockPos());
		InventoryUtils.createPlayerSlots(playerInv, 8, 84).forEach(this::addSlot);
		energyTracker = addTracker(new EnergyTracker(te.getEnergyStorage()));
		
		this.data = new IIntArray() {
			
			@Override
			public void set(int i, int pValue) {
				if (i == 0) {
					te.getEnergyStorage().receiveInternalEnergy(pValue, false);
				}
			}
			
			@Override
			public int getCount() { return 1; }
			
			@Override
			public int get(int i) {
				if (i == 0) {
					return te.getEnergyStorage().getEnergyStored();
				}
				return 0;
			}
		};
		
		this.addDataSlots(data);
	}
	
	@Override
	public boolean stillValid(PlayerEntity p_75145_1_) {
		return stillValid(canInteractWithCallable, p_75145_1_, TestModule.TEST_BLOCK);
	}

	public TestContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data, TestTileEntity.class));
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getEnergyScaled() {
		return this.data.get(0) != 0 && this.energyTracker.getMaxStorage() != 0
				? this.data.get(0) * 76 / this.energyTracker.getMaxStorage()
				: 0;
	}

}
