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

package com.machina.datagen.client.lang;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.machina.Machina;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.init.FluidInit;
import com.machina.init.ItemInit;
import com.machina.init.PlanetTraitInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;

public class EnUsLangProvider extends LanguageProvider {

	public static final Logger LOGGER = LogManager.getLogger();

	public EnUsLangProvider(DataGenerator gen) {
		super(gen, Machina.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {

		add(PlanetTraitInit.WATER_WORLD, "Water World");
		add(PlanetTraitInit.CONTINENTALL, "Continental");
		add(PlanetTraitInit.LANDMMASS, "Landmass");
		add(PlanetTraitInit.MOUNTAINOUS, "Mountainous");
		add(PlanetTraitInit.HILLY, "Hilly");
		add(PlanetTraitInit.FLAT, "Flat");
		add(PlanetTraitInit.ORE_RICH, "Ore Rich");
		add(PlanetTraitInit.ORE_BARREN, "Ore Barren");
		add(PlanetTraitInit.CANYONS, "Canyons");
		add(PlanetTraitInit.FIORDS, "Fiords");
		add(PlanetTraitInit.RAVINES, "Ravines");
		add(PlanetTraitInit.LAKES, "Lakes");
		add(PlanetTraitInit.VOLCANIC, "Volcanic");
		add(PlanetTraitInit.FROZEN, "Frozen");
		add(PlanetTraitInit.LAYERED, "Layered");

		addItemGroup("machinaItemGroup", "Machina");

		add(FluidInit.OXYGEN.get(), "Oxygen");
		add(FluidInit.HYDROGEN.get(), "Hydrogen");
		add(FluidInit.LIQUID_HYDROGEN.get(), "Liquid Hydrogen");
		
		addCommandFeedback("planet_traits.add_trait.success", "Trait added!");
		addCommandFeedback("planet_traits.add_trait.duplicate", "This planet already has the trait %s!");
		addCommandFeedback("planet_traits.remove_trait.success", "Trait removed!");
		addCommandFeedback("planet_traits.remove_trait.not_existing", "This planet does not have the trait %s!");
		addCommandFeedback("planet_traits.list_traits.success", "This planet has the following traits: \n§6%s");
		addCommandFeedback("planet_traits.list_traits.no_traits", "This planet has no traits!");
		addCommandFeedback("planet_traits.not_planet", "This dimension is not a planet!");

		addCommandArgumentFeedback("planet_trait.invalid", "Invalid Planet Trait: \u00A76%s");

		addDamageSourceMsg("liquidHydrogen", "%1$s stayed too much in hydrogen... Never do that at home kids!",
				"%1$s encountered hydrogen whilst fighting %2$s!");

		add("machina.screen.starchart.title", "Starchart");

		add("multiblock.rocket.missing_relay", "Missing Relay at direction: $s0");
		add("direction.north", "North");
		add("direction.south", "South");
		add("direction.east", "East");
		add("direction.west", "West");

		add("jei.planet_trait.type", "Planet Trait");

		addAutoItems(Lists.newArrayList(ItemInit.ITEM_GROUP_ICON));
	}

	private void addAutoItems(List<Item> blacklisted) {
		Machina.ANNOTATION_PROCESSOR.getItems().stream().filter(item -> !blacklisted.contains(item)).forEach(item -> {
			String name = item.getRegistryName().getPath().replace("_", " ");
			add(item, capitalizeWord(name));
		});
	}

	private void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}

	private void add(PlanetTrait trait, String name) {
		add(trait.getRegistryName().getNamespace() + ".planet_trait." + trait.getRegistryName().getPath(), name);
	}

	private void add(Fluid fluid, String name) {
		add(new FluidStack(fluid, 2).getTranslationKey(), name);
	}

	private void addCommandFeedback(String key, String name) {
		add("command." + key, name);
	}
	
	private void addCommandArgumentFeedback(String key, String name) {
		add("argument." + key, name);
	}

	private void addDamageSourceMsg(String name, String normal, String diedWhilstFighting) {
		add("death.attack." + name, normal);
		add("death.attack." + name + ".player", diedWhilstFighting);
	}

	public static String capitalizeWord(String str) {
		String words[] = str.split("\\s");
		String capitalizeWord = "";
		for (String w : words) {
			String first = w.substring(0, 1);
			String afterfirst = w.substring(1);
			capitalizeWord += first.toUpperCase() + afterfirst + " ";
		}
		return capitalizeWord.trim();
	}
}
