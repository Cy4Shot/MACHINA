package com.machina.item;

import net.minecraft.world.item.Item;

public class MouldItem extends Item {

    public enum Mould {
        BASE, PLATE, ROD, WIRE
    }

    private final Mould mould;

    public MouldItem(Properties props, Mould mould) {
        super(props);
        this.mould = mould;
    }

    public Mould getMould() {
        return this.mould;
    }
}
