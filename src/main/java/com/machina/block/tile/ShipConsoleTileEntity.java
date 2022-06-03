package com.machina.block.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.machina.block.container.ShipConsoleContainer;
import com.machina.recipe.ShipConsoleRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.MachinaRL;
import com.machina.util.server.ItemStackUtil;
import com.machina.util.text.StringUtils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class ShipConsoleTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

	public static int slots = 4;
	protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
	public int stage = 1, progress = 0;
	public boolean isInProgress = false;

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			switch (index) {
			case 0:
				return ShipConsoleTileEntity.this.stage;
			case 1:
				return ShipConsoleTileEntity.this.progress;
			default:
				return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
			case 0:
				ShipConsoleTileEntity.this.stage = value;
				break;
			case 1:
				ShipConsoleTileEntity.this.progress = value;
				break;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

	public ShipConsoleTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public ShipConsoleTileEntity() {
		this(TileEntityTypesInit.SHIP_CONSOLE.get());
	}

	public List<ItemStack> getItemsForStage() {
		try {
			ShipConsoleRecipe recipe = (ShipConsoleRecipe) RecipeInit
					.getRecipes(RecipeInit.SHIP_CONSOLE_RECIPE, this.level.getRecipeManager())
					.get(new MachinaRL("ship_console_" + stage));

			return recipe.createRequirements();
		} catch (Exception e) {
			return Collections.nCopies(4, ItemStack.EMPTY);
		}
	}

	public List<ItemStack> getMissingItems() {
		List<ItemStack> missing = new ArrayList<>();
		getItemsForStage().forEach(item -> {
			ItemStack item1 = item.copy();
			for (ItemStack stack : getItems()) {
				if (item1.getItem().equals(stack.getItem())) {
					item1.shrink(stack.getCount());
				}
			}
			if (!item1.isEmpty()) {
				missing.add(item1);
			}
		});
		return missing;
	}

	public ItemStack getItemForStage(int id) {
		return getItemsForStage().get(id);
	}

	public IIntArray getData() {
		return this.data;
	}

	public void buttonPressed() {
		if (this.stage < 5 && this.progress == 0) {
			this.isInProgress = true;
			clear();
		}
	}

	public void clear() {
		for (int i = 0; i < getContainerSize(); i++)
			setItem(i, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		if (this.isInProgress) {
			this.progress++;
			if (this.progress == 100) {
				this.stage++;
				this.isInProgress = false;
				this.progress = 0;
			}
		}
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
		this.syncClients();
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		super.setItem(pIndex, pStack);
		this.syncClients();
	}

	public void syncClients() {
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
		this.setChanged();
	}

	@Override
	protected ITextComponent getDefaultName() {
		return StringUtils.translateComp("machina.container.ship_console");
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
		compound.putInt("Progress", this.progress);
		compound.putBoolean("InProgress", this.isInProgress);
		if (!this.trySaveLootTable(compound)) {
			ItemStackUtil.saveAllItems(compound, this.items, "I");
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compound)) {
			ItemStackUtil.loadAllItems(compound, this.items, "I");
		}
		this.stage = compound.getInt("Stage");
		this.progress = compound.getInt("Progress");
		this.isInProgress = compound.getBoolean("InProgress");
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.save(nbt);
		return new SUpdateTileEntityPacket(this.getBlockPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(this.getBlockState(), pkt.getTag());
	}
}
