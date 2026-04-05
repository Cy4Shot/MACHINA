package com.machina.registration.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.machina.Machina;
import com.machina.api.starchart.planet_trait.PlanetOreTrait;
import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.api.starchart.planet_trait.PlanetTraitConstraint;
import com.machina.api.starchart.planet_type.PlanetType.PlanetTraitSettings;
import com.machina.api.starchart.planet_type.PlanetType.PlanetTraitSettingsEntry;
import com.machina.registration.init.BlockInit.MachinaOre;

import net.neoforged.neoforge.registries.DeferredRegister;

public class PlanetTraitInit {
	public static final DeferredRegister<PlanetTrait> TRAITS = DeferredRegister.create(RegistryInit.TRAIT,
			Machina.MOD_ID);
	public static final List<PlanetTraitConstraint> CONSTRAINTS = new ArrayList<>();
	public static final Map<Supplier<PlanetOreTrait>, Integer> ORE_POOL = new HashMap<>();

	//@formatter:off
	public static final Supplier<PlanetTrait> ALWAYS_RAINING = create("always_raining", 0x1c4ed6);

	public static final Supplier<PlanetOreTrait> RICH_COAL = ore("rich_coal", 0x2b2b2b, BlockInit.COAL_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_IRON = ore("rich_iron", 0xd8af93, BlockInit.IRON_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_COPPER = ore("rich_copper", 0xc87533, BlockInit.COPPER_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_GOLD = ore("rich_gold", 0xffd700, BlockInit.GOLD_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_REDSTONE = ore("rich_redstone", 0xff0000, BlockInit.REDSTONE_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_QUARTZ = ore("rich_quartz", 0xf5f5f5, BlockInit.NETHER_QUARTZ_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_EMERALD = ore("rich_emerald", 0x50c878, BlockInit.EMERALD_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_LAPIS = ore("rich_lapis", 0x26619c, BlockInit.LAPIS_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_DIAMOND = ore("rich_diamond", 0x4fe2e2, BlockInit.DIAMOND_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_ALUMINUM = ore("rich_aluminum", 0xbcc6cc, BlockInit.ALUMINUM_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_NICKEL = ore("rich_nickel", 0xa8a9ad, BlockInit.NICKEL_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_TIN = ore("rich_tin", 0xdcdcdc, BlockInit.TIN_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_ZINC = ore("rich_zinc", 0x7f8c8d, BlockInit.ZINC_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_LOW_GRADE_TITANIUM = ore("rich_low_grade_titanium", 0x8a8a8a, BlockInit.LOW_GRADE_TITANIUM_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_LEAD = ore("rich_lead", 0x4a4a4a, BlockInit.LEAD_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_BORON = ore("rich_boron", 0x9acd32, BlockInit.BORON_ORE, 2);
	public static final Supplier<PlanetOreTrait> RICH_PALLADIUM = ore("rich_palladium", 0xc0c0c0, BlockInit.PALLADIUM_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_SILVER = ore("rich_silver", 0xcfd3d4, BlockInit.SILVER_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_FLUORITE = ore("rich_fluorite", 0x7fffd4, BlockInit.FLUORITE_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_SALTPETER = ore("rich_saltpeter", 0xf0e68c, BlockInit.SALTPETER_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_PYRITE = ore("rich_pyrite", 0xffff00, BlockInit.PYRITE_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_BISMUTH = ore("rich_bismuth", 0x9b59b6, BlockInit.BISMUTH_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_MAGNETITE = ore("rich_magnetite", 0x1c1c1c, BlockInit.MAGNETITE_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_GYPSUM = ore("rich_gypsum", 0xf8f8ff, BlockInit.GYPSUM_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_PERCHLORATE = ore("rich_perchlorate", 0xffa07a, BlockInit.PERCHLORATE_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_ILMENITE = ore("rich_ilmenite", 0x5c5c5c, BlockInit.ILMENITE_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_PLATINUM = ore("rich_platinum", 0xe5e4e2, BlockInit.PLATINUM_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_IRIDIUM = ore("rich_iridium", 0xe6e6fa, BlockInit.IRIDIUM_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_OSMIUM = ore("rich_osmium", 0x6e7f80, BlockInit.OSMIUM_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_COBALT = ore("rich_cobalt", 0x0047ab, BlockInit.COBALT_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_URANINITE = ore("rich_uraninite", 0x556b2f, BlockInit.URANINITE_ORE, 1);
	public static final Supplier<PlanetOreTrait> RICH_THORIUM = ore("rich_thorium", 0x708238, BlockInit.THORIUM_ORE, 1);

	@SuppressWarnings("unchecked")
	PlanetTraitConstraint WEATHER_CONSTRAINT = constrain(ALWAYS_RAINING);
	//@formatter:on

	private static final Supplier<PlanetTrait> create(String name, int color) {
		return TRAITS.register(name, () -> new PlanetTrait(name, color));
	}

	private static final Supplier<PlanetOreTrait> ore(String name, int color, MachinaOre ore, int weight) {
		Supplier<PlanetOreTrait> trait = TRAITS.register(name, () -> new PlanetOreTrait(name, color, ore));
		ORE_POOL.put(trait, weight);
		return trait;

	}

	@SuppressWarnings("unchecked")
	private static final PlanetTraitConstraint constrain(Supplier<PlanetTrait>... traits) {
		PlanetTraitConstraint constraint = new PlanetTraitConstraint(
				Stream.of(traits).map(Supplier::get).collect(Collectors.toSet()));
		CONSTRAINTS.add(constraint);
		return constraint;
	}

	public static final PlanetTraitSettings getOreConfig() {
		return new PlanetTraitSettings(0, 32, ORE_POOL.entrySet().stream()
				.map(e -> new PlanetTraitSettingsEntry(e.getKey().get(), e.getValue())).collect(Collectors.toList()));
	}
}
