package com.machina.api.starchart.obj;

import java.util.Random;

import com.machina.api.starchart.AtmosphericComposition;
import com.machina.registration.init.FluidInit;
import com.mojang.datafixers.util.Pair;

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
public record Moon(String name, float mass, float radius, float orbital, float orbitalSpeed, float eccentricity,
		float gravity, float temperature, float atmosphericPressure, AtmosphericComposition atmosphericComposition) {

	public static final Moon MOON = new Moon("Moon", 734.2f, 1737400, 384.399f, 1022, 0.0549f, 1.622f, 250, 0,
			AtmosphericComposition.NONE);
	public static final Moon PHOBOS = new Moon("Phobos", 0.00010659f, 11266, 9.376f, 2138, 0.0151f, 0.0057f, 269, 0,
			AtmosphericComposition.NONE);
	public static final Moon DEIMOS = new Moon("Deimos", 0.000014762f, 6200, 23.4632f, 1351, 0.00033f, 0.003f, 233, 0,
			AtmosphericComposition.NONE);
	public static final Moon IO = new Moon("Io", 893.1938f, 1821600, 421.7f, 17334, 0.0040313019f, 1.796502844f, 110, 0,
			AtmosphericComposition.NONE);
	public static final Moon EUROPA = new Moon("Europa", 479.9844f, 1560800, 670.9f, 13743.36f, 0.009f, 1.314f, 102, 0,
			AtmosphericComposition.NONE);
	public static final Moon GANYMEDE = new Moon("Ganymede", 1481.9f, 2634100, 1070.4f, 10880, 0.0013f, 1.428f, 110, 0,
			AtmosphericComposition.NONE);
	public static final Moon CALLISTO = new Moon("Callisto", 1075.938f, 2410300, 1882.7f, 8204, 0.0074f, 1.235f, 134, 0,
			AtmosphericComposition.NONE);
	public static final Moon MIMAS = new Moon("Mimas", 0.375094f, 198200, 185.539f, 14280, 0.0196f, 0.064f, 64, 0,
			AtmosphericComposition.NONE);
	public static final Moon ENCELADUS = new Moon("Enceladus", 1.080318f, 252100, 237.948f, 12640, 0.0047f, 0.113f, 75,
			0, AtmosphericComposition.NONE);
	public static final Moon TETHYS = new Moon("Tethys", 6.174959f, 531100, 294.619f, 11350, 0.0001f, 0.146f, 86, 0,
			AtmosphericComposition.NONE);
	public static final Moon DIONE = new Moon("Dione", 10.954868f, 561400, 377.396f, 10030, 0.0022f, 0.232f, 87, 0,
			AtmosphericComposition.NONE);
	public static final Moon RHEA = new Moon("Rhea", 23.064854f, 763500, 527.108f, 8480, 0.0012583f, 0.264f, 76, 0,
			AtmosphericComposition.NONE);
	public static final Moon TITAN = new Moon("Titan", 1345.2f, 2574730, 1221.87f, 5570, 0.0288f, 1.352f, 93.7f, 1.45f,
			new AtmosphericComposition(
			//@formatter:off
					Pair.of(FluidInit.NITROGEN.stack(), 98.4f),
					Pair.of(FluidInit.METHANE.stack(), 1.4f),
					Pair.of(FluidInit.HYDROGEN.stack(), 0.2f)
			//@formatter:on
			));
	public static final Moon HYPERION = new Moon("Hyperion", 0.055510f, 135000, 1481.009f, 5070, 0.1230061f, 0.019f, 93,
			0, AtmosphericComposition.NONE);
	public static final Moon IAPETUS = new Moon("Iapetus", 18.056591f, 734400, 3560.82f, 3260, 0.0276812f, 0.223f, 100,
			0, AtmosphericComposition.NONE);
	public static final Moon PHOEBE = new Moon("Phoebe", 0.083123f, 106500, 12960, -1710, 0.1562415f, 0.044f, 73, 0,
			AtmosphericComposition.NONE);
	public static final Moon ARIEL = new Moon("Ariel", 12.51f, 578900, 191.02f, 5510, 0.0012f, 0.249f, 60, 0,
			AtmosphericComposition.NONE);
	public static final Moon UMBRIEL = new Moon("Umbriel", 12.75f, 584700, 266, 4670, 0.0039f, 0.25f, 75, 0,
			AtmosphericComposition.NONE);
	public static final Moon TITANIA = new Moon("Titania", 34, 788400, 435.91f, 3640, 0.0011f, 0.365f, 70, 0,
			AtmosphericComposition.NONE);
	public static final Moon OBERON = new Moon("Oberon", 30.76f, 761400, 583.52f, 3150, 0.0014f, 0.354f, 75, 0,
			AtmosphericComposition.NONE);
	public static final Moon MIRANDA = new Moon("Miranda", 0.64f, 235800, 129.39f, 6660, 0.0013f, 0.077f, 60, 0,
			AtmosphericComposition.NONE);
	public static final Moon TRITON = new Moon("Triton", 213.9f, 1353400, 354.759f, 4390, 0.000016f, 0.779f, 38, 0,
			AtmosphericComposition.NONE);
	
	public static Moon gen(Random rand) {
		return new Moon("", 0, 0, 0, 0, 0, 0, 0, 0, AtmosphericComposition.NONE);
	}
}
