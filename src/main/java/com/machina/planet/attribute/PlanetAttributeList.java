package com.machina.planet.attribute;

import java.util.Collection;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.machina.util.nbt.NBTList;

import net.minecraft.nbt.CompoundNBT;

public class PlanetAttributeList extends NBTList<PlanetAttribute<?>, CompoundNBT> {

	private static final long serialVersionUID = 357647471350483603L;

	public PlanetAttributeList() {
		super(PlanetAttribute::fromNBT);
	}

	@Override
	public boolean add(PlanetAttribute<?> e) {
		if (getAttributeForType(e.getAttributeType()).isPresent())
			return false;
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
			if (get(i).getAttributeType() == type)
				return Optional.ofNullable((PlanetAttribute<Z>) get(i));
		}
		return Optional.empty();
	}

	/**
	 * Sets the value of the attribute, creating it if it does not exist
	 * 
	 * @param <Z>
	 * @param type
	 * @param value a function accepting the old value and returning the new one.
	 *              The input value will be null if the attribute doesn't exist so
	 *              keep that in mind
	 */
	public <Z> void setValue(PlanetAttributeType<Z> type, UnaryOperator<Z> value) {
		getAttributeForType(type).ifPresent(attr -> attr.setValue(value.apply(attr.getValue())));
		if (!getAttributeForType(type).isPresent())
			add(new PlanetAttribute<>(type, value.apply(null)));
	}

	public <Z> Z getValue(PlanetAttributeType<Z> t) {
		return getAttributeForType(t).get().getValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(PlanetAttributeType<?> type, Object value) {
		getAttributeForType(type).ifPresent(attr -> attr.set(value));
		if (!getAttributeForType(type).isPresent())
			add(new PlanetAttribute(type, value));
	}

	public UnaryOperator<Integer> intOperator(Operation operation, Integer value) {
		switch (operation) {
		case ADDITION:
			return old -> value + old;
		case SUBSTRACTION:
			return old -> old - value;
		case MULTIPLICATION:
			return old -> value * old;
		case DIVISON:
			return old -> old / value;
		}
		return old -> value;
	}

	public enum Operation {
		ADDITION, SUBSTRACTION, MULTIPLICATION, DIVISON;
	}
}
