package com.machina.api.starchart;

import java.util.ArrayList;
import java.util.List;

import com.machina.api.starchart.burke.AccreteObject;
import com.machina.api.starchart.burke.StarSystem;
import com.machina.api.starchart.obj.Moon;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.starchart.obj.Star;
import com.mojang.datafixers.util.Pair;

public class StarchartGenerator {

	public static SolarSystem getOrCreateSystem(long seed, String name) {
		AccreteObject.cr.setSeed(seed);
		StarSystem ss = new StarSystem();
		Star star = convertStar(name, ss.primary);

		List<Planet> planets = new ArrayList<>();
		if (ss.planets != null) {
			Pair<Planet, com.machina.api.starchart.burke.Planet> curr = Pair.of(null, ss.planets);
			int i = 0;
			while (curr.getSecond() != null) {
				i++;
				curr = convertPlanet(name + " " + String.valueOf(i), curr.getSecond());
				planets.add(curr.getFirst());
			}
		}

		SolarSystem sol = new SolarSystem(seed, name + " System", star, planets);
//		System.out.println(sol);
		return sol;
	}

	private static Star convertStar(String name, com.machina.api.starchart.burke.Star sp) {
		return new Star(name, sp.classCode(), sp.VM, sp.LUM, sp.SM, sp.main_seq_life, sp.age, sp.radius, sp.r_ecosphere,
				sp.r_greenhouse);
	}

	private static Pair<Planet, com.machina.api.starchart.burke.Planet> convertPlanet(String name,
			com.machina.api.starchart.burke.Planet p) {

		List<Moon> moons = new ArrayList<>();
		if (p.first_moon != null) {
			Pair<Moon, com.machina.api.starchart.burke.Planet> curr = Pair.of(null, p.first_moon);
			int i = 0;
			while (curr.getSecond() != null) {
				i++;
				curr = convertMoon(name + "." + String.valueOf(i), curr.getSecond());
				moons.add(curr.getFirst());
			}
		}

		Planet planet = new Planet(name, p.a, p.e, p.where_in_orbit, p.mass, p.gas_giant, p.orbit_zone, p.radius,
				p.density, p.orb_period, p.day, p.resonant_period, p.axial_tilt, p.esc_velocity, p.surf_accel,
				p.surf_grav, p.rms_velocity, p.molec_weight, p.volatile_gas_inventory, p.GH2, p.GH2O, p.GN2, p.GO2,
				p.GCO2, p.surf_pressure, p.greenhouse_effect, p.boil_point, p.albedo, p.surf_temp, p.min_temp,
				p.max_temp, p.avg_temp, p.hydrosphere, p.cloud_cover, p.ice_cover, p.plan_class, p.r_ecosphere,
				p.resonance, p.stell_mass_ratio, p.age, p.cloud_factor, p.water_factor, p.rock_factor,
				p.airless_rock_factor, p.ice_factor, p.airless_ice_factor, p.its, p.temp_unstable, moons);
		return Pair.of(planet, p.next_planet);
	}

	private static Pair<Moon, com.machina.api.starchart.burke.Planet> convertMoon(String name,
			com.machina.api.starchart.burke.Planet p) {
		Moon moon = new Moon(name, p.a, p.e, p.where_in_orbit, p.mass, p.gas_giant, p.orbit_zone, p.radius, p.density,
				p.orb_period, p.day, p.resonant_period, p.axial_tilt, p.esc_velocity, p.surf_accel, p.surf_grav,
				p.rms_velocity, p.molec_weight, p.volatile_gas_inventory, p.GH2, p.GH2O, p.GN2, p.GO2, p.GCO2,
				p.surf_pressure, p.greenhouse_effect, p.boil_point, p.albedo, p.surf_temp, p.min_temp, p.max_temp,
				p.avg_temp, p.hydrosphere, p.cloud_cover, p.ice_cover, p.plan_class, p.r_ecosphere, p.resonance,
				p.stell_mass_ratio, p.age, p.cloud_factor, p.water_factor, p.rock_factor, p.airless_rock_factor,
				p.ice_factor, p.airless_ice_factor, p.its, p.temp_unstable);
		return Pair.of(moon, p.next_planet);
	}
}
