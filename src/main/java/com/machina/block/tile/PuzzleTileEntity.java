package com.machina.block.tile;

import com.machina.block.CargoCrateBlock;
import com.machina.block.container.PuzzleContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.math.DirectionUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class PuzzleTileEntity extends BaseTileEntity implements IMachinaContainerProvider {
	
	public boolean unlocked = false;

	public PuzzleTileEntity(TileEntityType<?> te) {
		super(te);
	}

	public PuzzleTileEntity() {
		this(TileEntityTypesInit.PUZZLE.get());
	}

	public void completed() {
		if (!unlocked) {
			DirectionUtil.DIRECTIONS.forEach(dir -> {
				BlockPos pos = this.worldPosition.relative(dir);
				BlockState state = this.level.getBlockState(pos);
				if (state.getBlock().equals(BlockInit.CARGO_CRATE.get())) {
					TileEntity te = level.getBlockEntity(pos);
					if (te == null || !(te instanceof CargoCrateTileEntity))
						return;

					((CargoCrateTileEntity) te).setOpen();
					level.setBlock(pos, state.setValue(CargoCrateBlock.OPEN, Boolean.valueOf(true)), 3);

					return;
				}
			});
			
			this.unlocked = true;
			sync();
		}
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putBoolean("unlocked", unlocked);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState p_230337_1_, CompoundNBT nbt) {
		this.unlocked = nbt.getBoolean("unlocked");
		super.load(p_230337_1_, nbt);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new PuzzleContainer(windowId, this);
	}
}
