package com.machina.api.starchart.burke;

/**
 * This 'interface' is used strictly as a holding tank for constants which are
 * used across multiple classes. An alternative method would be to make a
 * PhysicalConstants class of which these constants are static members; however,
 * defining them in an interface allows <em>unqualified</em> access by any class
 * which implements it.
 * <p>
 * Some of these constants are not completely/correctly documented; if you spot
 * any changes that should be made, please notify the author.
 * <p>
 * <strong><center>Copyright information</center></strong>
 * </p>
 * <p>
 * This Java class is copyright 1998 by Carl Burke. All rights reserved.
 * Substantial sections of this code were previously distributed in different
 * form as part of 'starform' (copyright 1989 Matthew Burdick)
 * <p>
 * <p>
 * This software is provided absolutely free and without warranty, including but
 * not limited to the implied warranties of merchantability and fitness for a
 * purpose. You may use this code for any legal purpose provided that you do not
 * charge for it; this implies that you <em>may</em> use this code as a
 * component of a commercial system as long as the additional functionality of
 * the commercial system is greater than what this code provides and that the
 * commercial system is not primarily intended as a simulation of solar system
 * formation. In other words, if you want to write a science-fiction computer
 * game that uses the code in this package to build objects which are used in
 * the game, that's great and permitted; if you use this code to make a kickass
 * solar-system-builder, you are not allowed to distribute that software except
 * for free.
 * <p>
 * You are allowed and encouraged to modify this software, provided that this
 * copyright notice remains intact. This notice may be reformatted, but not
 * removed.
 * <p>
 * If you do use this software, I and the contributing authors listed under
 * "Acknowledgements" would appreciate some recognition. If you make changes, I
 * would appreciate it if you would pass those changes back to me for possible
 * inclusion in the master. At the time this notice was prepared, my email
 * address is <a href="mailto:cburke@mitre.org">cburke@mitre.org</a> and the
 * home page for this software is <a href=
 * "http://www.geocities.com/Area51/6902/w_accr.html">http://www.geocities.com/Area51/6902/w_accr.html</a>.
 * <a name="ack"><strong><center>Acknowledgements</center></strong></a>
 * <p>
 * Matt Burdick, the author of 'starform' (freeware copyright 1989); much of the
 * code (particularly planetary environments) was adapted from this.
 * </p>
 * <p>
 * Andrew Folkins, the author of 'accretion' (public domain) for the Amiga; I
 * used chunks of his code when creating my displays.
 * </p>
 * <p>
 * Ed Taychert of <a href="http://www.irony.com/">Irony Games</a>, for the
 * algorithm he uses to classify terrestrial planets in his tabular CGI
 * implementation of 'starform'.
 * </p>
 * <p>
 * Paul Schlyter, who provided information about
 * <a href="http://spitfire.ausys.se/psr/comp/ppcomp.html">computing planetary
 * positions</a>.
 * </p>
 */
public interface PhysicalConstants {
	double PI = (3.1415926536);
	double RADIANS_PER_ROTATION = (2.0 * PI);
	double DEG_TO_RAD = (PI / 180.0);
	double RAD_TO_DEG = (180.0 / PI);
	double ANGSTROM = 1.0E-8;
	double G = 6.67E-08; // gravitational constant
	double MEARTH = 6.0E+24; // mass of Earth in kilograms
	double BK = 1.38E-16; // Boltzmann constant
	double MH = 1.673E-24; // mass of hydrogen atom
	double H2 = 2.016;
	double H2O = 18.016;
	double N2 = 28.016;
	double O2 = 32.0;
	double CO2 = 44.011;

