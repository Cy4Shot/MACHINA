package com.machina.api.blueprint;

import com.machina.api.util.StringUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;

public record Blueprint(String id) {
    public static final Blueprint EMPTY = new Blueprint("empty");

    public String getId() {
        return id;
    }

    public MutableComponent getName() {
        return StringUtils.translateMultiblockComp(id);
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", this.id);
        return nbt;
    }

    public static Blueprint fromNBT(CompoundTag nbt) {
        return new Blueprint(nbt.getString("id"));
    }
}
