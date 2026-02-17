package com.machina.api.cap.fluid;

import com.machina.api.network.s2c.S2CFluidSync;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Predicate;

public class MachinaTank extends FluidTank {

    protected final Runnable onChanged;

    private final BlockEntity tile;
    public final int id;

    public MachinaTank(BlockEntity tile, int capacity, Predicate<FluidStack> validator, int id, Runnable onChanged) {
        super(capacity, validator);
        this.tile = tile;
        this.id = id;
        this.setFluid(new FluidStack(Fluids.EMPTY, 0));
        this.onChanged = onChanged;
    }

    @Override
    public CompoundTag writeToNBT(Provider provider, CompoundTag nbt) {
        CompoundTag comp = new CompoundTag();
        super.writeToNBT(provider, comp);
        nbt.put("MachinaTank" + id, comp);
        return nbt;
    }

    @Override
    public FluidTank readFromNBT(Provider provider, CompoundTag nbt) {
        return super.readFromNBT(provider, nbt.getCompound("MachinaTank" + id));
    }

    @Override
    protected void onContentsChanged() {
        if (tile.getLevel() != null && !tile.getLevel().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) tile.getLevel(),
                    new ChunkPos(tile.getBlockPos()), new S2CFluidSync(tile.getBlockPos(), getFluid(), id));
        }
        onChanged.run();
    }
}
