package com.machina.block.tile;

import com.machina.block.container.AtmosphericSeperatorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseTileEntity;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IIntArray;

public class AtmosphericSeperatorTileEntity extends BaseTileEntity implements IMachinaContainerProvider {

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			return 0;
		}

		public void set(int index, int value) {
		}

		@Override
		public int getCount() {
			return 0;
		}
	};

	public AtmosphericSeperatorTileEntity() {
		super(TileEntityInit.ATMOSPHERIC_SEPERATOR.get());
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new AtmosphericSeperatorContainer(id, this);
	}

	public IIntArray getData() {
		return this.data;
	}

}
