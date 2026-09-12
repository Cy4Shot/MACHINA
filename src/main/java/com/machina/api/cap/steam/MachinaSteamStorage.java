package com.machina.api.cap.steam;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.IMachinaStorage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

/**
 * Block entity storage for steam.
 *
 * @author Cy4
 * @since Machina v0.3.0
 */
public record MachinaSteamStorage(Direction facing, MachinaBlockEntity be) implements ISteamHandler, IMachinaStorage {

    @Override
    public int receiveSteam(int toReceive, boolean simulate) {
        return be.receiveSteam(facing, toReceive, simulate);
    }

    @Override
    public int extractSteam(int toExtract, boolean simulate) {
        return be.extractSteam(facing, toExtract, simulate);
    }

    @Override
    public boolean canExtract() {
        return be.canExtractSteam(facing);
    }

    @Override
    public boolean canReceive() {
        return be.canReceiveSteam(facing);
    }

    @Override
    public CompoundTag serialize() {
        return new CompoundTag();
    }

    @Override
    public void deserialize(CompoundTag nbt) {

    }
}
