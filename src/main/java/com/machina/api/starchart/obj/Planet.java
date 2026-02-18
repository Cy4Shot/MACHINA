package com.machina.api.starchart.obj;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.machina.api.client.screen.MUI;
import com.machina.api.fluid.ChemicalFluid;
import com.machina.api.starchart.StarchartConst;
import com.machina.api.starchart.planet_type.PlanetType;
import com.machina.api.starchart.planet_type.PlanetTypeLoader;
import com.machina.api.util.MachinaRL;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public record Planet(String name, ResourceLocation planet_type, int icon_variant, double a, // semi-major axis of the
																							// orbit (in AU)
		double e, // eccentricity of the orbit
		double where_in_orbit, // position along orbit (in radians)
		double mass, // mass (in Earth masses)
		boolean gas_giant, // true if the planet is gassy
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
		double volatile_gas_inventory, double GH2, double GH2O, double GN2, double GO2, double GCO2,
		// gas retention
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
		double airless_ice_factor, int its, boolean temp_unstable, ChemicalFluid dominant_liquid,
		boolean dominant_liquid_frozen, List<Moon> moons) implements Celestial {

	public Component getName() {
		return Component.literal(name).withStyle(Style.EMPTY.withBold(true));
	}

	@Override
	public double radiusAU() {
		return this.radius * StarchartConst.KM_TO_AU;
	}

	@Override
	public Orbit orbit() {
		return Orbit.from(this);
	}

	@Override
	public String texture_fg() {
		return "clouds";
	}

	@Override
	public String texture_bg() {
		return "planet_" + switch (plan_class) {
		case 'M' -> "terrestrial";
		case 'V' -> "venal";
		case 'J' -> "jovian";
		case 'I' -> "ice";
		case 'R' -> "rocky";
		case 'G' -> "garden";
		default -> "rocky";
		};
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}

	public PlanetType type() {
		return PlanetTypeLoader.INSTANCE.get(planet_type);
	}

	public double calculateAphelionDistance() {
		return a * (1 + e);
	}

	public boolean hasClouds() {
		return this.cloud_cover > 0.1f;
	}

	// Extra Props

	public boolean hasGenLiquid() {
		return dominant_liquid != null;
	}

	public boolean isFluidFrozen() {
		return dominant_liquid_frozen;
	}

	public BlockState getDominantLiquidBodyBlock() {
		return dominant_liquid == null ? null : dominant_liquid.fluid().defaultFluidState().createLegacyBlock();
	}

	public boolean doesRain() {
		return hydrosphere > 0;
	}

	public boolean breathable() {
		return GO2 >= 19; // Gross oversimplification, but its a game!
	}

	private static final ResourceLocation PLANETS = MachinaRL.create("textures/gui/starchart/planets.png");

	@Override
	public void drawIcon(GuiGraphics gui, int x, int y, float alpha) {
		int tx = this.icon_variant * 16;
		int ty = this.type().iconY() * 16;
		MUI.drawWithScale(gui, 0.5f, trans -> {
			MUI.drawWithAlpha(alpha, () -> {
				gui.blit(PLANETS, trans.apply((float) x).intValue() - 8, trans.apply((float) y).intValue() - 8, tx, ty,
						16, 16, 512, 512);
			});
		});
	}
}