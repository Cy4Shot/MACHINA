package com.machina.block.tile.multiblock.pump;

import com.machina.block.container.PumpContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.multiblock.MultiblockMasterTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.MachinaRL;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class PumpControllerTileEntity extends MultiblockMasterTileEntity
		implements ITickableTileEntity, IMachinaContainerProvider {

	public PumpControllerTileEntity() {
		this(TileEntityInit.PUMP_CONTROLLER.get());
	}

	public PumpControllerTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return new MachinaRL("pump");
	}

	@Override
	public void createStorages() {
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new PumpContainer(id, inventory, this);
	}
}
