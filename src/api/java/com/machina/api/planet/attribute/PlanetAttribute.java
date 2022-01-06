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

package com.machina.api.planet.attribute;

import com.machina.api.registry.PlanetAttributeRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class PlanetAttribute<T> implements INBTSerializable<CompoundNBT> {

	private PlanetAttributeType<T> attributeType;
	private T value;
	
	public PlanetAttribute(PlanetAttributeType<T> attributeType, T value) {
		this.attributeType = attributeType;
		this.value = value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	public void set(Object v) {
		setValue((T) v);
	}
	
	public T getValue() { return this.value; }

	public PlanetAttributeType<T> getAttributeType() {
		return attributeType;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.put("value", attributeType.valueSerializer.apply(this.value));
		tag.putString("type", attributeType.getRegistryName().toString());
		return tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.attributeType = (PlanetAttributeType<T>) PlanetAttributeRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("type")));
		this.value = attributeType.valueDeserializer.apply(nbt.get("value"));
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static PlanetAttribute<?> fromNBT(CompoundNBT nbt) {
		PlanetAttributeType<?> type = PlanetAttributeRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("type")));
		if (type != null) {
			return new PlanetAttribute(type, type.valueDeserializer.apply(nbt.get("value")));
		}
		return null;
	}
	
}
