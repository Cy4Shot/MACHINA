package com.cy4.machina.util.helper;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cy4.machina.util.objects.TargetField;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class CustomRegistryHelper {

	public static final Logger LOGGER = LogManager.getLogger();

	public static <T extends IForgeRegistryEntry<T>> void registerRegistry(TargetField targetField,
			Class<T> registryClass, ResourceLocation registryName) {
		registerRegistry(targetField, registryClass, registryName, Optional.empty());
	}

	public static <T extends IForgeRegistryEntry<T>> void registerRegistry(TargetField targetField,
			Class<T> registryClass, ResourceLocation registryName, Optional<ResourceLocation> defaultValue) {
		registerRegistry(targetField, registryClass, registryName, Optional.empty());
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
			FieldUtils.removeFinalModifier(registryField);
			IForgeRegistry<T> newRegistry = registryBuilder.create();
			registryField.set(targetField.targetFieldClass, newRegistry);
			LOGGER.info("Created Custom Forge Registry {}!", registryName);
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
