package com.cy4.machina.api.planet.attribute;

import java.util.Collection;
import java.util.Optional;

import com.cy4.machina.api.nbt.NBTList;

import net.minecraft.nbt.CompoundNBT;

public class PlanetAttributeList extends NBTList<PlanetAttribute<?>, CompoundNBT> {

	private static final long serialVersionUID = 357647471350483603L;

	public PlanetAttributeList() {
		super(PlanetAttribute::fromNBT);
	}

	@Override
	public boolean add(PlanetAttribute<?> e) {
		if (getAttributeForType(e.getAttributeType()).isPresent()
				&& getAttributeForType(e.getAttributeType()).get() != null) {
			return false;
		}
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends PlanetAttribute<?>> c) {
		int oldSize = size();
		c.forEach(this::add);
		return oldSize != size();
	}

	@SuppressWarnings("unchecked")
	public <Z> Optional<PlanetAttribute<Z>> getAttributeForType(PlanetAttributeType<Z> type) {
		for (int i = 0; i < size(); i++) {
			if (get(i).getAttributeType() == type) { return Optional.ofNullable((PlanetAttribute<Z>) get(i)); }
		}
		return Optional.empty();
	}

}
