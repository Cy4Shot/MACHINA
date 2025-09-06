package com.machina.api.cap.sided;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.IMachinaStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.BiFunction;

public class MultiSidedStorage<T> extends SidedStorage {

    private final LazyOptional<IMachinaStorage>[] sidedStorage;

    @SuppressWarnings("unchecked")
    public MultiSidedStorage(String tag, MachinaBlockEntity be,
                             BiFunction<Direction, MachinaBlockEntity, IMachinaStorage> factory, Side[] sides) {
        super(tag, be, sides);
        this.sidedStorage = new LazyOptional[Direction.values().length];
        for (Direction dir : Direction.values()) {
            this.sidedStorage[dir.ordinal()] = LazyOptional.of(() -> factory.apply(dir, be));
        }
    }

    public void invalidate() {
        for (LazyOptional<IMachinaStorage> store : this.sidedStorage) {
            store.invalidate();
        }
    }

    public LazyOptional<T> getLazy(Direction dir) {
        return this.sidedStorage[dir.ordinal()].cast();
    }

    public boolean isOutput(Direction dir) {
        return this.modes[dir.ordinal()] == Side.OUTPUT;
    }

    @Override
    public void save(CompoundTag tag) {
        for (Direction dir : Direction.values()) {
            this.sidedStorage[dir.ordinal()].ifPresent(store -> tag.put("storage_" + dir.ordinal(), store.serialize()));
        }
    }

    @Override
    protected void load(CompoundTag tag) {
        for (Direction dir : Direction.values()) {
            if (tag.contains("storage_" + dir.ordinal())) {
                this.sidedStorage[dir.ordinal()]
                        .ifPresent(store -> store.deserialize(tag.getCompound("storage_" + dir.ordinal())));
            }
        }
    }
}
