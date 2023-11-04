package com.machina.api.starchart.obj;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Mass, Stellar Mass</br>
 * Luminosity</br>
 * Life, years</br>
 * Age, years</br>
 * Habitable Ecosphere, AU</br>
 * 
 * @author Cy4Shot
 */

public record Star(String name, String stellarClass, double absolute_magnitude, double luminosity, double stellar_mass,
		double main_seq_life, double age, double radius, double r_ecosphere, double r_greenhouse) {
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}

//	public static final Star SUN = new Star("Sol", 1D, 1D, 5_000_000_000D, 5_000_000_000D, 1D);
}