package com.machina.api.starchart.obj;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.machina.api.starchart.StarchartConst;

import net.minecraft.client.gui.GuiGraphics;

public record Star(String name, String stellarClass, double absolute_magnitude, double luminosity, double stellar_mass,
		double main_seq_life, double age, double radius, double r_ecosphere, double r_greenhouse) implements Celestial {

	@Override
	public double radiusAU() {
		return this.radius * StarchartConst.STELLAR_RADIUS_TO_AU;
	}

	@Override
	public Orbit orbit() {
		return Orbit.STAR;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}

	@Override
	public double a() {
		return 0;
	}

	@Override
	public double e() {
		return 0;
	}

	@Override
	public double orb_period() {
		return 0;
	}

	@Override
	public double where_in_orbit() {
		return 0;
	}

	@Override
	public void drawIcon(GuiGraphics gui, int x, int y, float alpha) {
	}
}