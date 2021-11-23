package com.cy4.machina.api.planet.trait;

import java.util.Collection;

import com.cy4.machina.api.nbt.BaseNBTList;

import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public class PlanetTraitList extends BaseNBTList<PlanetTrait, StringNBT> {
	
	private static final long serialVersionUID = -363048464726157694L;

	public PlanetTraitList() {
		super(trait -> StringNBT.valueOf(trait.getRegistryName().toString()),
				nbt -> PlanetTrait.REGISTRY.getValue(new ResourceLocation(nbt.getAsString())));
	}
	
	@Override
	public boolean add(PlanetTrait e) {
		if (contains(e) || !e.exists()) {
			return false;
		}
		return super.add(e);
	}
	
	@Override
	public void add(int index, PlanetTrait element) {
		if (contains(element) || !element.exists()) {
			return;
		}
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(Collection<? extends PlanetTrait> c) {
		int oldSize = size();
		c.forEach(this::add);
		return oldSize != size();
	}

}
