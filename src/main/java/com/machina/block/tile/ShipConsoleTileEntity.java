package com.machina.block.tile;

import java.util.ArrayList;
import java.util.List;

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
	public int stage = 1;
	public List<ItemStack> required = new ArrayList<>();

	public ShipConsoleTileEntity(TileEntityType<?> type) {
		super(type);
		updateStage();
	}

	public ShipConsoleTileEntity() {
		this(TileEntityTypesInit.SHIP_CONSOLE.get());
	}
	
	public void updateStage() {
		switch (stage) {
		case 1:
			required.clear();
			required.add(new ItemStack(Items.DIAMOND, 1));
			required.add(new ItemStack(Items.STICK, 3));
			required.add(new ItemStack(ItemInit.SHIP_COMPONENT.get(), 1));
			required.add(new ItemStack(Blocks.ACACIA_LOG.asItem(), 1));
		case 2:
			required.clear();
			required.add(new ItemStack(Items.ACACIA_BUTTON, 1));
			required.add(new ItemStack(Items.BEEF, 3));
			required.add(new ItemStack(ItemInit.THERMAL_REGULATING_BOOTS.get(), 1));
			required.add(new ItemStack(Blocks.BRAIN_CORAL_BLOCK.asItem(), 2));
		}

		System.out.println(required.get(0));
	}

	@Override
	public void onLoad() {
		updateStage();
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
		compound.putInt("Stage", this.stage);
		if (!this.trySaveLootTable(compound)) {
			ItemStackUtil.saveAllItems(compound, this.items, "");
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.stage = compound.getInt("Stage");
		this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compound)) {
			ItemStackUtil.loadAllItems(compound, this.items, "");
		}
		updateStage();
	}

}
