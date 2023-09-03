package com.machina.capability.item;

import java.util.function.BiPredicate;

import com.machina.capability.ICustomStorage;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MachinaItemStorage extends ItemStackHandler implements ICustomStorage {

	private Runnable onChanged;
	private BiPredicate<Integer, ItemStack> allow = (i, s) -> true;

	public MachinaItemStorage() {
		super();
	}

	public MachinaItemStorage(int size) {
		super(size);
	}

	public MachinaItemStorage(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	public MachinaItemStorage(int size, BiPredicate<Integer, ItemStack> allow) {
		super(size);
		this.allow = allow;
	}

	public MachinaItemStorage(NonNullList<ItemStack> stacks, BiPredicate<Integer, ItemStack> allow) {
		super(stacks);
		this.allow = allow;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return allow.test(slot, stack);
	}

	@Override
	protected void onContentsChanged(int slot) {
		onChanged.run();
	}

	@Override
	public void setChanged(Runnable runnable) {
		this.onChanged = runnable;
	}

	@Override
	public String getTag() {
		return "inv";
	}

	public NonNullList<ItemStack> items() {
		return this.stacks;
	}

	public void clear() {
		this.stacks.clear();
	}

	@Override
	public CompoundTag serialize() {
		return this.serializeNBT();
	}

	@Override
	public void deserialize(CompoundTag nbt) {
		this.deserializeNBT(nbt);
	}
}