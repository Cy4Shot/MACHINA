package com.machina.block.tile.base;

import com.machina.util.server.ItemStackHelper;
import com.machina.util.text.StringUtils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

public abstract class BaseLockableTileEntity extends LockableLootTileEntity {

	protected final int slots;
	protected NonNullList<ItemStack> items;

	public BaseLockableTileEntity(TileEntityType<?> type, int slots) {
		super(type);

		this.slots = slots;
		this.items = NonNullList.withSize(slots, ItemStack.EMPTY);

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

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
		super.handleUpdateTag(state, tag);
	}

	public void sync() {
		if (this.level instanceof ServerWorld) {
			final BlockState state = getBlockState();
			this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
			this.level.blockEntityChanged(this.worldPosition, this);
			this.setChanged();
		}
	}

	@Override
	public int getContainerSize() {
		return slots;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItems) {
		this.items = pItems;
		this.sync();
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		super.setItem(pIndex, pStack);
		this.sync();
	}

	@Override
	protected ITextComponent getDefaultName() {
		return StringUtils.EMPTY;
	}

	public ItemStack getItem(int id) {
		return this.items.get(id);
	}

	@Override
	protected abstract Container createMenu(int pId, PlayerInventory pPlayer);

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (!this.trySaveLootTable(compound)) {
			ItemStackHelper.saveAllItems(compound, this.items, "I");
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compound)) {
			ItemStackHelper.loadAllItems(compound, this.items, "I");
		}
	}

}
