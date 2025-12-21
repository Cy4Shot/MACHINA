package com.machina.api.cap.fluid;

import net.minecraftforge.fluids.FluidStack;

public interface FluidHandlerEntity {

    MachinaEntityTank getTank(int id);

    default void setFluid(int tank, FluidStack stack) {
        getTank(tank).setFluid(stack);
    }

    default FluidStack getFluid(int id) {
        return getTank(id).getFluid();
    }

    default int getTankCapacity(int tank) {
        return getTank(tank).getCapacity();
    }

    default int getFluidMB(int tank) {
        return getFluid(tank).getAmount();
    }

    default float getFluidF(int tank) {
        int cap = getTankCapacity(tank);
        if (cap == 0) {
            return 0;
        }
        return (float) getFluidMB(tank) / (float) cap;
    }
}
