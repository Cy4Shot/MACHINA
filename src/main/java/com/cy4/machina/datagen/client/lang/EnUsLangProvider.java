package com.cy4.machina.datagen.client.lang;

import static com.cy4.machina.Machina.MOD_ID;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.init.FluidInit;
import com.cy4.machina.init.ItemInit;
import com.cy4.machina.init.MachinaRegistries;
import com.cy4.machina.init.PlanetTraitInit;
import com.cy4.machina.util.helper.TextFormattingHelper;
import com.google.common.collect.Lists;

import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;

public class EnUsLangProvider extends LanguageProvider {
	
	public static final Logger LOGGER = LogManager.getLogger();
	private final Map<String, String> data = new TreeMap<>();
	
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

		addCommandArgumentFeedback("planet_trait.invalid", "Invalid Planet Trait: §6%s");

		addDamageSourceMsg("liquidHydrogen", "%1$s stayed too much in hydrogen... Never do that at home kids!", "%1$s encountered hydrogen whilst fighting %2$s!");

		add("machina.screen.starchart.title", "Starchart");
		
		add("multiblock.rocket.missing_relay", "Missing Relay at direction: $s0");
		add("direction.north", "North");
		add("direction.south", "South");
		add("direction.east", "East");
		add("direction.west", "West");
		
		addAutoItems(Lists.newArrayList(ItemInit.ITEM_GROUP_ICON));
	}
	
	private void addAutoItems(List<Item> blacklisted) {
		MachinaRegistries.ITEMS.get(MOD_ID).stream().filter(item -> !blacklisted.contains(item)).forEach(item -> {
			String name = item.getRegistryName().getPath().replace("_", " ");
			add(item, TextFormattingHelper.capitalizeWord(name));
		});
	}

	private void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}

	private void add(PlanetTrait trait, String name) {
		add(trait.getRegistryName().getNamespace() + ".trait." + trait.getRegistryName().getPath(), name);
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
	
	@Override
	public void add(String key, String value) {
		if (!data.containsKey(key)) {
			super.add(key, value);
		} else {
			LOGGER.warn("Found already existing key {}! Skipping.", key);
		}
	}
}
