package com.machina.api.cap.fluid;

import java.util.function.Predicate;

import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CFluidEntitySync;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MachinaEntityTank extends FluidTank {

    protected final Runnable onChanged;

    private final Entity entity;
    public final int id;

    public MachinaEntityTank(Entity entity, int capacity, Predicate<FluidStack> validator, int id, Runnable onChanged) {
        super(capacity, validator);
        this.entity = entity;
        this.id = id;
        this.setFluid(new FluidStack(Fluids.EMPTY, 0));
        this.onChanged = onChanged;
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        CompoundTag comp = new CompoundTag();
        super.writeToNBT(comp);
        nbt.put("MachinaTank" + id, comp);
        return nbt;
    }

    @Override
    public FluidTank readFromNBT(CompoundTag nbt) {
        return super.readFromNBT(nbt.getCompound("MachinaTank" + id));
    }

    @Override
    protected void onContentsChanged() {
        if (entity.level() != null && !entity.level().isClientSide()) {
            PacketSender.sendToClients(new S2CFluidEntitySync(entity.getId(), getFluid(), id));
        }
        onChanged.run();
    }
}
