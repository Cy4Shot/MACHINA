package com.machina.block.tile;

import com.machina.block.tile.base.BaseTileEntity;
import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class PuzzleTileEntity extends BaseTileEntity implements ITickableTileEntity {

	public int type = 0;

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
}
