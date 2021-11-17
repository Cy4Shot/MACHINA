package com.cy4.machina.tile_entity;

import com.cy4.machina.init.TileEntityTypesInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class RocketMountTile extends TileEntity implements ITickableTileEntity
{
    private int refreshTime;
	
    
    
	public RocketMountTile() {
        super(TileEntityTypesInit.ROCKET_MOUNT_TILE);
    }

	

    @Override
    public void tick() {
    	
    }
    
    
    
    public void setInitialRefreshTime(int newRefreshTime) {
    	refreshTime = newRefreshTime;
    	this.setChanged();
    }
    
    
    
    public int getRefreshTime() { return refreshTime; }
    
    
    
    @Override
    public CompoundNBT save(CompoundNBT tags) {
    	if (tags == null) tags = new CompoundNBT();
        tags.putInt("RefreshTime", this.refreshTime);
        return super.save(tags);
    }
    
    @Override
    public void load(BlockState state, CompoundNBT tags) {
        if (tags != null && tags.contains("RefreshTime")) this.refreshTime = tags.getInt("RefreshTime");
        super.load(state, tags);
    }
}
