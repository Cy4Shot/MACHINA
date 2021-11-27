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

package com.machina.api.world.data;

import static com.machina.api.ModIDs.MACHINA;

import java.util.HashSet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import net.minecraftforge.common.util.Constants;

public class PlanetDimensionData extends WorldSavedData {

	public final HashSet<String> dimensionIds = new HashSet<>();

	public PlanetDimensionData(String n) {
		super(n);
	}

	private static final String ID = MACHINA + "_planet_dimensions";

	public static PlanetDimensionData getDefaultInstance(MinecraftServer server) {
		return server.getLevel(World.OVERWORLD).getDataStorage().computeIfAbsent(() -> new PlanetDimensionData(ID), ID);
	}

	@Override
	public void load(CompoundNBT nbt) {
		dimensionIds.clear();
		ListNBT listNBT = nbt.getList("dimensionIds", Constants.NBT.TAG_STRING);
		for (INBT inbt : listNBT) {
			StringNBT stringNBT = (StringNBT) inbt;
			dimensionIds.add(stringNBT.getAsString());
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		ListNBT listNBT = new ListNBT();
		for (String str : dimensionIds) {
			listNBT.add(StringNBT.valueOf(str));
		}
		nbt.put("dimensionIds", listNBT);
		nbt.putString("dataOwnerMod", MACHINA);
		return nbt;
	}

	public void addId(String id) {
		dimensionIds.add(id);
		setDirty();
	}

	public void removeId(String id) {
		dimensionIds.remove(id);
		setDirty();
	}

}
