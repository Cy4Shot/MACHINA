package com.machina.planet.trait;

import java.util.Collection;

import com.machina.registration.registry.TraitRegistry;
import com.machina.util.serial.BaseNBTList;

import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public class TraitList extends BaseNBTList<Trait, StringNBT> {

	private static final long serialVersionUID = -363048464726157694L;

	public TraitList() {
		super(trait -> StringNBT.valueOf(trait.getRegistryName().toString()),
				nbt -> TraitRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getAsString())));
	}
	
	@Deprecated
	@Override
	public boolean add(Trait e) {
		if (contains(e) || !e.exists()) { return false; }
		return super.add(e);
	}
	
	@Deprecated
	@Override
	public void add(int index, Trait element) {
		if (contains(element) || !element.exists()) { return; }
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends Trait> c) {
		int oldSize = size();
		c.forEach(this::addTrait);
		return oldSize != size();
	}
	
	public boolean addTrait(Trait trait) {
		boolean result = add(trait);
		return result;
	}
}
