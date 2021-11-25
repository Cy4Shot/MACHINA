package com.cy4.machina.api.planet.attribute;

import java.util.Optional;
import java.util.function.Function;

import com.cy4.machina.api.annotation.ChangedByReflection;
import com.cy4.machina.api.annotation.registries.RegisterCustomRegistry;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.util.MachinaRL;
import com.cy4.machina.util.helper.ClassHelper;
import com.cy4.machina.util.helper.CustomRegistryHelper;
import com.cy4.machina.util.objects.TargetField;

import net.minecraft.nbt.INBT;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

@RegistryHolder
public class PlanetAttributeType<T> extends ForgeRegistryEntry<PlanetAttributeType<?>> {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<PlanetAttributeType<?>> REGISTRY = null;

	@RegisterCustomRegistry
	public static void createRegistry(RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.registerRegistry(new TargetField(PlanetAttributeType.class, "REGISTRY"),
				ClassHelper.withWildcard(PlanetAttributeType.class), new MachinaRL("planet_attribute_type"),
				Optional.empty(), Optional.empty());
	}

	private final String measureUnit;

	public final Function<T, INBT> valueSerializer;
	public final Function<INBT, T> valueDeserializer;

	public PlanetAttributeType(String measureUnit, Function<T, INBT> valueSerializer,
			Function<INBT, T> valueDeserializer) {
		this.measureUnit = measureUnit;
		this.valueSerializer = valueSerializer;
		this.valueDeserializer = valueDeserializer;
	}

	public String getMeasureUnit() { return measureUnit; }
	
	public boolean isShown() { return true; }

}
