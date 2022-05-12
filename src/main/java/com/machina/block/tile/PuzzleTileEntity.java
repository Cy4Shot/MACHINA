package com.machina.block.tile;

import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class PuzzleTileEntity extends TileEntity implements ITickableTileEntity {
	
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
	
	public void syncClients() {
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
		this.setChanged();
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
