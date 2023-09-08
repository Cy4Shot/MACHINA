package com.machina.api.starchart.obj;

import java.util.List;
import java.util.Random;

import com.machina.api.starchart.AtmosphericComposition;
import com.machina.registration.init.FluidInit;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

/**
 * Mass: 10^20 kg</br>
 * Radius: m</br>
 * Orbital, SemiMajor Axis: 10^6 m</br>
 * Orbital Speed: m/s</br>
 * Eccentricity (no unit)</br>
 * Gravity: m/s^2</br>
 * Temperature, Surface: K</br>
 * Atmospheric Pressure: atm</br>
 * 
 * @author Cy4Shot
 * @since Machina v0.1.0
 */
public record Planet(String name, float mass, float radius, float orbital, float orbitalSpeed, float eccentricity,
		float gravity, float temperature, float atmosphericPressure, AtmosphericComposition atmosphericComposition,
		List<Moon> moons) {

	public static final Planet MERCURY = new Planet("Mercury", 3301.1f, 2439700, 57.91f, 47360, 0.205630f, 3.7f, 340, 0,
			AtmosphericComposition.NONE, List.of());
	public static final Planet VENUS = new Planet("Venus", 48675, 6051800, 108.21f, 35020, 0.006772f, 8.87f, 737, 92,
			new AtmosphericComposition(
			//@formatter:off
					Pair.of(FluidInit.CARBON_DIOXIDE.stack(), 96500f),
					Pair.of(FluidInit.NITROGEN.stack(), 3500f),
					Pair.of(FluidInit.SULPHUR_DIOXIDE.stack(), 15f),
					Pair.of(FluidInit.ARGON.stack(), 7f),
					Pair.of(new FluidStack(Fluids.WATER, 1000), 2f),
					Pair.of(FluidInit.CARBON_MONOXIDE.stack(), 1.7f),
					Pair.of(FluidInit.HELIUM.stack(), 1.2f)
			//@formatter:on
			), List.of());
	public static final Planet EARTH = new Planet("Earth", 59721.68f, 6371000, 149598.023f, 29782.7f, 0.0167086f,
			9.80665f, 287.76f, 1, new AtmosphericComposition(
			//@formatter:off
					Pair.of(FluidInit.NITROGEN.stack(), 78.08f),
					Pair.of(FluidInit.OXYGEN.stack(), 20.95f),
					Pair.of(new FluidStack(Fluids.WATER, 1000), 1f),
					Pair.of(FluidInit.ARGON.stack(), 0.9340f),
					Pair.of(FluidInit.CARBON_DIOXIDE.stack(), 0.0413f)
			//@formatter:on
			), List.of(Moon.MOON));
	public static final Planet MARS = new Planet("Mars", 6417.1f, 3389500, 227939.366f, 24070, 0.0934f, 3.72076f, 213,
			0.00628f, new AtmosphericComposition(
			//@formatter:off
					Pair.of(FluidInit.CARBON_DIOXIDE.stack(), 9597f),
					Pair.of(FluidInit.ARGON.stack(), 193f),
					Pair.of(FluidInit.NITROGEN.stack(), 189f),
					Pair.of(FluidInit.OXYGEN.stack(), 15f),
					Pair.of(FluidInit.CARBON_MONOXIDE.stack(), 6f),
					Pair.of(new FluidStack(Fluids.WATER, 1000), 2f)
			//@formatter:on
			), List.of(Moon.PHOBOS, Moon.DEIMOS));

	public static final Planet JUPITER = new Planet("Jupiter", 18982000, 69911000, 778479, 13070, 0.0489f, 24.79f, 165,
			1f, new AtmosphericComposition(
			//@formatter:off
				Pair.of(FluidInit.HYDROGEN.stack(), 89f),
				Pair.of(FluidInit.HELIUM.stack(), 10f),
				Pair.of(FluidInit.METHANE.stack(), 0.3f),
				Pair.of(FluidInit.AMMONIA.stack(), 0.026f)
			//@formatter:on
			), List.of(Moon.IO, Moon.EUROPA, Moon.GANYMEDE, Moon.CALLISTO));
	public static final Planet SATURN = new Planet("Saturn", 5683400, 58232000, 1433530, 9680, 0.0565f, 10.44f, 134, 1f,
			new AtmosphericComposition(
			//@formatter:off
				Pair.of(FluidInit.HYDROGEN.stack(), 96.3f),
				Pair.of(FluidInit.HELIUM.stack(), 3.25f),
				Pair.of(FluidInit.METHANE.stack(), 0.45f),
				Pair.of(FluidInit.AMMONIA.stack(), 0.0125f)	
			//@formatter:on
			), List.of(Moon.MIMAS, Moon.ENCELADUS, Moon.TETHYS, Moon.DIONE, Moon.RHEA, Moon.TITAN, Moon.HYPERION,
					Moon.IAPETUS, Moon.PHOEBE));
	public static final Planet URANUS = new Planet("Uranus", 868100, 25362000, 2870972, 6800, 0.04717f, 8.69f, 76, 1f,
			new AtmosphericComposition(
			//@formatter:off
				Pair.of(FluidInit.HYDROGEN.stack(), 83f),
				Pair.of(FluidInit.HELIUM.stack(), 15f),
				Pair.of(FluidInit.METHANE.stack(), 2.3f)
			//@formatter:on
			), List.of(Moon.ARIEL, Moon.UMBRIEL, Moon.TITANIA, Moon.OBERON, Moon.MIRANDA));
	public static final Planet NEPTUNE = new Planet("Neptune", 1024130, 24622000, 4500000, 5430, 0.008678f, 11.15f, 72,
			1f, new AtmosphericComposition(
			//@formatter:off
				Pair.of(FluidInit.HYDROGEN.stack(), 80f),
				Pair.of(FluidInit.HELIUM.stack(), 19f),
				Pair.of(FluidInit.METHANE.stack(), 1.5f)
			//@formatter:on
			), List.of(Moon.TRITON));

	public static Planet gen(Random rand) {
		return new Planet("", 0, 0, 0, 0, 0, 0, 0, 0, AtmosphericComposition.NONE, List.of());
	}
}
