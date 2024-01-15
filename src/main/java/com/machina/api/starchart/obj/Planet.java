package com.machina.api.starchart.obj;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import net.minecraft.world.phys.Vec3;

public record Planet(String name, double a, // semi-major axis of the orbit (in AU)
		double e, // eccentricity of the orbit
		double where_in_orbit, // position along orbit (in radians)
		double mass, // mass (in Earth masses)
		boolean gas_giant, // true if the planet is a gas giant
		int orbit_zone, // the 'zone' of the planet
		double radius, // equatorial radius (in km)
		double density, // density (in g/cc)
		double orb_period, // length of the local year (days)
		double day, // length of the local day (hours)
		int resonant_period, // true if in resonant rotation
		int axial_tilt, // units of degrees
		double esc_velocity, // units of cm/sec
		double surf_accel, // units of cm/sec2
		double surf_grav, // units of Earth gravities
		double rms_velocity, // units of cm/sec
		double molec_weight, // smallest molecular weight retained
		double volatile_gas_inventory, double GH2, double GH2O, double GN2, double GO2, double GCO2, // gas retention
																										// percentages
		double surf_pressure, // units of millibars (mb)
		boolean greenhouse_effect, // runaway greenhouse effect?
		double boil_point, // the boiling point of water (Kelvin)
		double albedo, // albedo of the planet
		double surf_temp, // surface temperature in Kelvin
		double min_temp, double max_temp, // surface temperature ranges
		double avg_temp, // weighted average of iterations
		double hydrosphere, // fraction of surface covered
		double cloud_cover, // fraction of surface covered
		double ice_cover, // fraction of surface covered
		char plan_class, // general type classification
		double r_ecosphere, double resonance, double stell_mass_ratio, double age, double cloud_factor,
		double water_factor, double rock_factor, double airless_rock_factor, double ice_factor,
		double airless_ice_factor, int its, boolean temp_unstable, List<Moon> moons) {

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}

	public Vec3 calculateOrbitalCoordinates(double t) {
		double trueAnomaly = calculateTrueAnomaly(t);

		double x = a * (Math.cos(trueAnomaly) - e);
		double z = a * Math.sqrt(1 - e * e) * Math.sin(trueAnomaly);

		return new Vec3(x, 0, z);
	}

	private double calculateTrueAnomaly(double t) {
		// Orbital parameters
		double meanMotion = 2 * Math.PI / orb_period; // Mean motion (radians/day)
		double meanAnomaly = meanMotion * t + where_in_orbit; // Mean anomaly (radians)

		// Eccentricity anomaly
		double eccentricAnomaly = calculateEccentricAnomaly(meanAnomaly);

		// True anomaly
		double trueAnomaly = 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(eccentricAnomaly / 2));

		return trueAnomaly;
	}

	private double calculateEccentricAnomaly(double meanAnomaly) {
		// Initial guess for eccentric anomaly
		double E0 = meanAnomaly;

		// Iterative solution for eccentric anomaly using Newton's method
		double E = E0;
		double tolerance = 1e-9;
		int maxIterations = 1000;
		int iterations = 0;

		do {
			double nextE = E - ((E - e * Math.sin(E) - meanAnomaly) / (1 - e * Math.cos(E)));
			if (Math.abs(nextE - E) < tolerance) {
				E = nextE;
				break;
			}
			E = nextE;
			iterations++;
		} while (iterations < maxIterations);

		return E;
	}

