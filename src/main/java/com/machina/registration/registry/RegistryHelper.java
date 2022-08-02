package com.machina.registration.registry;

import java.lang.reflect.Field;
import java.util.Optional;

import com.machina.Machina;
import com.machina.util.reflection.ClassHelper;
import com.machina.util.reflection.TargetField;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class RegistryHelper {

	public static <T extends IForgeRegistryEntry<T>> void registerRegistry(TargetField targetField,
			Class<T> registryClass, ResourceLocation registryName) {
		registerRegistry(targetField, registryClass, registryName, Optional.empty());
	}

	public static <T extends IForgeRegistryEntry<T>> void registerRegistry(TargetField targetField,
			Class<T> registryClass, ResourceLocation registryName, Optional<ResourceLocation> defaultValue) {
		registerRegistry(targetField, registryClass, registryName, defaultValue, Optional.empty());
	}

	public static <T extends IForgeRegistryEntry<T>> void registerRegistry(TargetField targetField,
			Class<T> registryClass, ResourceLocation registryName, Optional<ResourceLocation> defaultValue,
			Optional<ResourceLocation> legacyName) {
		RegistryBuilder<T> registryBuilder = new RegistryBuilder<>();
		registryBuilder.setName(registryName);
		registryBuilder.setType(registryClass);

		defaultValue.ifPresent(registryBuilder::setDefaultKey);
		legacyName.ifPresent(registryBuilder::legacyName);

		try {
			Field registryField = targetField.getField();
			registryField.setAccessible(true);
			ClassHelper.removeFinalModifier(registryField);
			IForgeRegistry<T> newRegistry = registryBuilder.create();
			registryField.set(targetField.targetFieldClass, newRegistry);
			Machina.LOGGER.info("Created Machina Registry For {}!", registryName);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new CustomRegistryException(e);
		}
	}

	private static final class CustomRegistryException extends RuntimeException {

		private static final long serialVersionUID = 204522615406714921L;

		public CustomRegistryException(Throwable e) {
			super(e);
		}
	}

}
