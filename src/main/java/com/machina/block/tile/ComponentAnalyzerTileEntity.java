package com.machina.block.tile;

import java.util.Random;

import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.nbt.ItemStackUtil;
import com.machina.util.text.TextComponentHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
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
		if (!this.getItem(0).isEmpty()) {
			this.progress++;

			if (this.progress % 3 == 0) {
				BlockPos p = this.getBlockPos();
				Random r = new Random();
				double d0 = (double) p.getX() + 0.5D;
				double d1 = (double) p.getY() + 0.5D;
				double d2 = (double) p.getZ() + 0.5D;
				double d3 = (r.nextDouble() - 0.5D) * 0.1D;
				double d4 = (r.nextDouble() - 0.5D) * 0.1D;
				double d5 = (r.nextDouble() - 0.5D) * 0.1D;
				this.level.addParticle(ParticleTypes.END_ROD, d0, d1, d2, d3, d4, d5);
			}

			if (this.progress == 100) {
				this.progress = 0;
				this.getItem(0).shrink(1);
				if (this.getItem(1).isEmpty()) {
					this.setItem(1, new ItemStack(Items.DIAMOND));
				} else {
					this.getItem(1).grow(1);
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
	}

	@Override
	protected ITextComponent getDefaultName() {
		return TextComponentHelper.translate("machina.container.component_analyzer");
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