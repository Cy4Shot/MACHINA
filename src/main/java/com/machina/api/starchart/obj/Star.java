package com.machina.api.starchart.obj;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public record Star(String name, String stellarClass, double absolute_magnitude, double luminosity, double stellar_mass,
		double main_seq_life, double age, double radius, double r_ecosphere, double r_greenhouse) {
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}
}