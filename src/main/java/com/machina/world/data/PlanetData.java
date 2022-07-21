package com.machina.world.data;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.machina.planet.PlanetTraitPoolManager;
import com.machina.planet.attribute.PlanetAttribute;
import com.machina.planet.attribute.PlanetAttributeList;
import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.attribute.serializers.DoubleListSerializer;
import com.machina.planet.trait.PlanetTraitList;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.registry.PlanetAttributeRegistry;
import com.machina.registration.registry.PlanetTraitRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class PlanetData implements INBTSerializable<CompoundNBT> {

	private final PlanetTraitList traits = new PlanetTraitList();
	private final PlanetAttributeList attributes = new PlanetAttributeList();

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
		PlanetAttributeRegistry.REGISTRY.getEntries().forEach(entry -> {
			attributes.set(entry.getValue(), entry.getValue().ser.rand(rand)); // Kinda Hacky .. dunno
		});

		// Traits
		traits.clear();
		PlanetTraitPoolManager.INSTANCE.forEach((location, pool) -> traits.addAll(
				pool.roll(rand).stream().map(PlanetTraitRegistry.REGISTRY::getValue).collect(Collectors.toList())));
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

	public PlanetTraitList getTraits() {
		return traits;
	}

	public PlanetAttributeList getAttributes() {
		return attributes;
	}

	public <T> T getAttribute(PlanetAttributeType<T> type) {
		Optional<PlanetAttribute<T>> op = attributes.getAttributeForType(type);
		if (op.isPresent())
			return op.get().getValue();
		else {
			return type.ser.def();
		}
	}

	public Double[] getAtmosphere(RegistryKey<World> dim) {
		Optional<PlanetAttribute<Double[]>> op = attributes.getAttributeForType(AttributeInit.ATMOSPHERE);
		if (op.isPresent())
			return op.get().getValue();
		else {
			return ((DoubleListSerializer) AttributeInit.ATMOSPHERE.ser).func.apply(dim);
		}
	}

	public <T> String getAttributeFormatted(PlanetAttributeType<T> type) {
		return String.valueOf(attributes.getAttributeForType(type).get().getValueFormatted()) + " "
				+ type.getMeasureUnit();
	}

	public static PlanetData NONE = new PlanetData();
}