	double CHANGE_IN_EARTH_ANG_VEL = (-1.3E-15); // Units of radians/sec/year
	double SOLAR_MASS_IN_GRAMS = (1.989E33); // Units of grams
	double EARTH_MASS_IN_GRAMS = (5.977E27); // Units of grams
	double EARTH_RADIUS = (6.378E8); // Units of cm
	double EARTH_DENSITY = (5.52); // Units of g/cc
	double KM_EARTH_RADIUS = (6378.0); // Units of km
	double EARTH_ACCELERATION = (981.0); // Units of cm/sec2
	double EARTH_AXIAL_TILT = (23.4); // Units of degrees
	double EARTH_EXOSPHERE_TEMP = (1273.0); // Units of degrees Kelvin
	double SUN_MASS_IN_EARTH_MASSES = (332775.64);
	double EARTH_EFFECTIVE_TEMP = (255.0); // Units of degrees Kelvin
	double EARTH_ALBEDO = (0.3);
	double CLOUD_COVERAGE_FACTOR = (1.839E-8); // Km2/kg
	double EARTH_WATER_MASS_PER_AREA = (3.83E15);// grams per square km
	double EARTH_SURF_PRES_IN_MILLIBARS = (1000.0);
	double EARTH_CONVECTION_FACTOR = (0.43); // from Hart, eq.20
	double FREEZING_POINT_OF_WATER = (273.0); // Units of degrees Kelvin
	double DAYS_IN_A_YEAR = (365.256); // Earth days per Earth year
	double GAS_RETENTION_THRESHOLD = (5.0); // ratio of esc vel to RMS vel (was 6.0)
	double GAS_GIANT_ALBEDO = (0.5); // albedo of a gas giant
	double CLOUD_ALBEDO = (0.52);
	double ROCKY_AIRLESS_ALBEDO = (0.07);
	double ROCKY_ALBEDO = (0.15);
	double WATER_ALBEDO = (0.04);
	double AIRLESS_ICE_ALBEDO = (0.5);
	double ICE_ALBEDO = (0.7);
	double SECONDS_PER_HOUR = (3600.0);
	double CM_PER_AU = (1.495978707E13);// number of cm in an AU
	double CM_PER_KM = (1.0E5); // number of cm in a km
	double KM_PER_AU = (CM_PER_AU / CM_PER_KM);
	double CM_PER_METER = (100.0);
	double MILLIBARS_PER_BAR = (1000.0);
	double KELVIN_CELCIUS_DIFFERENCE = (273.0);
	double GRAV_CONSTANT = (6.672E-8); // units of dyne cm2/gram2
	double GREENHOUSE_EFFECT_CONST = (0.93); // affects inner radius..
	double MOLAR_GAS_CONST = (8314.41); // units: g*m2/(sec2*K*mol)
	double K = (50.0); // K = gas/dust ratio
	double B = (1.2E-5); // Used in Crit_mass calc
	double DUST_DENSITY_COEFF = (2.0E-3); // A in Dole's paper
	double ALPHA = (5.0); // Used in density calcs
	double N = (3.0); // Used in density calcs
	double J = (1.46E-19); // Used in day-length calcs (cm2/sec2 g)
	double INCREDIBLY_LARGE_NUMBER = (9.9999E37);
	double ECCENTRICITY_COEFF = (0.077);

	// Now for a few molecular weights (used for RMS velocity calcs):
	// This table is from Dole's book "Habitable Planets for Man", p. 38

	double ATOMIC_HYDROGEN = (1.0); // H
	double MOL_HYDROGEN = (2.0); // H2
	double HELIUM = (4.0); // He
	double ATOMIC_NITROGEN = (14.0); // N
	double ATOMIC_OXYGEN = (16.0); // O
	double METHANE = (16.0); // CH4
	double AMMONIA = (17.0); // NH3
	double WATER_VAPOR = (18.0); // H2O
	double NEON = (20.2); // Ne
	double MOL_NITROGEN = (28.0); // N2
	double CARBON_MONOXIDE = (28.0); // CO
	double NITRIC_OXIDE = (30.0); // NO
	double MOL_OXYGEN = (32.0); // O2
	double HYDROGEN_SULPHIDE = (34.1); // H2S
	double ARGON = (39.9); // Ar
	double CARBON_DIOXIDE = (44.0); // CO2
	double NITROUS_OXIDE = (44.0); // N2O
	double NITROGEN_DIOXIDE = (46.0); // NO2
	double OZONE = (48.0); // O3
	double SULPH_DIOXIDE = (64.1); // SO2
	double SULPH_TRIOXIDE = (80.1); // SO3
	double KRYPTON = (83.8); // Kr
	double XENON = (131.3); // Xe

	// The following constants are used in the kothari_radius function

	double A1_20 = (6.485E12); // All units are in cgs system.
	double A2_20 = (4.0032E-8); // ie: cm, g, dynes, etc.
	double BETA_20 = (5.71E12);

	// The following values are used in determining the fraction of a planet
	// covered with clouds in function cloud_fraction

	double Q1_36 = (1.258E19); // grams
	double Q2_36 = (0.0698); // 1/Kelvin
}