//	public static final Planet MERCURY = new Planet("Mercury", 3301.1f, 2439700, 57.91f, 47360, 0.205630f, 3.7f, 340, 0,
//			AtmosphericComposition.NONE, List.of());
//	public static final Planet VENUS = new Planet("Venus", 48675, 6051800, 108.21f, 35020, 0.006772f, 8.87f, 737, 92,
//			new AtmosphericComposition(
//			//@formatter:off
//					Pair.of(FluidInit.CARBON_DIOXIDE.stack(), 96500d),
//					Pair.of(FluidInit.NITROGEN.stack(), 3500d),
//					Pair.of(FluidInit.SULPHUR_DIOXIDE.stack(), 15d),
//					Pair.of(FluidInit.ARGON.stack(), 7d),
//					Pair.of(new FluidStack(Fluids.WATER, 1000), 2d),
//					Pair.of(FluidInit.CARBON_MONOXIDE.stack(), 1.7d),
//					Pair.of(FluidInit.HELIUM.stack(), 1.2d)
//			//@formatter:on
//			), List.of());
//	public static final Planet EARTH = new Planet("Earth", 59721.68f, 6371000, 149598.023f, 29782.7f, 0.0167086f,
//			9.80665f, 287.76f, 1, new AtmosphericComposition(
//			//@formatter:off
//					Pair.of(FluidInit.NITROGEN.stack(), 78.08d),
//					Pair.of(FluidInit.OXYGEN.stack(), 20.95d),
//					Pair.of(new FluidStack(Fluids.WATER, 1000), 1d),
//					Pair.of(FluidInit.ARGON.stack(), 0.9340d),
//					Pair.of(FluidInit.CARBON_DIOXIDE.stack(), 0.0413d)
//			//@formatter:on
//			), List.of(Moon.MOON));
//	public static final Planet MARS = new Planet("Mars", 6417.1f, 3389500, 227939.366f, 24070, 0.0934f, 3.72076f, 213,
//			0.00628f, new AtmosphericComposition(
//			//@formatter:off
//					Pair.of(FluidInit.CARBON_DIOXIDE.stack(), 9597d),
//					Pair.of(FluidInit.ARGON.stack(), 193d),
//					Pair.of(FluidInit.NITROGEN.stack(), 189d),
//					Pair.of(FluidInit.OXYGEN.stack(), 15d),
//					Pair.of(FluidInit.CARBON_MONOXIDE.stack(), 6d),
//					Pair.of(new FluidStack(Fluids.WATER, 1000), 2d)
//			//@formatter:on
//			), List.of(Moon.PHOBOS, Moon.DEIMOS));
//
//	public static final Planet JUPITER = new Planet("Jupiter", 18982000, 69911000, 778479, 13070, 0.0489f, 24.79f, 165,
//			1f, new AtmosphericComposition(
//			//@formatter:off
//				Pair.of(FluidInit.HYDROGEN.stack(), 89d),
//				Pair.of(FluidInit.HELIUM.stack(), 10d),
//				Pair.of(FluidInit.METHANE.stack(), 0.3d),
//				Pair.of(FluidInit.AMMONIA.stack(), 0.026d)
//			//@formatter:on
//			), List.of(Moon.IO, Moon.EUROPA, Moon.GANYMEDE, Moon.CALLISTO));
//	public static final Planet SATURN = new Planet("Saturn", 5683400, 58232000, 1433530, 9680, 0.0565f, 10.44f, 134, 1f,
//			new AtmosphericComposition(
//			//@formatter:off
//				Pair.of(FluidInit.HYDROGEN.stack(), 96.3d),
//				Pair.of(FluidInit.HELIUM.stack(), 3.25d),
//				Pair.of(FluidInit.METHANE.stack(), 0.45d),
//				Pair.of(FluidInit.AMMONIA.stack(), 0.0125d)	
//			//@formatter:on
//			), List.of(Moon.MIMAS, Moon.ENCELADUS, Moon.TETHYS, Moon.DIONE, Moon.RHEA, Moon.TITAN, Moon.HYPERION,
//					Moon.IAPETUS, Moon.PHOEBE));
//	public static final Planet URANUS = new Planet("Uranus", 868100, 25362000, 2870972, 6800, 0.04717f, 8.69f, 76, 1f,
//			new AtmosphericComposition(
//			//@formatter:off
//				Pair.of(FluidInit.HYDROGEN.stack(), 83d),
//				Pair.of(FluidInit.HELIUM.stack(), 15d),
//				Pair.of(FluidInit.METHANE.stack(), 2.3d)
//			//@formatter:on
//			), List.of(Moon.ARIEL, Moon.UMBRIEL, Moon.TITANIA, Moon.OBERON, Moon.MIRANDA));
//	public static final Planet NEPTUNE = new Planet("Neptune", 1024130, 24622000, 4500000, 5430, 0.008678f, 11.15f, 72,
//			1f, new AtmosphericComposition(
//			//@formatter:off
//				Pair.of(FluidInit.HYDROGEN.stack(), 80d),
//				Pair.of(FluidInit.HELIUM.stack(), 19d),
//				Pair.of(FluidInit.METHANE.stack(), 1.5d)
//			//@formatter:on
//			), List.of(Moon.TRITON));
}
