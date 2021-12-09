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

import java.util.Random;

import com.machina.api.tile_entity.EnergyTileEntity;
import com.machina.api.tile_entity.MachinaEnergyStorage;
import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.annotation.SyncValue.SyncType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TestTileEntity extends EnergyTileEntity implements INamedContainerProvider, ITickableTileEntity {

	@SyncValue(name = "randomIntSync", onPacket = true)
	int randomInt = 129;
	@SyncValue(name = "randomStringSync", onPacket = true)
	String randomString = "random yes";

	public TestTileEntity() {
		super(TestModule.TEST_TE_TYPE);
	}

	@Override
	protected MachinaEnergyStorage createEnergyStorage() {
		return new MachinaEnergyStorage(1200, 20, 20);
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return new TestContainer(p_createMenu_1_, p_createMenu_2_, this);
	}

	@Override
	public ITextComponent getDisplayName() { return new StringTextComponent(""); }

	@Override
	public void tick() {
		/*
		 * if (level.isClientSide()) {
		 * System.out.println(energyStorage.getEnergyStored()); } else {
		 * energyStorage.receiveEnergy(100, false); sync(Direction.SERVER_TO_CLIENT); }
		 */
		if (new Random().nextInt(259) == 15) {
			System.out.println(
					SyncValue.Helper.writeSyncValues(getSyncFields(), this, new CompoundNBT(), SyncType.PACKET));
		}
	}

}
