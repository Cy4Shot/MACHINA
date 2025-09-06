package com.machina.api.util;

import com.machina.registration.init.FluidInit;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public record ChemicalConstants(FlowingFluid fluid, float molec_mass, float melting, float boiling) {

    // @formatter:off
	public static final ChemicalConstants WATER = new ChemicalConstants(Fluids.WATER, 18.01528f, 273.15F, 373.15F);
	public static final ChemicalConstants AMMONIA = new ChemicalConstants(FluidInit.AMMONIA.fluid(), 17.031f, 195.4F, 239.81F);
	public static final ChemicalConstants METHANE = new ChemicalConstants(FluidInit.METHANE.fluid(), 16.04f, 90.694F, 111.6F);
	public static final ChemicalConstants CARBON_DISULPHIDE = new ChemicalConstants(FluidInit.CARBON_DISULPHIDE.fluid(), 76.13f, 161.54F, 319.49F);
	public static final ChemicalConstants HYDROGEN_SULPHIDE = new ChemicalConstants(FluidInit.HYDROGEN_SULPHIDE.fluid(), 34.08f, 187.7F, 213.6F);
	public static final ChemicalConstants SULPHURIC_ACID = new ChemicalConstants(FluidInit.SULPHURIC_ACID.fluid(), 98.079f, 283.46F, 573F);
	
	public static final ChemicalConstants LAVA = new ChemicalConstants(Fluids.LAVA, 60.02f, 600F, 1600000F);
	//@formatter:on

    public static final ChemicalConstants[] OCEANIC = new ChemicalConstants[]{WATER, AMMONIA, METHANE, SULPHURIC_ACID,
            CARBON_DISULPHIDE, HYDROGEN_SULPHIDE};

    public enum FluidTempState {
        SOLID,
        LIQUID,
        GAS
    }

    public FluidTempState fluidState(double temp) {
        if (temp < melting()) {
            return FluidTempState.SOLID;
        }
        if (temp > boiling()) {
            return FluidTempState.GAS;
        }
        return FluidTempState.LIQUID;
    }

    public FluidState state() {
        return fluid().defaultFluidState();
    }

}
