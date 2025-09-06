package com.machina.api.cap.sided;

import com.machina.api.block.entity.MachinaBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public abstract class SidedStorage implements ISideAdapter {

    public final Side[] modes;
    protected final String tag;
    protected final MachinaBlockEntity e;

    public SidedStorage(String tag, MachinaBlockEntity e, Side[] d) {
        this.tag = tag;
        this.e = e;
        this.modes = d;
    }

    public boolean isNonNullMode(Direction dir) {
        return this.modes[dir.ordinal()] != Side.NONE;
    }

    public boolean isInput(Direction from) {
        return this.modes[from.ordinal()] == Side.INPUT;
    }

    @Override
    public Side get(Direction d) {
        return this.modes[d.ordinal()];
    }

    @Override
    public void cycle(Direction dir) {
        Side.cycle(modes, dir, e, tag);
    }

    public abstract void invalidate();

    protected abstract void save(CompoundTag tag);

    protected abstract void load(CompoundTag tag);

    public void saveAdditional(CompoundTag tag) {
        CompoundTag storageTag = new CompoundTag();
        this.save(storageTag);
        tag.put(this.tag, storageTag);
        tag.putByteArray("sides_" + this.tag, getRawSideData());
    }

    public void loadAdditional(CompoundTag tag2) {
        if (tag2.contains(this.tag)) {
            CompoundTag storageTag = tag2.getCompound(this.tag);
            this.load(storageTag);
        }
        if (tag2.contains("sides_" + this.tag)) {
            setRawSideData(tag2.getByteArray("sides_" + this.tag));
        }
    }

    public void setRawSideData(byte[] side_data) {
        Side.fromRaw(modes, side_data);
    }

    public byte[] getRawSideData() {
        return Side.getRaw(modes);
    }
}
