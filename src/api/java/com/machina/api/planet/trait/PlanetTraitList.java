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

package com.machina.api.planet.trait;

import java.util.Collection;

import com.machina.api.nbt.BaseNBTList;
import com.machina.api.planet.attribute.PlanetAttributeList;

import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public class PlanetTraitList extends BaseNBTList<PlanetTrait, StringNBT> {

	private static final long serialVersionUID = -363048464726157694L;
	
	private final PlanetAttributeList attributes;

	public PlanetTraitList(PlanetAttributeList attributes) {
		super(trait -> StringNBT.valueOf(trait.getRegistryName().toString()),
				nbt -> PlanetTrait.REGISTRY.getValue(new ResourceLocation(nbt.getAsString())));
		this.attributes = attributes;
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
	 * @deprecated Use {@link #addTrait} so that the attributes are updated
	 * @param e
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
	 * Adds a trait to the list, whilst also calling {@link #update(PlanetTrait, ChangeType)}
	 * @param trait
	 * @return
	 */
	public boolean addTrait(PlanetTrait trait) {
		boolean result = add(trait);
		if (result && trait.hasAttributeModifier()) {
			update(trait, ChangeType.ADD);
		}
		return result;
	}
	
	/**
	 * Removes a trait from the list, whilst also calling {@link #update(PlanetTrait, ChangeType)}
	 * @param trait
	 * @return
	 */
	public boolean removeTrait(PlanetTrait trait) {
		boolean result = remove(trait);
		if (result && trait.hasAttributeModifier()) {
			update(trait, ChangeType.REMOVE);
		}
		return result;
	}
	
	public void update(PlanetTrait trait, ChangeType type) {
		switch (type) {
		case ADD: trait.addAttribute(attributes);
		case REMOVE: trait.removeAttribute(attributes);
		}
	}
	
	public enum ChangeType {
		ADD, REMOVE;
	}

}
