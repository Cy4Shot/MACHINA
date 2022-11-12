package com.machina.datagen.client.lang;

import com.machina.Machina;
import com.machina.registration.Registration;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.KeyBindingsInit;
import com.machina.registration.init.SoundInit;

import net.minecraft.data.DataGenerator;

public class EnUsLangProvider extends BaseLangProvider {

	public EnUsLangProvider(DataGenerator gen) {
		super(gen, "en_us", Machina.MOD_ID);
		
		// Generic Music Disc Translation.
		this.music_disc = "Music Disc";
	}

	@Override
	protected void addTranslations() {

		// Blocks
		add(BlockInit.SHIP_CONSOLE.get(), "Ship Console");
		add(BlockInit.ATMOSPHERIC_SEPARATOR.get(), "Atmospheric Separator");
		add(BlockInit.TEMPERATURE_REGULATOR.get(), "Temperature Regulator");
		add(BlockInit.CARGO_CRATE.get(), "Cargo Crate");
		add(BlockInit.COMPONENT_ANALYZER.get(), "Component Analyzer");
		add(BlockInit.PUZZLE_BLOCK.get(), "Puzzle Block");
		add(BlockInit.PRESSURIZED_CHAMBER.get(), "Pressurized Chamber");
		add(BlockInit.CABLE.get(), "Cable");
		add(BlockInit.TANK.get(), "Tank");
		add(BlockInit.BATTERY.get(), "Battery");
		add(BlockInit.CREATIVE_BATTERY.get(), "Creative Battery");
		add(BlockInit.STEEL_BLOCK.get(), "Steel Block");
		add(BlockInit.ALUMINUM_BLOCK.get(), "Aluminum Block");
		add(BlockInit.ALUMINUM_ORE.get(), "Aluminum Ore");
		add(BlockInit.COPPER_BLOCK.get(), "Copper Block");
		add(BlockInit.COPPER_ORE.get(), "Copper Ore");
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
		add(BlockInit.REINFORCED_TILE.get(), "Reinforced Tile");
		add(BlockInit.FUEL_STORAGE_UNIT.get(), "Rocket Fuel Container");
		add(BlockInit.FLUID_HOPPER.get(), "Fluid Hopper");
		add(BlockInit.FURNACE_GENERATOR.get(), "Furnace Generator");
		add(BlockInit.STATE_CONVERTER.get(), "State Converter");

		// Items
		add(ItemInit.SHIP_COMPONENT.get(), "Ship Component");
		add(ItemInit.SCANNER.get(), "Scanner");
		add(ItemInit.REINFORCED_STICK.get(), "Reinforced Stick");
		add(ItemInit.STEEL_INGOT.get(), "Steel Ingot");
		add(ItemInit.STEEL_NUGGET.get(), "Steel Nugget");
		add(ItemInit.ALUMINUM_INGOT.get(), "Aluminum Ingot");
		add(ItemInit.ALUMINUM_NUGGET.get(), "Aluminum Nugget");
		add(ItemInit.COPPER_INGOT.get(), "Copper Ingot");
		add(ItemInit.COPPER_NUGGET.get(), "Copper Nugget");
		add(ItemInit.COPPER_COIL.get(), "Copper Coil");
		add(ItemInit.IRON_CATALYST.get(), "Iron Catalyst");
		add(ItemInit.PROCESSOR.get(), "Processor");
		add(ItemInit.SILICON.get(), "Silicon");
		add(ItemInit.SILICON_BOLUS.get(), "Silicon Bolus");
		add(ItemInit.HIGH_PURITY_SILICON.get(), "High Purity Silicon");
		add(ItemInit.TRANSISTOR.get(), "Transistor");
		add(ItemInit.AMMONIUM_NITRATE.get(), "Ammonium Nitrate");

		// Music Discs
		addMusicDisc(ItemInit.BEYOND_DISC.get(), "Cy4 - Beyond (Machina OST)");
		addMusicDisc(ItemInit.LOOK_UP_DISC.get(), "Cy4 - Look Up (Machina OST)");
		addMusicDisc(ItemInit.BOSS_01_DISC.get(), "Dan Johansen - Overlord Of Chaos Pt.1");
		addMusicDisc(ItemInit.BOSS_02_DISC.get(), "Dan Johansen - Overlord Of Chaos Pt.2");
		addMusicDisc(ItemInit.BOSS_03_DISC.get(), "Dan Johansen - Overlord Of Chaos Pt.3");

		// Fluids
		add(FluidInit.OXYGEN, "Oxygen", "Bucket");
		add(FluidInit.NITROGEN, "Nitrogen", "Bucket");
		add(FluidInit.AMMONIA, "Ammonia", "Bucket");
		add(FluidInit.CARBON_DIOXIDE, "Carbon Dioxide", "Bucket");
		add(FluidInit.HYDROGEN, "Hydrogen", "Bucket");
		add(FluidInit.LIQUID_HYDROGEN, "Liquid Hydrogen", "Bucket");
		add(FluidInit.LIQUID_AMMONIA, "Liquid Ammonia", "Bucket");
		add(FluidInit.ETHANE, "Ethane", "Bucket");
		add(FluidInit.PROPANE, "Propane", "Bucket");
		add(FluidInit.ETHYLENE, "Ethylene", "Bucket");
		add(FluidInit.PROPYLENE, "Propylene", "Bucket");

		// Traits

		// Attributes
		add(AttributeInit.ATMOSPHERIC_PRESSURE, "Atmospheric Pressure");
		add(AttributeInit.BASE_BLOCKS, "Blocks");
		add(AttributeInit.CAVE_CHANCE, "Cave Density");
		add(AttributeInit.CAVE_LENGTH, "Cave Length");
		add(AttributeInit.CAVE_THICKNESS, "Cave Thickness");
		add(AttributeInit.CAVES_EXIST, "Caves");
		add(AttributeInit.FLUID_BLOCKS, "Fluids");
		add(AttributeInit.FOG_DENSITY, "Fog Density");
		add(AttributeInit.GRAVITY, "Gravity");
		add(AttributeInit.ISLAND_DENSITY, "Island Density");
		add(AttributeInit.PALETTE, "Color Palettte");
		add(AttributeInit.PLANET_NAME, "Planet Name");
		add(AttributeInit.PLANET_ICON, "Planet Icon");
		add(AttributeInit.SURF_BLOCKS, "Surface Blocks");
		add(AttributeInit.SURFACE_SCALE, "Surface Scale");
		add(AttributeInit.SURFACE_DETAIL, "Surface Detail");
		add(AttributeInit.SURFACE_ROUGHNESS, "Surface Roughness");
		add(AttributeInit.SURFACE_DISTORTION, "Surface Distortion");
		add(AttributeInit.SURFACE_SHAPE, "Surface Shape");
		add(AttributeInit.SURFACE_MODIFIER, "Surface Modifier");
		add(AttributeInit.TEMPERATURE, "Temperature");

		// Sounds
		add(SoundInit.ROCKET_LAUNCH, "Rocket Launch");
		add(SoundInit.BEYOND, "Cy4 - Beyond (MACHINA OST)");
		add(SoundInit.LOOK_UP, "Cy4 - Look Up (MACHINA OST)");
		add(SoundInit.BOSS_01, "Dan Johansen - Overlord Of Chaos Pt. 1");
		add(SoundInit.BOSS_02, "Dan Johansen - Overlord Of Chaos Pt. 2");
		add(SoundInit.BOSS_03, "Dan Johansen - Overlord Of Chaos Pt. 3");

		// Item Groups
		addItemGroup(Registration.MAIN_GROUP, "Machina");
		addItemGroup(Registration.WORLDGEN_GROUP, "Machina: Worldgen");

		// Keybindings
		addKeyCategory("machina", "Machina");
		add(KeyBindingsInit.DEV_SCREEN, "Dev Screen");
		add(KeyBindingsInit.STARCHART, "Open Starchart");

		// Commands
		addCommandFeedback("planet_traits.add_trait.success", "Trait added!");
		addCommandFeedback("planet_traits.add_trait.duplicate", "This planet already has the trait %s!");
		addCommandFeedback("planet_traits.remove_trait.success", "Trait removed!");
		addCommandFeedback("planet_traits.remove_trait.not_existing", "This planet does not have the trait %s!");
		addCommandFeedback("planet_traits.list_traits.success", "This planet has the following traits: \n§6%s");
		addCommandFeedback("planet_traits.list_traits.no_traits", "This planet has no traits!");
		addCommandFeedback("planet_traits.not_planet", "This dimension is not a planet!");
		addCommandArgumentFeedback("planet_trait.invalid", "Invalid Planet Trait: \u00A76%s");

		// Ship Component
		addShipComponent("unidentified", "Unidentified");
		addShipComponent("reactor", "Reactor");
		addShipComponent("core", "Core");
		addShipComponent("thrusters", "Thrusters");
		addShipComponent("shields", "Shields");
		addShipComponent("life_support", "Life Support");

		// GUI
		addScreen("vlc", "warning1", "WARNING: You are playing Machina on MacOS / Linux.");
		addScreen("vlc", "warning2", "In order for some features to run correctly, you must install VLC.");
		addScreen("vlc", "warning3", "Make sure to install VLC 4.0.");
		addScreen("vlc", "link", "Install VLC 4.0");
		addScreen("vlc", "title", "I have installed VLC");
		addScreen("component_analyzer", "insert", "Insert Component");
		addScreen("cargo_crate", "open", "Right Click to Loot");
		addScreen("starchart", "sel", "Select");
		addScreen("starchart", "rot", "Rotate");
		addScreen("starchart", "pan", "Pan");
		addScreen("starchart", "zoo", "Zoom");
		addScreen("starchart", "cls", "Close");
		addScreen("scanner", "title", "Scanner");
		addScreen("scanner", "tab0", "1. Planet Traits");
		addScreen("scanner", "tab1", "2. Planet Attributes");
		addScreen("scanner", "tab2", "3. Cave Structures");
		addScreen("scanner", "tab3", "4. Atmosphere Composition");
		addScreen("scanner", "tab4", "5. Terrain Information");
		addScreen("scanner", "location", "Location: ");
		addScreen("scanner", "nodata", "Data Unavailable.");
		addScreen("scanner", "nocave", "There are no caves on this planet!");
		addScreen("ship_console", "missing", "Missing Items");
		addScreen("ship_console", "obstructed", "Obstructed");
		addScreen("ship_console", "craft_ready", "Craft Ready");
		addScreen("ship_console", "crafting", "Crafting...");
		addScreen("ship_console", "craft", "Craft");
		addScreen("ship_console", "stage", "Stage");
		addScreen("ship_console", "await", "Awaiting Launch");
		addScreen("atmospheric_seperator", "producing", "Producing: ");
		addScreen("atmospheric_seperator", "no", "Not Producing ");
		addScreen("atmospheric_seperator", "not_enough", "Fluid Too Thin");
		addScreen("tank", "stored", "Stored: ");
		addScreen("tank", "none", "None ");
		addScreen("pressurized_chamber", "clear", "Clear");
		addScreen("pressurized_chamber", "start", "Start");
		addScreen("pressurized_chamber", "pause", "Pause");
		addScreen("pressurized_chamber", "waiting", "Awaiting input...");
		addScreen("pressurized_chamber", "ready", "Clear to Craft");
		addScreen("pressurized_chamber", "crafting", "In Progress...");
		addScreen("pressurized_chamber", "no_recipe", "No Recipe Found");
		addScreen("pressurized_chamber", "heat", "Not Enough Heat");
		addScreen("pressurized_chamber", "stored", "Stored: ");
		addScreen("pressurized_chamber", "none", "None ");
		addScreen("temperature_regulator", "stored", "Current Temperature: ");
		addScreen("fuel_storage", "stored", "Current Temperature: ");
		addScreen("fuel_storage", "depleting", "WARNING: DEPLETING");
		addScreen("state_converter", "stored", "Current Temperature: ");
		addScreen("state_converter", "none", "None");
		addScreen("go_beyond", "title", "MACHINA - Alpha 0.1");
		addScreen("go_beyond", "desc1", "Travelling between planets isn't yet available!");
		addScreen("go_beyond", "desc2", "This feature will be introduced in Alpha 0.2.");

		// Terminal
		addTerminalCommand("clear", "Clears the console.");
		addTerminalCommand("help", "Shows a list of commands.");
		addTerminalFeedback("help", "prompt", "Type 'help' to see a list of commands.");
		addTerminalFeedback("help", "list", "The following is a list of commands:");
		addTerminalFeedback("help", "unrecognised", "Unrecognised command: ");
		addTerminalCommand("neofetch", "Displays info about the OS.");
		addTerminalFeedback("neofetch", "ship_os", "OS: ShipOS CyberSec Edition");
		addTerminalFeedback("neofetch", "ship_cpu", "CPU: SM-908172U @ 64.0 MHz");
		addTerminalFeedback("neofetch", "ship_status", "Status: LISTENING");
		addTerminalFeedback("neofetch", "star_os", "OS: StarOS - Your Journey Awaits.");
		addTerminalFeedback("neofetch", "star_cpu", "CPU: LC-00012S @ 1.7 GHz");
		addTerminalFeedback("neofetch", "star_status", "Status: IDLE");
		addTerminalCommand("unlock", "Lifts cargo security.");
		addTerminalFeedback("unlock", "already_complete", "Cargo security already lifted.");
		addTerminalFeedback("unlock", "verification_needed", "Security verification needed.");
		addTerminalFeedback("unlock", "translate", "Translate: ");
		addTerminalFeedback("unlock", "incorrect", "Incorrect.");
		addTerminalFeedback("unlock", "permission_granted", "Permission granted.");
		addTerminalCommand("destination", "Set the destination.");
		addTerminalFeedback("destination", "loading", "Initializing Starchart...");
		addTerminalFeedback("destination", "set", "Destination set: ");
		addTerminalFeedback("destination", "calculating", "Calculating Required Fuel...");
		addTerminalFeedback("destination", "required", "Required Materials:");
		addTerminalFeedback("destination", "water", "Water: ");
		addTerminalFeedback("destination", "aluminium", "Aluminium: ");
		addTerminalFeedback("destination", "ammonium_nitrate", "Ammonium Nitrate: ");
		addTerminalCommand("fuel", "Check fuel status.");
		addTerminalFeedback("fuel", "stored", "Fuel stored: ");
		addTerminalFeedback("fuel", "water", "Water: ");
		addTerminalFeedback("fuel", "aluminium", "Aluminium: ");
		addTerminalFeedback("fuel", "ammonium_nitrate", "Ammonium Nitrate: ");
		addTerminalCommand("refuel", "Refuel the ship.");
		addTerminalFeedback("refuel", "info", "Place fuel barrel adjacent to the console.");
		addTerminalFeedback("refuel", "await", "Press ENTER to confirm.");
		addTerminalFeedback("refuel", "progress", "Refuelling...");
		addTerminalFeedback("refuel", "complete", "Refuel complete!");
		addTerminalFeedback("refuel", "gain", "Gained: ");
		addTerminalFeedback("refuel", "water", "Water: ");
		addTerminalFeedback("refuel", "aluminium", "Aluminium: ");
		addTerminalFeedback("refuel", "ammonium_nitrate", "Ammonium Nitrate: ");
		addTerminalCommand("launch", "Launch the ship.");
		addTerminalFeedback("launch", "unset", "Please set a destination.");
		addTerminalFeedback("launch", "dest", "Destination: ");
		addTerminalFeedback("launch", "dist", "Distance: ");
		addTerminalFeedback("launch", "required", "Required Materials:");
		addTerminalFeedback("launch", "water", "Water: ");
		addTerminalFeedback("launch", "aluminium", "Aluminium: ");
		addTerminalFeedback("launch", "ammonium_nitrate", "Ammonium Nitrate: ");
		addTerminalFeedback("launch", "await", "Press ENTER to continue.");
		addTerminalFeedback("launch", "load_booting", "Booting systems...");
		addTerminalFeedback("launch", "load_booting_1", "OS: MachOS - GO BEYOND.");
		addTerminalFeedback("launch", "load_booting_2", "CPU: CY4-22.08-209147125 @ 7.71GHz");
		addTerminalFeedback("launch", "load_booting_3", "MEMORY: 92GB / 512GB @ 5600MHz");
		addTerminalFeedback("launch", "load_booting_4", "VERSION: GB-8.12");
		addTerminalFeedback("launch", "load_booting_5", "STATUS: BOOTING");
		addTerminalFeedback("launch", "load_reactor", "Spooling reactor...");
		addTerminalFeedback("launch", "load_reactor_1", "Reactor spooled!");
		addTerminalFeedback("launch", "load_reactor_2", "TEMP: 367.34K");
		addTerminalFeedback("launch", "load_reactor_3", "PRESSURE: 287.01kPa");
		addTerminalFeedback("launch", "load_reactor_4", "STATUS: READY");
		addTerminalFeedback("launch", "load_thrusters", "Heating thrusters...");
		addTerminalFeedback("launch", "load_thrusters_1", "Thrusters heated!");
		addTerminalFeedback("launch", "load_thrusters_2", "TEMP: 390.21K");
		addTerminalFeedback("launch", "load_thrusters_3", "STATUS: READY");
		addTerminalFeedback("launch", "load_navigation", "Initializing navigation...");
		addTerminalFeedback("launch", "load_navigation_1", "> Aquired destination.");
		addTerminalFeedback("launch", "load_navigation_2", "> Route established.");
		addTerminalFeedback("launch", "load_navigation_3", "> Fuel requirements calculated.");
		addTerminalFeedback("launch", "load_navigation_4", "> All obstacles accounted for.");
		addTerminalFeedback("launch", "load_comms", "Establishing comms link...");
		addTerminalFeedback("launch", "load_comms_1", "Comms link esablished.");
		addTerminalFeedback("launch", "load_comms_2", "STATUS: LISTENING ON IP:");
		addTerminalFeedback("launch", "load_comms_3", "9e2563950fd265ff805fa1a086411f06");
		addTerminalFeedback("launch", "load_comms_4", "CONNECTED TO COMMS NETWORK");
		addTerminalFeedback("launch", "load_comms_5", "CONNECTIONS: 2984");
		addTerminalFeedback("launch", "load_preparing", "Preparing for launch...");
		addTerminalFeedback("launch", "load_preparing_1", "Core status.. OK");
		addTerminalFeedback("launch", "load_preparing_2", "Reactor status.. OK");
		addTerminalFeedback("launch", "load_preparing_3", "Engine status.. OK");
		addTerminalFeedback("launch", "load_preparing_4", "Life Support status.. OK");
		addTerminalFeedback("launch", "load_preparing_5", "Shields status.. OK");
		addTerminalFeedback("launch", "load_preparing_6", "Navigation status.. OK");
		addTerminalFeedback("launch", "load_preparing_7", "Other systems.. OK");
		addTerminalFeedback("launch", "load_preparing_8", "ALL SYSTEMS READY FOR LAUNCH");
		addTerminalFeedback("launch", "confirm", "Enter CONFIRM to confirm launch.");
		addTerminalFeedback("launch", "warning", "WARNING: THIS IS IRREVERSIBLE.");
		addTerminalFeedback("launch", "seated", "Please wait for rocket door to open.");
		addTerminalFeedback("launch", "init", "Life support initializing...");
	}
}
