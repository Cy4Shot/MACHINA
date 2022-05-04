package com.machina.block.tile;

import com.machina.block.container.ShipConsoleContainer;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.nbt.ItemStackUtil;
import com.machina.util.text.TextComponentHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class ShipConsoleTileEntity extends LockableLootTileEntity {

	public static int slots = 4;
	protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
	public NonNullList<ItemStack> required = NonNullList.withSize(slots, ItemStack.EMPTY);

	public ShipConsoleTileEntity(TileEntityType<?> type) {
		super(type);
		if (required.get(0).isEmpty())
			required.set(0, new ItemStack(Items.DIAMOND, 1));
		if (required.get(1).isEmpty())
			required.set(1, new ItemStack(Items.STICK, 3));
		if (required.get(2).isEmpty())
			required.set(2, new ItemStack(ItemInit.SHIP_COMPONENT.get(), 1));
		if (required.get(3).isEmpty())
			required.set(3, new ItemStack(Blocks.ACACIA_LOG.asItem(), 1));
	}

	public ShipConsoleTileEntity() {
		this(TileEntityTypesInit.SHIP_CONSOLE.get());
	}

	@Override
	public int getContainerSize() {
		return slots;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItems) {
		this.items = pItems;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return TextComponentHelper.translate("machina.container.ship_console");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new ShipConsoleContainer(id, player, this);
	}

	public ItemStack getItem(int id) {
		return this.items.get(id);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (!this.trySaveLootTable(compound)) {
			ItemStackUtil.saveAllItems(compound, this.items, "I");
			ItemStackUtil.saveAllItems(compound, this.required, "R");
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		this.required = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compound)) {
			ItemStackUtil.loadAllItems(compound, this.items, "I");
			ItemStackUtil.loadAllItems(compound, this.required, "R");
		}
	}

}
