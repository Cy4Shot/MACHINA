package com.machina.datagen.client.lang;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.Machina;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
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
		
		// Blocks
		add(BlockInit.SHIP_CONSOLE.get(), "Ship Console");
		add(BlockInit.CARGO_CRATE.get(), "Cargo Crate");
		add(BlockInit.COMPONENT_ANALYZER.get(), "Component Analyzer");
		add(BlockInit.PUZZLE_BLOCK.get(), "Puzzle Block");
		
		add(BlockInit.STEEL_BLOCK.get(), "Steel Block");
		add(BlockInit.STEEL_CHASSIS.get(), "Steel Chassis");
		add(BlockInit.IRON_CHASSIS.get(), "Iron Chassis");
		
		add(BlockInit.ALIEN_STONE.get(), "Alien Stone");
		add(BlockInit.ALIEN_STONE_SLAB.get(), "Alien Stone Slab");
		add(BlockInit.ALIEN_STONE_STAIRS.get(), "Alien Stone Stairs");
		add(BlockInit.TWILIGHT_DIRT.get(), "Twilight Dirt");
		add(BlockInit.TWILIGHT_DIRT_SLAB.get(), "Twilight Dirt Slab");
		add(BlockInit.TWILIGHT_DIRT_STAIRS.get(), "Twilight Stairs");
		add(BlockInit.WASTELAND_DIRT.get(), "Wasteland Dirt");
		add(BlockInit.WASTELAND_DIRT_SLAB.get(), "Wasteland Dirt Slab");
		add(BlockInit.WASTELAND_DIRT_STAIRS.get(), "Wasteland Dirt Stairs");
		add(BlockInit.WASTELAND_SAND.get(), "Wasteland Sand");
		add(BlockInit.WASTELAND_SANDSTONE.get(), "Wasteland Sandstone");
		add(BlockInit.WASTELAND_SANDSTONE_SLAB.get(), "Wasteland Sandstone Slab");
		add(BlockInit.WASTELAND_SANDSTONE_STAIRS.get(), "Wasteland Sandstone Stairs");
		add(BlockInit.WASTELAND_SANDSTONE_WALL.get(), "Wasteland Sandstone Wall");
		
		// Items
		add(ItemInit.THERMAL_REGULATING_HELMET.get(), "Thermal Regulating Helmet");
		add(ItemInit.THERMAL_REGULATING_CHESTPLATE.get(), "Thermal Regulating Chestplate");
		add(ItemInit.THERMAL_REGULATING_LEGGINGS.get(), "Thermal Regulating Leggings");
		add(ItemInit.THERMAL_REGULATING_BOOTS.get(), "Thermal Regulating Boots");
		add(ItemInit.WRENCH.get(), "Wrench");
		add(ItemInit.SHIP_COMPONENT.get(), "Ship Component");
		add(ItemInit.SCANNER.get(), "Scanner");
		add(ItemInit.REINFORCED_STICK.get(), "Reinforced Stick");
		add(ItemInit.STEEL_INGOT.get(), "Steel Ingot");
		add(ItemInit.STEEL_NUGGET.get(), "Steel Nugget");
		add(ItemInit.PROCESSOR.get(), "Processor");
		add(ItemInit.SILICON.get(), "Silicon");
		add(ItemInit.TRANSISTOR.get(), "Transistor");

		// Traits
		add(PlanetTraitInit.WATER_WORLD, "Water World");
		add(PlanetTraitInit.CONTINENTAL, "Continental");
		add(PlanetTraitInit.MOUNTAINOUS, "Mountainous");
		add(PlanetTraitInit.HILLY, "Hilly");
		add(PlanetTraitInit.FLAT, "Flat");
		add(PlanetTraitInit.LAKES, "Lakes");
		add(PlanetTraitInit.FROZEN, "Frozen");
		add(PlanetTraitInit.ISLANDS, "Islands");

		// Item Groups
		addItemGroup("machinaItemGroup", "Machina");
		
		// Commands
		addCommandFeedback("planet_traits.add_trait.success", "Trait added!");
		addCommandFeedback("planet_traits.add_trait.duplicate", "This planet already has the trait %s!");
		addCommandFeedback("planet_traits.remove_trait.success", "Trait removed!");
		addCommandFeedback("planet_traits.remove_trait.not_existing", "This planet does not have the trait %s!");
		addCommandFeedback("planet_traits.list_traits.success", "This planet has the following traits: \n§6%s");
		addCommandFeedback("planet_traits.list_traits.no_traits", "This planet has no traits!");
		addCommandFeedback("planet_traits.not_planet", "This dimension is not a planet!");
		addCommandArgumentFeedback("planet_trait.invalid", "Invalid Planet Trait: \u00A76%s");

		// GUI
		add("machina.screen.starchart.title", "Starchart");
		add("machina.container.ship_console", "Ship Console");
		
		// Tooltips
		add("machina.cargo_crate.open", "Right Click to Loot");
		add("machina.ship_console.missing", "Missing Items");
		add("machina.ship_console.craft_ready", "Craft Ready");
		add("machina.ship_console.crafting", "Crafting...");
		add("machina.ship_console.craft", "Craft");
		add("machina.ship_component.unidentified", "Unidentified");
		add("machina.ship_component.reactor", "Reactor");
		add("machina.ship_component.core", "Core");
		add("machina.ship_component.thrusters", "Thrusters");
		add("machina.ship_component.shields", "Shields");
		add("machina.ship_component.life_support", "Life Support");
		add("machina.scanner.title", "Scanner");
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
}
