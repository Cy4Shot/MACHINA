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

package com.machina.api.registry.annotation;

import static com.machina.api.registry.annotation.MachinaRegistries.ADVANCED_CRAFTING_FUNCTION_SERIALIZERS;
import static com.machina.api.registry.annotation.MachinaRegistries.PLANET_ATTRIBUTE_TYPES;
import static com.machina.api.registry.annotation.MachinaRegistries.PLANET_TRAITS;
import static java.util.Optional.of;

import com.machina.api.planet.attribute.PlanetAttributeType;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.recipe.advanced_crafting.AdvancedCraftingFunctionSerializer;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class MachinaAnnotationProcessor extends com.matyrobbrt.lib.registry.annotation.AnnotationProcessor {
	
	public MachinaAnnotationProcessor(String modid) {
		super(modid);
	}

	/**
	 * Adds listeners for all the methods that process annotations. Basically starts
	 * the actual registering. <br>
	 * Call it in your mods' constructor
	 *
	 * @param modBus
	 */
	@Override
	public void register(IEventBus modBus) {
		super.register(modBus);
		// Registry Annotations
		modBus.addGenericListener(AdvancedCraftingFunctionSerializer.class, this::registerAdvancedCraftingFunctions);
		modBus.addGenericListener(PlanetTrait.class, this::registerPlanetTraits);
		modBus.addGenericListener(PlanetAttributeType.class, this::registerPlanetAttributeTypes);
	}


	private void registerPlanetTraits(final RegistryEvent.Register<PlanetTrait> event) {
		registerFieldsWithAnnotation(event, RegisterPlanetTrait.class, RegisterPlanetTrait::id, of(PLANET_TRAITS));
	}

	private void registerPlanetAttributeTypes(final RegistryEvent.Register<PlanetAttributeType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterPlanetAttributeType.class, RegisterPlanetAttributeType::value,
				of(PLANET_ATTRIBUTE_TYPES));
	}

	private void registerAdvancedCraftingFunctions(
			final RegistryEvent.Register<AdvancedCraftingFunctionSerializer<?>> event) {
		registerFieldsWithAnnotation(event, RegisterACFunctionSerializer.class, RegisterACFunctionSerializer::value,
				of(ADVANCED_CRAFTING_FUNCTION_SERIALIZERS));
	}

}
