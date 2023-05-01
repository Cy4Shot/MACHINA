package com.machina.block.multiblock;

import com.machina.block.tile.multiblock.MultiblockMasterTileEntity;
import com.machina.block.tile.multiblock.MultiblockPartTileEntity;
import com.machina.util.helper.BlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class MultiblockBlock extends Block {

	public MultiblockBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return getTE().create();
	}

	public abstract TileEntityType<?> getTE();

	public abstract boolean isMaster();

	public boolean isFormed(World world, BlockPos pos) {
		TileEntity e = world.getBlockEntity(pos);
		if (e == null)
			return false;
		
		if (isMaster()) {
			if (!MultiblockMasterTileEntity.class.isAssignableFrom(e.getClass())) {
				return false;
			}
			return ((MultiblockMasterTileEntity) e).formed;
		} else {
			if (!MultiblockPartTileEntity.class.isAssignableFrom(e.getClass())) {
				return false;
			}
			return ((MultiblockPartTileEntity) e).formed;
		}
	}

	@Override
	public void onPlace(BlockState pState, World pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
		if (isMaster()) {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockMasterTileEntity.class, te -> {
				te.update();
			});
		} else {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockPartTileEntity.class, te -> {
				te.attemptAssimilate();
			});
		}
		super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
	}

	@Override
	public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (isMaster()) {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockMasterTileEntity.class, te -> {
				te.deform();
			});
		} else {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockPartTileEntity.class, te -> {
				te.update();
			});
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}
}
