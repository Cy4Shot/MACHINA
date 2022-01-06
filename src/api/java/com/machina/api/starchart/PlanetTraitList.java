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

package com.machina.api.starchart;

import java.util.Collection;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.registry.PlanetTraitRegistry;
import com.matyrobbrt.lib.nbt.BaseNBTList;

import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

// TODO add some kind of setChanged
public class PlanetTraitList extends BaseNBTList<PlanetTrait, StringNBT> {

	private static final long serialVersionUID = -363048464726157694L;

	public PlanetTraitList() {
		super(trait -> StringNBT.valueOf(trait.getRegistryName().toString()),
				nbt -> PlanetTraitRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getAsString())));
	}

	/**
	 * @deprecated Use {@link #addTrait} so that the attributes are updated
	 * @param e
	 */
	@Deprecated
	@Override
	public boolean add(PlanetTrait e) {
		if (contains(e) || !e.exists()) { return false; }
		return super.add(e);
	}

	/**
	 * @deprecated Use {@link #addTrait}
	 * @param index
	 * @param element
	 */
	@Deprecated
	@Override
	public void add(int index, PlanetTrait element) {
		if (contains(element) || !element.exists()) { return; }
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends PlanetTrait> c) {
		int oldSize = size();
		c.forEach(this::addTrait);
		return oldSize != size();
	}
	
	/**
	 * Adds a trait to the list
	 * @param trait
	 * @return
	 */
	public boolean addTrait(PlanetTrait trait) {
		boolean result = add(trait);
		return result;
	}
	
	/**
	 * Removes a trait from the list
	 * @param trait
	 * @return
	 */
	public boolean removeTrait(PlanetTrait trait) {
		boolean result = remove(trait);
		return result;
	}

}
