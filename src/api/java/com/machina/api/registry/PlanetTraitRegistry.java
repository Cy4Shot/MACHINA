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

package com.machina.api.registry;

import java.util.Optional;

import com.machina.api.ModIDs;
import com.machina.api.annotation.ChangedByReflection;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.helper.CustomRegistryHelper;
import com.machina.api.util.objects.TargetField;
import com.matyrobbrt.lib.registry.annotation.RegisterCustomRegistry;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

@RegistryHolder(modid = ModIDs.MACHINA)
public class PlanetTraitRegistry {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<PlanetTrait> REGISTRY = null;

	@RegisterCustomRegistry
	public static void createRegistry(RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.<PlanetTrait>registerRegistry(new TargetField(PlanetTraitRegistry.class, "REGISTRY"),
				PlanetTrait.class, new MachinaRL("planet_trait"), Optional.of(new MachinaRL("none")),
				Optional.of(new MachinaRL("planet_trait_registry")));
	}
}
