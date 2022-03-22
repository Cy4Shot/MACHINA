package com.machina.datagen.client.lang;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.Machina;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.PlanetTraitInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;

public class EnUsLangProvider extends LanguageProvider {

	public static final Logger LOGGER = LogManager.getLogger();

	public EnUsLangProvider(DataGenerator gen) {
		super(gen, Machina.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		
		addBlock(() -> BlockInit.ALIEN_STONE.get(), "Alien Stone");
		addBlock(() -> BlockInit.TWILIGHT_DIRT.get(), "Twilight Dirt");

		add(PlanetTraitInit.WATER_WORLD, "Water World");
		add(PlanetTraitInit.CONTINENTAL, "Continental");
		add(PlanetTraitInit.LANDMMASS, "Landmass");
		add(PlanetTraitInit.MOUNTAINOUS, "Mountainous");
		add(PlanetTraitInit.HILLY, "Hilly");
		add(PlanetTraitInit.FLAT, "Flat");
		add(PlanetTraitInit.LAKES, "Lakes");
		add(PlanetTraitInit.FROZEN, "Frozen");

		addItemGroup("machinaItemGroup", "Machina");
		
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
	}

	private void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}

	private void add(PlanetTrait trait, String name) {
		add(trait.getRegistryName().getNamespace() + ".planet_trait." + trait.getRegistryName().getPath(), name);
	}

	@SuppressWarnings("unused")
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
