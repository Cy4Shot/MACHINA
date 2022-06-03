package com.machina.block.tile;

import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.item.ShipComponentItem;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.server.ItemStackUtil;
import com.machina.util.server.ParticleUtil;
import com.machina.util.text.StringUtils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class ComponentAnalyzerTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

	public static int slots = 2;
	protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
	public int progress = 0;

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			switch (index) {
			case 0:
				return ComponentAnalyzerTileEntity.this.progress;
			default:
				return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
			case 0:
				ComponentAnalyzerTileEntity.this.progress = value;
				break;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

	public ComponentAnalyzerTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public ComponentAnalyzerTileEntity() {
		this(TileEntityTypesInit.COMPONENT_ANALYZER.get());
	}

	public IIntArray getData() {
		return this.data;
	}

	@Override
	public void tick() {
		if (!this.getItem(0).isEmpty() && this.getItem(1).isEmpty()) {
			this.progress++;

			if (this.progress % 3 == 0) {
				ParticleUtil.spawnParticle(level, ParticleTypes.END_ROD, getBlockPos(), 0.1D, null);
			}

			if (this.progress == 100) {
				this.progress = 0;
				this.getItem(0).shrink(1);
					this.setItem(1, ShipComponentItem.randomComponent());
				for (int i = 0; i < 10; i++) {
					ParticleUtil.spawnParticle(level, ParticleTypes.CAMPFIRE_COSY_SMOKE, getBlockPos(), 0.1D, null);
				}
			}
		} else {
			this.progress = 0;
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
		return StringUtils.translateComp("machina.container.component_analyzer");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new ComponentAnalyzerContainer(id, player, this);
	}

	public ItemStack getItem(int id) {
		return this.items.get(id);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("Progress", this.progress);
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
		this.progress = compound.getInt("Progress");
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