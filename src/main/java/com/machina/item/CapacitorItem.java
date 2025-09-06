package com.machina.item;

import java.util.function.Supplier;

import com.machina.api.item.EnergyItem;

import net.minecraftforge.common.ForgeConfigSpec;

public class CapacitorItem extends EnergyItem {

    private final Supplier<ForgeConfigSpec.IntValue> capacity;

    public CapacitorItem(Properties props, Supplier<ForgeConfigSpec.IntValue> capacity) {
        super(props);
        this.capacity = capacity;
    }

    @Override
    public int getMaxEnergy() {
        return this.capacity.get().get();
    }
}