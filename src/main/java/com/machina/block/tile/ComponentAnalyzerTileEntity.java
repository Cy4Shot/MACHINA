package com.machina.block.tile;

import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.item.ShipComponentItem;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.ParticleHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;

public class ComponentAnalyzerTileEntity extends BaseLockableTileEntity implements ITickableTileEntity {

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
			return 1;
		}
	};

	public ComponentAnalyzerTileEntity(TileEntityType<?> type) {
		super(type, 2);
	}

	public ComponentAnalyzerTileEntity() {
		this(TileEntityInit.COMPONENT_ANALYZER.get());
	}

	public IIntArray getData() {
		return this.data;
	}

	@Override
	public void tick() {
		if (!this.getItem(0).isEmpty() && this.getItem(1).isEmpty()) {
			this.progress++;

			if (this.progress % 3 == 0) {
				ParticleHelper.spawnParticle(level, ParticleTypes.END_ROD, getBlockPos(), 0.1D, null);
			}

			if (this.progress == 100) {
				this.progress = 0;
				this.getItem(0).shrink(1);
				this.setItem(1, ShipComponentItem.randomComponent());
				for (int i = 0; i < 10; i++) {
					ParticleHelper.spawnParticle(level, ParticleTypes.CAMPFIRE_COSY_SMOKE, getBlockPos(), 0.1D, null);
				}
			}
		} else {
			this.progress = 0;
		}
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new ComponentAnalyzerContainer(id, player, this);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("Progress", this.progress);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.progress = compound.getInt("Progress");
	}
}