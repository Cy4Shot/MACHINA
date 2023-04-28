package com.machina.block.tile.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.machina.capability.CustomEnergyStorage;
import com.machina.capability.CustomFluidStorage;
import com.machina.capability.CustomItemStorage;
import com.machina.capability.ICustomStorage;
import com.machina.capability.LazyOptionalCache;
import com.machina.capability.MachinaTank;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class CustomTE extends BaseTileEntity implements IInventory {

	private List<ICustomStorage> storages = new ArrayList<>();
	private LazyOptionalCache<CustomItemStorage> item = new LazyOptionalCache<>();
	private LazyOptionalCache<CustomEnergyStorage> energy = new LazyOptionalCache<>();
	private LazyOptionalCache<CustomFluidStorage> fluid = new LazyOptionalCache<>();

	public abstract void createStorages();

	public CustomItemStorage add(CustomItemStorage item) {
		this.item.revalidate(() -> item);
		this.storages.add(item);
		return item;
	}

	public CustomEnergyStorage add(CustomEnergyStorage energy) {
		this.energy.revalidate(() -> energy);
		this.storages.add(energy);
		return energy;
	}

	public CustomFluidStorage add(MachinaTank... fluid) {
		LinkedList<MachinaTank> tank = new LinkedList<>(Arrays.asList(fluid));
		CustomFluidStorage f = new CustomFluidStorage(tank);
		this.fluid.revalidate(() -> f);
		this.storages.add(f);
		return f;
	}

	public CustomTE(TileEntityType<?> type) {
		super(type);
		createStorages();
		storages.forEach(storage -> storage.setChanged(() -> setChanged()));
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		item.invalidate();
		energy.invalidate();
		fluid.invalidate();
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		storages.forEach(storage -> storage.deserializeNBT(tag.getCompound(storage.getTag())));
		super.load(state, tag);
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		storages.forEach(storage -> tag.put(storage.getTag(), storage.serializeNBT()));
		return super.save(tag);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return item.get().cast();
		}
		if (cap == CapabilityEnergy.ENERGY) {
			return energy.get().cast();
		}
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return fluid.get().cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public int getContainerSize() {
		CustomItemStorage storage = this.item.get().orElseGet(() -> null);
		if (storage == null) {
			return 0;
		}

		return storage.getSlots();
	}

	@Override
	public boolean isEmpty() {
		CustomItemStorage storage = this.item.get().orElseGet(() -> null);
		if (storage == null) {
			return true;
		}

		return storage.items().stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int pIndex) {
		CustomItemStorage storage = this.item.get().orElseGet(() -> null);
		if (storage == null) {
			return null;
		}

		return storage.getStackInSlot(pIndex);
	}

	@Override
	public ItemStack removeItem(int pIndex, int pCount) {

		CustomItemStorage storage = this.item.get().orElseGet(() -> null);
		if (storage == null) {
			return null;
		}

		ItemStack itemstack = ItemStackHelper.removeItem(storage.items(), pIndex, pCount);
		if (!itemstack.isEmpty()) {
			this.setChanged();
		}

		return itemstack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int pIndex) {
		CustomItemStorage storage = this.item.get().orElseGet(() -> null);
		if (storage == null) {
			return null;
		}
		return ItemStackHelper.takeItem(storage.items(), pIndex);
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		this.item.get().ifPresent(item -> {
			item.setStackInSlot(pIndex, pStack);
			if (pStack.getCount() > this.getMaxStackSize()) {
				pStack.setCount(this.getMaxStackSize());
			}

			this.setChanged();
		});
	}

	@Override
	public boolean stillValid(PlayerEntity pPlayer) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(pPlayer.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
					(double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public void clearContent() {
		this.item.get().ifPresent(item -> {
			item.clear();
		});
	}
}
