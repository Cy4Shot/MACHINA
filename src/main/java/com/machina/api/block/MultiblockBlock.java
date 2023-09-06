package com.machina.api.block;

import com.machina.api.block.tile.multiblock.MultiblockMasterBlockEntity;
import com.machina.api.block.tile.multiblock.MultiblockPartBlockEntity;
import com.machina.api.util.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Abstract class for Multiblock blocks to extend. Provides functionality for
 * forming and unforming a multiblock.
 * 
 * @author Cy4Shot
 * @since Machina v0.1.0
 *
 */
public abstract class MultiblockBlock extends BaseEntityBlock {

	public MultiblockBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return getTE().create(pos, state);
	}

	/**
	 * Method which specifies the {@link BlockEntityType} associated with the block.
	 * 
	 * @return {@link BlockEntityType}: The block entity type assiociated with the
	 *         block.
	 */
	public abstract BlockEntityType<?> getTE();

	/**
	 * Method which specifies if the block is a master / controller or a part /
	 * port.
	 * 
	 * @return {@code boolean}: Whether the block is a multiblock master /
	 *         controller.
	 */
	public abstract boolean isMaster();

	public boolean isFormed(Level world, BlockPos pos) {
		BlockEntity e = world.getBlockEntity(pos);
		if (e == null)
			return false;

		if (isMaster()) {
			if (!MultiblockMasterBlockEntity.class.isAssignableFrom(e.getClass())) {
				return false;
			}
			return ((MultiblockMasterBlockEntity) e).formed;
		} else {
			if (!MultiblockPartBlockEntity.class.isAssignableFrom(e.getClass())) {
				return false;
			}
			return ((MultiblockPartBlockEntity) e).formed;
		}
	}

	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
		if (isMaster()) {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockMasterBlockEntity.class, te -> {
				te.update();
			});
		} else {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockPartBlockEntity.class, te -> {
				te.attemptAssimilate();
			});
		}
		super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (isMaster()) {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockMasterBlockEntity.class, te -> {
				te.deform();
			});
		} else {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockPartBlockEntity.class, te -> {
				te.update();
			});
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}
}