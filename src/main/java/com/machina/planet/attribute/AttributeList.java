package com.machina.planet.attribute;

import java.util.Collection;
import java.util.Optional;

import com.machina.util.serial.NBTList;

import net.minecraft.nbt.CompoundNBT;

public class AttributeList extends NBTList<Attribute<?>, CompoundNBT> {

	private static final long serialVersionUID = 357647471350483603L;

	public AttributeList() {
		super(Attribute::fromNBT);
	}

	@Override
	public boolean add(Attribute<?> e) {
		if (getAttributeForType(e.getAttributeType()).isPresent())
			return false;
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Attribute<?>> c) {
		int oldSize = size();
		c.forEach(this::add);
		return oldSize != size();
	}

	@SuppressWarnings("unchecked")
	public <Z> Optional<Attribute<Z>> getAttributeForType(AttributeType<Z> type) {
		for (int i = 0; i < size(); i++) {
			if (get(i).getAttributeType() == type)
				return Optional.ofNullable((Attribute<Z>) get(i));
		}
		return Optional.empty();
	}

	public <Z> Z getValue(AttributeType<Z> t) {
		return getAttributeForType(t).get().getValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(AttributeType<?> type, Object value) {
		getAttributeForType(type).ifPresent(attr -> attr.set(value));
		if (!getAttributeForType(type).isPresent())
			add(new Attribute(type, value));
	}
}
