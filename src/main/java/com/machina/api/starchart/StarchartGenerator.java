package com.machina.api.starchart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.machina.api.fluid.ChemicalFluid;
import com.machina.api.fluid.FluidPhase;
import com.machina.api.starchart.burke.AccreteObject;
import com.machina.api.starchart.burke.BPlanet;
import com.machina.api.starchart.burke.BStar;
import com.machina.api.starchart.burke.StarSystem;
import com.machina.api.starchart.name.SystemNameGenerator;
import com.machina.api.starchart.obj.Moon;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.starchart.obj.Star;
import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.api.starchart.planet_trait.PlanetTraitConstraint;
import com.machina.api.starchart.planet_type.PlanetType.PlanetTraitSettings;
import com.machina.api.starchart.planet_type.PlanetType.PlanetTraitSettingsEntry;
import com.machina.api.starchart.planet_type.PlanetTypeLoader;
import com.machina.api.util.math.RomanNumber;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.PlanetTraitInit;
import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;

public class StarchartGenerator {

	public static final FluidObject[] OCEANIC = new FluidObject[] { FluidInit.AMMONIA, FluidInit.METHANE,
			FluidInit.SULPHUR_DIOXIDE, FluidInit.SULPHURIC_ACID, FluidInit.CARBON_DIOXIDE,
			FluidInit.HYDROGEN_SULPHIDE };

	public static SolarSystem gen(long seed) {
		Random rand = new Random(seed);
		String name = new SystemNameGenerator().gen(rand);
		AccreteObject.cr.setSeed(seed);
		StarSystem ss = new StarSystem();
		Star star = convertStar(name, ss.primary);

		List<Planet> planets = new ArrayList<>();
		if (ss.planets != null) {
			Pair<Planet, BPlanet> curr = Pair.of(null, ss.planets);
			int i = 0;
			while (curr.getSecond() != null) {
				i++;
				curr = convertPlanet(name + " " + RomanNumber.toRoman(i), curr.getSecond(), rand, i);
				planets.add(curr.getFirst());
			}
		}

		return new SolarSystem(seed, name + " System", star, planets);
	}

	private static ChemicalFluid getDominantLiquidChemical(BPlanet p, Random rand) {
		if (p.water_factor > 0) {
			return ChemicalFluid.WATER;
		}

		List<FluidObject> filtered = new ArrayList<>();
		for (FluidObject obj : OCEANIC) {
			if (obj.chem().getPhase(p.surf_temp, p.surf_pressure * 100d).equals(FluidPhase.LIQUID)) {
				filtered.add(obj);
			}
		}
		if (!filtered.isEmpty()) {
			FluidObject sel = filtered.get(rand.nextInt(0, filtered.size()));
			return new ChemicalFluid(sel.chem(), sel.fluid());
		}

		return null;
	}

	private static Star convertStar(String name, BStar sp) {
		return new Star(name, sp.classCode(), sp.VM, sp.LUM, sp.SM, sp.main_seq_life, sp.age, sp.radius, sp.r_ecosphere,
				sp.r_greenhouse);
	}

