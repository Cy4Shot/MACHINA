package com.machina.block.tile.machine;

import com.machina.block.container.FurnaceGeneratorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.capability.inventory.MachinaItemStorage;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.EnergyHelper;
import com.machina.util.helper.ItemStackHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class FurnaceGeneratorTileEntity extends MachinaTileEntity
		implements IMachinaContainerProvider, ITickableTileEntity, IEnergyTileEntity {

	private int litTime;

	public FurnaceGeneratorTileEntity() {
		super(TileEntityInit.FURNACE_GENERATOR.get());
	}

	MachinaItemStorage items;
	MachinaEnergyStorage energy;

	@Override
	public void createStorages() {
		this.items = add(new MachinaItemStorage(1));
		this.energy = add(new MachinaEnergyStorage(this, 100000, 1000));
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

	@Override
	public boolean isGeneratorMode() {
		return true;
	}
}
