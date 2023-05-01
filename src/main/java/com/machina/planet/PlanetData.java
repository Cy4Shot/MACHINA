package com.machina.planet;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.machina.planet.attribute.Attribute;
import com.machina.planet.attribute.AttributeList;
import com.machina.planet.attribute.AttributeType;
import com.machina.planet.attribute.serializers.DoubleListSerializer;
import com.machina.planet.pool.TraitPoolLoader;
import com.machina.planet.trait.TraitList;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.registry.AttributeRegistry;
import com.machina.registration.registry.TraitRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class PlanetData implements INBTSerializable<CompoundNBT> {

	private final TraitList traits = new TraitList();
	private final AttributeList attributes = new AttributeList();

	public static PlanetData fromNBT(CompoundNBT nbt) {
		PlanetData pd = new PlanetData();
		pd.deserializeNBT(nbt);
		return pd;
	}

	public static PlanetData fromRand(Random rand) {
		PlanetData pd = new PlanetData();
		pd.generate(rand);
		return pd;
	}

	public void generate(Random rand) {
		// Attributes
		AttributeRegistry.REGISTRY.getEntries().forEach(entry -> {
			attributes.set(entry.getValue(), entry.getValue().ser.rand(rand)); // Kinda Hacky .. dunno
		});

		// Traits
		traits.clear();
		TraitPoolLoader.INSTANCE.forEach((location, pool) -> traits.addAll(
				pool.roll(rand).stream().map(TraitRegistry.REGISTRY::getValue).collect(Collectors.toList())));
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = new CompoundNBT();
		nbt.put("traits", traits.serializeNBT());
		nbt.put("attributes", attributes.serializeNBT());

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		deserialize(nbt, this);
	}

	public static PlanetData deserialize(CompoundNBT nbt) {
		PlanetData data = new PlanetData();
		deserialize(nbt, data);
		return data;
	}

	private static PlanetData deserialize(CompoundNBT nbt, PlanetData data) {
		data.traits.deserializeNBT(nbt.getCompound("traits"));
		data.attributes.deserializeNBT(nbt.getCompound("attributes"));
		return data;
	}

	public TraitList getTraits() {
		return traits;
	}

	public AttributeList getAttributes() {
		return attributes;
	}

	public <T> T getAttribute(AttributeType<T> type) {
		Optional<Attribute<T>> op = attributes.getAttributeForType(type);
		if (op.isPresent())
			return op.get().getValue();
		else {
			return type.ser.def();
		}
	}

	public Double[] getAtmosphere(RegistryKey<World> dim) {
		Optional<Attribute<Double[]>> op = attributes.getAttributeForType(AttributeInit.ATMOSPHERE);
		if (op.isPresent())
			return op.get().getValue();
		else {
			return ((DoubleListSerializer) AttributeInit.ATMOSPHERE.ser).func.apply(dim);
		}
	}

	public <T> String getAttributeFormatted(AttributeType<T> type) {
		return String.valueOf(attributes.getAttributeForType(type).get().getValueFormatted()) + " "
				+ type.getMeasureUnit();
	}

	public static PlanetData NONE = new PlanetData();
}
