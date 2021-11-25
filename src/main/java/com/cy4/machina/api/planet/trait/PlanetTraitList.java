/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.planet.trait;

import java.util.Collection;

import com.cy4.machina.api.nbt.BaseNBTList;
import com.cy4.machina.api.planet.attribute.PlanetAttributeList;

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
