package com.machina.api.cap.fluid;

import java.util.function.Predicate;

import com.machina.api.network.s2c.S2CFluidEntitySync;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;

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
    public CompoundTag writeToNBT(Provider lookupProvider, CompoundTag nbt) {
        CompoundTag comp = new CompoundTag();
        super.writeToNBT(lookupProvider, comp);
        nbt.put("MachinaTank" + id, comp);
        return nbt;
    }

    @Override
    public FluidTank readFromNBT(Provider lookupProvider, CompoundTag nbt) {
        return super.readFromNBT(lookupProvider, nbt.getCompound("MachinaTank" + id));
    }

    @Override
    protected void onContentsChanged() {
        if (entity.level() != null && !entity.level().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) entity.level(), entity.chunkPosition(),
                    new S2CFluidEntitySync(entity.getId(), getFluid(), id));
        }
        onChanged.run();
    }
}
