package com.machina.api.cap.sided;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.IMachinaStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import java.util.function.BiFunction;

public class MultiSidedStorage<T extends IMachinaStorage> extends SidedStorage {

    private final IMachinaStorage[] sidedStorage;

    public MultiSidedStorage(String tag, MachinaBlockEntity be, BiFunction<Direction, MachinaBlockEntity, T> factory,
            Side[] sides) {
        super(tag, be, sides);
        this.sidedStorage = new IMachinaStorage[Direction.values().length];
        for (Direction dir : Direction.values()) {
            this.sidedStorage[dir.ordinal()] = factory.apply(dir, be);
        }
    }

    @SuppressWarnings("unchecked")
    public T getCap(Direction dir) {
        return (T) this.sidedStorage[dir.ordinal()];
    }

    public boolean isOutput(Direction dir) {
        return this.modes[dir.ordinal()] == Side.OUTPUT;
    }

    @Override
    public void save(CompoundTag tag) {
        for (Direction dir : Direction.values()) {
            tag.put("storage_" + dir.ordinal(), this.sidedStorage[dir.ordinal()].serialize());
        }
    }

    @Override
    protected void load(CompoundTag tag) {
        for (Direction dir : Direction.values()) {
            if (tag.contains("storage_" + dir.ordinal())) {
                this.sidedStorage[dir.ordinal()].deserialize(tag.getCompound("storage_" + dir.ordinal()));
            }
        }
    }
}
