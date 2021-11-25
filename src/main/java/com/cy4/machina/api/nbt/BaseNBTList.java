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

package com.cy4.machina.api.nbt;

import java.util.ArrayList;
import java.util.function.Function;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unchecked")
public class BaseNBTList<O, ONBT extends INBT> extends ArrayList<O> implements INBTSerializable<CompoundNBT> {

	private static final long serialVersionUID = -3021708656511519616L;

	private final transient Function<O, ONBT> serializer;
	private final transient Function<ONBT, O> deserializer;

	public BaseNBTList(Function<O, ONBT> serializer, Function<ONBT, O> deserializer) {
		super();
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("size", size());
		for (int i = 0; i < size(); i++) {
			tag.put(String.valueOf(i), serializer.apply(get(i)));
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			O element = deserializer.apply((ONBT) nbt.get(String.valueOf(i)));
			if (i < size()) {
				set(i, element);
			} else {
				add(i, element);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof BaseNBTList<?, ?>;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
