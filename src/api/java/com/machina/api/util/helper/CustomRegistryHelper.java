/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.util.helper;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.api.util.objects.TargetField;

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
