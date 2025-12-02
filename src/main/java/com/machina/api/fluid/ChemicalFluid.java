package com.machina.api.fluid;

import com.machina.registration.init.FluidInit.Chemical;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public record ChemicalFluid(Chemical chem, Fluid fluid) {
    public static final ChemicalFluid WATER = new ChemicalFluid(Chemical.WATER, Fluids.WATER);

}
