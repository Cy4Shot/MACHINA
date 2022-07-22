package com.machina.block.tile;

import com.machina.block.container.FurnaceGeneratorContainer;
import com.machina.block.container.base.IMachinaContainerProvider;
import com.machina.block.tile.base.BaseEnergyLootTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.ItemStackUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class FurnaceGeneratorTileEntity extends BaseEnergyLootTileEntity implements IMachinaContainerProvider {

	private int litTime;

	public FurnaceGeneratorTileEntity() {
		super(TileEntityInit.FURNACE_GENERATOR.get(), 1);
		this.sides = new int[] { 2, 2, 2, 2, 2, 2 };
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
		ItemStack itemstack = this.items.get(0);
		if ((this.isLit() || !itemstack.isEmpty()) && !this.energyDef.isFull()) {
			if (this.isLit()) {
				this.energyDef.receiveEnergy(400, false);
				flag1 = true;
			} else {
				this.litTime = ItemStackUtil.burnTime(itemstack);
				if (this.isLit()) {
					flag1 = true;
					if (itemstack.hasContainerItem())
						this.items.set(0, itemstack.getContainerItem());
					else if (!itemstack.isEmpty()) {
						itemstack.shrink(1);
						if (itemstack.isEmpty())
							this.items.set(0, itemstack.getContainerItem());
					}
				}
			}
		}

		if (flag != this.isLit()) {
			flag1 = true;
//			this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition)
//					.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isLit())), 3);
		}

		if (flag1) {
			sync();
		}
		super.tick();
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	@Override
	public MachinaEnergyStorage createStorage() {
		return new MachinaEnergyStorage(this, 100000, 1000, 1000);
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
}
