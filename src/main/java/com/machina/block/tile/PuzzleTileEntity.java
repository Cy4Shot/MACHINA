package com.machina.block.tile;

import com.machina.block.container.PuzzleContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;

public class PuzzleTileEntity extends BaseTileEntity implements ITickableTileEntity, IMachinaContainerProvider {

	public int type = 0;

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			switch (index) {
			case 0:
				return PuzzleTileEntity.this.type;
			default:
				return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
			case 0:
				PuzzleTileEntity.this.type = value;
				break;
			}
		}

		@Override
		public int getCount() {
			return 1;
		}
	};

	public PuzzleTileEntity(TileEntityType<?> te) {
		super(te);
	}

	public PuzzleTileEntity() {
		this(TileEntityTypesInit.PUZZLE.get());
	}

	@Override
	public void tick() {

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("Type", this.type);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.type = compound.getInt("Type");
	}
	
	public IIntArray getData() {
		return this.data;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new PuzzleContainer(windowId, this);
	}
}
