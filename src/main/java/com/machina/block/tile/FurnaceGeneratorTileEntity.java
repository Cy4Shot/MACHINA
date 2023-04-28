package com.machina.block.tile;

import com.machina.block.container.FurnaceGeneratorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.CustomTE;
import com.machina.capability.CustomEnergyStorage;
import com.machina.capability.CustomItemStorage;
import com.machina.capability.IEnergyTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.EnergyHelper;
import com.machina.util.server.ItemStackHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class FurnaceGeneratorTileEntity extends CustomTE
		implements IMachinaContainerProvider, ITickableTileEntity, IEnergyTileEntity {

	private int litTime;

	public FurnaceGeneratorTileEntity() {
		super(TileEntityInit.FURNACE_GENERATOR.get());
	}

	CustomItemStorage items;
	CustomEnergyStorage energy;

	@Override
	public void createStorages() {
		this.items = add(new CustomItemStorage(1));
		this.energy = add(new CustomEnergyStorage(this, 100000, 1000));
	}

	@Override
	public void tick() {

		if (this.level.isClientSide())
			return;

		// Generate!
		boolean flag = this.isLit();
		boolean flag1 = false;
		if (this.isLit())
			--this.litTime;
		ItemStack itemstack = this.items.getStackInSlot(0);
		if ((this.isLit() || !itemstack.isEmpty()) && !this.energy.isFull()) {
			if (this.isLit()) {
				this.energy.receiveEnergy(400, false);
				flag1 = true;
			} else {
				this.litTime = ItemStackHelper.burnTime(itemstack);
				if (this.isLit()) {
					flag1 = true;
					if (itemstack.hasContainerItem())
						this.items.setStackInSlot(0, itemstack.getContainerItem());
					else if (!itemstack.isEmpty()) {
						itemstack.shrink(1);
						if (itemstack.isEmpty())
							this.items.setStackInSlot(0, itemstack.getContainerItem());
					}
				}
			}
		}

		if (flag != this.isLit()) {
			flag1 = true;
		}

		EnergyHelper.sendOutPower(energy, level, worldPosition, () -> setChanged());

		if (flag1) {
			sync();
		}
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new FurnaceGeneratorContainer(id, inventory, this);
	}

	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		this.litTime = nbt.getInt("BurnTime");
	}

	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.putInt("BurnTime", this.litTime);
		return nbt;
	}

	public int getEnergy() {
		return this.energy.getEnergyStored();
	}

	public int getMaxEnergy() {
		return this.energy.getMaxEnergyStored();
	}

	public float propFull() {
		return (float) this.getEnergy() / (float) this.getMaxEnergy();
	}

	@Override
	public boolean isGeneratorMode() {
		return true;
	}
}
