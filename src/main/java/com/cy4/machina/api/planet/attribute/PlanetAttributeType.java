package com.cy4.machina.api.planet.attribute;

import static com.cy4.machina.Machina.MOD_ID;

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
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

@RegistryHolder
public class PlanetAttributeType<T> extends ForgeRegistryEntry<PlanetAttributeType<?>> {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<PlanetAttributeType<?>> REGISTRY = null;

	@RegisterCustomRegistry
	public static void createRegistry(RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.registerRegistry(new TargetField(PlanetAttributeType.class, "REGISTRY"),
				ClassHelper.withWildcard(PlanetAttributeType.class), new MachinaRL("planet_attribute_type"),
				Optional.empty(), Optional.empty(), Optional.of(AddCallback.INSTANCE));
	}

	public static final class AddCallback
			implements net.minecraftforge.registries.IForgeRegistry.AddCallback<PlanetAttributeType<?>> {

		public static final AddCallback INSTANCE = new AddCallback();

		@Override
		public void onAdd(IForgeRegistryInternal<PlanetAttributeType<?>> owner, RegistryManager stage, int id,
				PlanetAttributeType<?> obj, PlanetAttributeType<?> oldObj) {
			if (!obj.getRegistryName().getNamespace().equals(MOD_ID)) {
				if (REGISTRY.containsKey(obj.getRegistryName())) {
					//TODO prevent registration
				}
			}
		}
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

}