	private static Pair<Planet, BPlanet> convertPlanet(String name, BPlanet p, Random rand, int id) {

		int icon_variant = rand.nextInt(6);

		List<Moon> moons = new ArrayList<>();
		if (p.first_moon != null) {
			Pair<Moon, BPlanet> curr = Pair.of(null, p.first_moon);
			int i = 0;
			while (curr.getSecond() != null) {
				i++;
				curr = convertMoon(name + "." + i, curr.getSecond());
				moons.add(curr.getFirst());
			}
		}

		ResourceLocation type = PlanetTypeLoader.INSTANCE.pickRandom(rand);

		ChemicalFluid sea = getDominantLiquidChemical(p, rand);
		boolean frozen_sea = false;
		if (sea != null) {
			frozen_sea = sea.chem().getPhase(p.surf_temp, p.surf_pressure * 100d).equals(FluidPhase.SOLID);
		}

		Set<PlanetTrait> traits = new HashSet<>();
		traits.addAll(pickTraits(rand, PlanetTypeLoader.INSTANCE.get(type).traits(), PlanetTraitInit.CONSTRAINTS));
		traits.addAll(pickTraits(rand, PlanetTraitInit.getOreConfig(), List.of())); // Ore traits come from static pool

		Planet planet = new Planet(name, type, traits, icon_variant, p.a, p.e, p.where_in_orbit, p.mass, p.gas_giant,
				p.orbit_zone, p.radius, p.density, p.orb_period, p.day, p.resonant_period, p.axial_tilt, p.esc_velocity,
				p.surf_accel, p.surf_grav, p.rms_velocity, p.molec_weight, p.volatile_gas_inventory, p.GH2, p.GH2O,
				p.GN2, p.GO2, p.GCO2, p.surf_pressure, p.greenhouse_effect, p.boil_point, p.albedo, p.surf_temp,
				p.min_temp, p.max_temp, p.avg_temp, p.hydrosphere, p.cloud_cover, p.ice_cover, p.plan_class,
				p.r_ecosphere, p.resonance, p.stell_mass_ratio, p.age, p.cloud_factor, p.water_factor, p.rock_factor,
				p.airless_rock_factor, p.ice_factor, p.airless_ice_factor, p.its, p.temp_unstable, sea, frozen_sea,
				moons, id);
		return Pair.of(planet, p.next_planet);
	}

	private static Pair<Moon, BPlanet> convertMoon(String name, BPlanet p) {
		Moon moon = new Moon(name, p.a, p.e, p.where_in_orbit, p.mass, p.gas_giant, p.orbit_zone, p.radius, p.density,
				p.orb_period, p.day, p.resonant_period, p.axial_tilt, p.esc_velocity, p.surf_accel, p.surf_grav,
				p.rms_velocity, p.molec_weight, p.volatile_gas_inventory, p.GH2, p.GH2O, p.GN2, p.GO2, p.GCO2,
				p.surf_pressure, p.greenhouse_effect, p.boil_point, p.albedo, p.surf_temp, p.min_temp, p.max_temp,
				p.avg_temp, p.hydrosphere, p.cloud_cover, p.ice_cover, p.plan_class, p.r_ecosphere, p.resonance,
				p.stell_mass_ratio, p.age, p.cloud_factor, p.water_factor, p.rock_factor, p.airless_rock_factor,
				p.ice_factor, p.airless_ice_factor, p.its, p.temp_unstable);
		return Pair.of(moon, p.next_planet);
	}

	private static List<PlanetTrait> pickTraits(Random random, PlanetTraitSettings config,
			List<PlanetTraitConstraint> constraints) {
		int numRolls = config.minRolls() + random.nextInt(config.maxRolls() - config.minRolls() + 1);
		List<PlanetTrait> selectedTraits = new ArrayList<>();
		List<PlanetTraitSettingsEntry> availableTraits = new ArrayList<>(config.weights());
		availableTraits.sort(Comparator.comparing(t -> t.trait().toString()));

		while (selectedTraits.size() < numRolls && !availableTraits.isEmpty()) {
			double totalWeight = 0.0;
			for (PlanetTraitSettingsEntry t : availableTraits) {
				totalWeight += t.weight();
			}
			double r = random.nextDouble() * totalWeight;
			double cumulative = 0.0;

			PlanetTraitSettingsEntry chosen = null;
			for (PlanetTraitSettingsEntry t : availableTraits) {
				cumulative += t.weight();
				if (r <= cumulative) {
					chosen = t;
					break;
				}
			}

			if (chosen == null)
				break;

			PlanetTrait chosenTrait = chosen.trait();

			if (!selectedTraits.contains(chosenTrait)) {
				selectedTraits.add(chosenTrait);
			}
			availableTraits.remove(chosen);

			Iterator<PlanetTraitSettingsEntry> it = availableTraits.iterator();
			while (it.hasNext()) {
				PlanetTraitSettingsEntry t = it.next();

				List<PlanetTrait> testList = new ArrayList<>(selectedTraits);
				testList.add(t.trait());

				boolean violates = false;
				for (PlanetTraitConstraint c : constraints) {
					if (c.violates(new HashSet<>(testList))) {
						violates = true;
						break;
					}
				}

				if (violates) {
					it.remove();
				}
			}
		}

		return selectedTraits;
	}

}