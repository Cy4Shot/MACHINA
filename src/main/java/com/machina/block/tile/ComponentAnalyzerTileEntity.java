package com.machina.block.tile;

import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.CustomTE;
import com.machina.capability.CustomItemStorage;
import com.machina.item.ShipComponentItem;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.ParticleHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ComponentAnalyzerTileEntity extends CustomTE
		implements ITickableTileEntity, IAnimatable, IMachinaContainerProvider {

	private final AnimationFactory manager = new AnimationFactory(this);
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
		super(type);
	}

	public ComponentAnalyzerTileEntity() {
		this(TileEntityInit.COMPONENT_ANALYZER.get());
	}

	CustomItemStorage items;

	@Override
	public void createStorages() {
		this.items = add(new CustomItemStorage(2));
	}

	public IIntArray getData() {
		return this.data;
	}

	@Override
	public void tick() {
		if (!items.getStackInSlot(0).isEmpty() && items.getStackInSlot(1).isEmpty()) {
			this.progress++;

			if (this.progress % 3 == 0) {
				ParticleHelper.spawnParticle(level, ParticleTypes.END_ROD, getBlockPos(), 0.1D, null);
			}

			if (this.progress == 100) {
				this.progress = 0;
				items.getStackInSlot(0).shrink(1);
				items.setStackInSlot(1, ShipComponentItem.randomComponent());
				for (int i = 0; i < 10; i++) {
					ParticleHelper.spawnParticle(level, ParticleTypes.CAMPFIRE_COSY_SMOKE, getBlockPos(), 0.1D, null);
				}
			}
		} else {
			this.progress = 0;
		}
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new ComponentAnalyzerContainer(id, inventory, this);
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

	@Override
	public void registerControllers(AnimationData data) {
	}

	@Override
	public AnimationFactory getFactory() {
		return this.manager;
	}
}