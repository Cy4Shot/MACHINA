package com.machina.capability.inventory;

import com.machina.capability.ICustomStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class MachinaItemStorage extends ItemStackHandler implements ICustomStorage {
	
	private Runnable onChanged;
	
	public MachinaItemStorage() {
		super();
	}

	public MachinaItemStorage(int size) {
		super(size);
	}

	public MachinaItemStorage(NonNullList<ItemStack> stacks) {
		super(stacks);
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
}
