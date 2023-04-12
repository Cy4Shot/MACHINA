package com.machina.blueprint;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;

public class Blueprint {

	public static final Blueprint EMPTY = new Blueprint("empty", Items.BARRIER, BlueprintCategory.COMP);

	private final String id;

	private final ItemStack bp;

	private final BlueprintCategory category;

	public Blueprint(String id, IItemProvider in, BlueprintCategory cat) {
		this.id = id;
		this.bp = new ItemStack(in);
		this.category = cat;
	}

	public boolean has(ItemStack stack) {
		return stack.getItem().equals(bp.getItem());
	}

	public String getId() {
		return id;
	}

	public ItemStack getItem() {
		return bp;
	}

	public BlueprintCategory getCategory() {
		return category;
	}

	public enum BlueprintCategory {
		COMP,
		MACH,
		TOOL,
		SUIT
	}

	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("id", this.id);
		nbt.putString("category", this.category.name());
		CompoundNBT item = new CompoundNBT();
		this.bp.save(item);
		nbt.put("item", item);
		return nbt;
	}

	public static Blueprint fromNBT(CompoundNBT nbt) {
		return new Blueprint(nbt.getString("id"), ItemStack.of(nbt.getCompound("item")).getItem(),
				BlueprintCategory.valueOf(nbt.getString("category")));
	}
}
