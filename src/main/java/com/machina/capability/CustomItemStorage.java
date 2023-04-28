package com.machina.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class CustomItemStorage extends ItemStackHandler implements ICustomStorage {
	
	private Runnable onChanged;
	
	public CustomItemStorage() {
		super();
	}

	public CustomItemStorage(int size) {
		super(size);
	}

	public CustomItemStorage(NonNullList<ItemStack> stacks) {
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
