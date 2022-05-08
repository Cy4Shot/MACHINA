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
		List<ItemStack> required = new ArrayList<>();
		if (stage == 1) {
			required.add(new ItemStack(Items.DIAMOND, 1));
			required.add(new ItemStack(Items.STICK, 3));
			required.add(new ItemStack(ItemInit.SHIP_COMPONENT.get(), 1));
			required.add(new ItemStack(Blocks.ACACIA_LOG.asItem(), 1));
		} else if (stage == 2) {
			required.add(new ItemStack(Items.ACACIA_BUTTON, 1));
			required.add(new ItemStack(Items.BEEF, 3));
			required.add(new ItemStack(ItemInit.THERMAL_REGULATING_BOOTS.get(), 1));
			required.add(new ItemStack(Blocks.BRAIN_CORAL_BLOCK.asItem(), 2));
		} else {
			required.add(ItemStack.EMPTY);
			required.add(ItemStack.EMPTY);
			required.add(ItemStack.EMPTY);
			required.add(ItemStack.EMPTY);
		}

		return required;
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
