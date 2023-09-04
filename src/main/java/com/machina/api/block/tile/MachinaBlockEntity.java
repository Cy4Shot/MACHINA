package com.machina.api.block.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.machina.api.cap.ICustomStorage;
import com.machina.api.cap.LazyOptionalCache;
import com.machina.api.cap.energy.MachinaEnergyStorage;
import com.machina.api.cap.fluid.MachinaFluidStorage;
import com.machina.api.cap.fluid.MachinaTank;
import com.machina.api.cap.item.MachinaItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public abstract class MachinaBlockEntity extends BaseBlockEntity implements Container {

	private final List<ICustomStorage> storages = new ArrayList<>();
	private final LazyOptionalCache<MachinaItemStorage> item = new LazyOptionalCache<>();
	private final LazyOptionalCache<MachinaEnergyStorage> energy = new LazyOptionalCache<>();
	private final LazyOptionalCache<MachinaFluidStorage> fluid = new LazyOptionalCache<>();

	public abstract void createStorages();

	public MachinaItemStorage add(MachinaItemStorage item) {
		this.item.revalidate(() -> item);
		this.storages.add(item);
		return item;
	}

	public MachinaEnergyStorage add(MachinaEnergyStorage energy) {
		this.energy.revalidate(() -> energy);
		this.storages.add(energy);
		return energy;
	}

	public MachinaFluidStorage add(MachinaTank... fluid) {
		LinkedList<MachinaTank> tank = new LinkedList<>(Arrays.asList(fluid));
		MachinaFluidStorage f = new MachinaFluidStorage(tank);
		this.fluid.revalidate(() -> f);
		this.storages.add(f);
		return f;
	}

	public MachinaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		createStorages();
		storages.forEach(storage -> storage.setChanged(this::setChanged));
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		item.invalidate();
		energy.invalidate();
		fluid.invalidate();
	}

	@Override
	public void load(CompoundTag tag) {
		storages.forEach(storage -> storage.deserialize(tag.getCompound(storage.getTag())));
		super.load(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		storages.forEach(storage -> tag.put(storage.getTag(), storage.serialize()));
		super.saveAdditional(tag);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return item.get().cast();
		}
		if (cap == ForgeCapabilities.ENERGY) {
			return energy.get().cast();
		}
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return fluid.get().cast();
		}
		return super.getCapability(cap, side);
	}

	// Could be replaced by empty storages instead of null, throwing an exception
	@Override
	public int getContainerSize() {
		MachinaItemStorage storage = this.item.get().orElseGet(() -> null);

		return storage.getSlots();
	}

	@Override
	public boolean isEmpty() {
		MachinaItemStorage storage = this.item.get().orElseGet(() -> null);

		return storage.items().stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public @NotNull ItemStack getItem(int pIndex) {
		MachinaItemStorage storage = this.item.get().orElseGet(() -> null);

		return storage.getStackInSlot(pIndex);
	}

	@Override
	public @NotNull ItemStack removeItem(int pIndex, int pCount) {

		MachinaItemStorage storage = this.item.get().orElseGet(() -> null);

		ItemStack itemstack = storage.extractItem(pIndex, pCount, false);
		if (!itemstack.isEmpty()) {
			this.setChanged();
		}

		return itemstack;
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
		MachinaItemStorage storage = this.item.get().orElseGet(() -> null);
		ItemStack slot = storage.getStackInSlot(pIndex);
		return storage.extractItem(pIndex, slot.getCount(), false);
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		MachinaItemStorage storage = this.item.get().orElseGet(() -> null);

		storage.setStackInSlot(pIndex, pStack);
		if (pStack.getCount() > this.getMaxStackSize()) {
			pStack.setCount(this.getMaxStackSize());
		}

		this.setChanged();
	}

	@Override
	public boolean stillValid(@NotNull Player pPlayer) {
		if ((this.level != null ? this.level.getBlockEntity(this.worldPosition) : null) != this) {
			return false;
		} else {
			return !(pPlayer.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
					(double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public void clearContent() {
		MachinaItemStorage storage = this.item.get().orElseGet(() -> null);

		storage.clear();
	}

	@Override
	public void setChanged() {
		if (this.level instanceof ServerLevel) {
			final BlockState state = getBlockState();
			this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
			this.level.blockEntityChanged(this.worldPosition);
		}
		super.setChanged();
	}

	public int getEnergy() {
		MachinaEnergyStorage storage = this.energy.get().orElseGet(() -> null);

		return storage.getEnergyStored();
	}

	public int getMaxEnergy() {
		MachinaEnergyStorage storage = this.energy.get().orElseGet(() -> null);
		return storage.getMaxEnergyStored();
	}

	public float getEnergyProp() {
		MachinaEnergyStorage storage = this.energy.get().orElseGet(() -> null);
		return (float) storage.getEnergyStored() / (float) storage.getMaxEnergyStored();
	}

	public MachinaTank getTank(int id) {
		MachinaFluidStorage storage = this.fluid.get().orElseGet(() -> null);
		return storage.tank(id);
	}
}