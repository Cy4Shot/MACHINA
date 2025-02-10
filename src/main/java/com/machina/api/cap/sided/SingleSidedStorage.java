package com.machina.api.cap.sided;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.IMachinaStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;

public class SingleSidedStorage<T> extends SidedStorage {

	private final LazyOptional<IMachinaStorage> singleStorage;

	public SingleSidedStorage(String tag, MachinaBlockEntity be, @NonNull IMachinaStorage storage, Side[] sides) {
		super(tag, be, sides);
		this.singleStorage = LazyOptional.of(() -> storage);
	}

	public void invalidate() {
		singleStorage.invalidate();
	}

	public LazyOptional<T> get() {
		return singleStorage.cast();
	}

	@Override
	protected void save(CompoundTag tag) {
		singleStorage.ifPresent(store -> tag.put(this.tag, store.serialize()));
	}

	@Override
	protected void load(CompoundTag tag) {
		if (tag.contains(this.tag)) {
			singleStorage.ifPresent(store -> store.deserialize(tag.getCompound(this.tag)));
		}
	}
}
