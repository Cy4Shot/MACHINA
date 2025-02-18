package com.machina.api.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.menu.IMachinaMenuProvider;
import com.machina.api.cap.energy.MachinaEnergyStorage;
import com.machina.api.cap.fluid.MachinaFluidStorage;
import com.machina.api.cap.fluid.MachinaTank;
import com.machina.api.cap.sided.ISideAdapter;
import com.machina.api.cap.sided.MultiSidedStorage;
import com.machina.api.cap.sided.Side;
import com.machina.api.cap.sided.SidedStorage;
import com.machina.api.cap.sided.SingleSidedStorage;
import com.machina.api.util.block.BlockProperties;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.machine.BatteryBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * Abstract class to allow Machina BlockEntities to store items, fluids and
 * energy.
 * 
 * @author Cy4Shot
 * @since Machina v0.1.0
 */
public abstract class MachinaBlockEntity extends BaseBlockEntity implements WorldlyContainer, IMachinaMenuProvider {

	private LockCode lockKey = LockCode.NO_LOCK;
	protected MultiSidedStorage<MachinaEnergyStorage> energyCap;
	protected final List<SingleSidedStorage<MachinaFluidStorage>> fluidsCap = new ArrayList<>();
	protected final NonNullList<ItemStack> items = NonNullList.create();
	protected NonNullList<Side[]> itemSides = NonNullList.create();

	private int energy;

	public abstract void createStorages();

	public void itemStorage(Side[] sides) {
		this.items.add(ItemStack.EMPTY);
		this.itemSides.add(sides.clone());
	}

	public void energyStorage(Side[] sides) {
		this.energyCap = new MultiSidedStorage<>("cap_energy", this, MachinaEnergyStorage::new, sides.clone());
	}

	public void fluidStorage(int capacity, Predicate<FluidStack> validator, Side[] sides) {
		int id = this.fluidsCap.size();
		this.fluidsCap.add(new SingleSidedStorage<>("cap_fluid_" + id, this,
				new MachinaFluidStorage(new MachinaTank(this, capacity, validator, id)), sides.clone()));
	}

	public MachinaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.createStorages();
	}

	public void forEachStorage(Consumer<SidedStorage> consumer) {
		if (this.energyCap != null)
			consumer.accept(energyCap);
		this.fluidsCap.forEach(consumer);
	}

	public Supplier<ISideAdapter> getEnergyAdapter() {
		return () -> this.energyCap;
	}

	public Supplier<ISideAdapter> getFluidAdapter(int tank) {
		return () -> this.fluidsCap.get(tank);
	}

	public Supplier<ISideAdapter> getItemAdapter(int slot) {
		return () -> new ISideAdapter() {
			@Override
			public Side get(Direction d) {
				return MachinaBlockEntity.this.itemSides.get(slot)[d.ordinal()];
			}

			@Override
			public void cycle(Direction d) {
				Side.cycle(MachinaBlockEntity.this.itemSides.get(slot), d, MachinaBlockEntity.this, "cap_item_" + slot);
			}
		};
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		forEachStorage(SidedStorage::invalidate);
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		super.load(tag);
		forEachStorage(s -> s.loadAdditional(tag));
		this.energy = tag.getInt("energy");
		this.lockKey = LockCode.fromTag(tag);
		ContainerHelper.loadAllItems(tag, this.items);
		this.itemSides = NonNullList.create();
		ListTag sides = tag.getList("sides_item", Tag.TAG_COMPOUND);
		for (int i = 0; i < sides.size(); i++) {
			this.itemSides.add(Side.deserialize(sides.getCompound(i)));
		}
		this.setChanged();

	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		forEachStorage(s -> s.saveAdditional(tag));
		tag.putInt("energy", energy);
		ContainerHelper.saveAllItems(tag, this.items);
		ListTag sides = new ListTag();
		for (Side[] itemSide : this.itemSides) {
			sides.add(Side.serialize(itemSide));
		}
		tag.put("sides_item", sides);
		this.lockKey.addToTag(tag);
		super.saveAdditional(tag);
	}

	LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (side != null) {
			if (cap == ForgeCapabilities.ENERGY) {
				if (energyCap != null && energyCap.isNonNullMode(side)) {
					return energyCap.getLazy(side).cast();
				}
			} else if (cap == ForgeCapabilities.ITEM_HANDLER && !this.remove) {
				return handlers[side.ordinal()].cast();
			} else if (cap == ForgeCapabilities.FLUID_HANDLER) {
				for (SingleSidedStorage<MachinaFluidStorage> storage : fluidsCap) {
					if (storage.isNonNullMode(side)) {
						return storage.get().cast();
					}
				}
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		forEachStorage(SidedStorage::invalidate);
		for (LazyOptional<? extends IItemHandler> handler : handlers) {
			handler.invalidate();
		}
		super.invalidateCaps();
	}

	@Override
	public void reviveCaps() {
		handlers = SidedInvWrapper.create(this, Direction.values());
		this.items.clear();
		this.itemSides.clear();
		this.fluidsCap.clear();
		this.createStorages();
		super.reviveCaps();
	}

	public boolean isLit() {
		return false;
	}

	@Override
	public int getContainerSize() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		return items.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public @NotNull ItemStack getItem(int pIndex) {
		return items.get(pIndex);
	}

	@Override
	public @NotNull ItemStack removeItem(int pIndex, int pCount) {
		ItemStack stack = ContainerHelper.removeItem(items, pIndex, pCount);
		if (!stack.isEmpty()) {
			this.setChanged();
		}
		return stack;
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
		return ContainerHelper.takeItem(items, pIndex);
	}

	@Override
	public void setItem(int pIndex, @NotNull ItemStack pStack) {
		this.items.set(pIndex, pStack);
		if (pStack.getCount() > this.getMaxStackSize()) {
			pStack.setCount(this.getMaxStackSize());
		}
		this.setChanged();
	}

	@Override
	public int @NotNull [] getSlotsForFace(@NotNull Direction slots) {
		List<Integer> acceptable = new ArrayList<>();
		for (int i = 0; i < itemSides.size(); i++) {
			if (itemSides.get(i)[slots.ordinal()] != Side.NONE) {
				acceptable.add(i);
			}
		}
		return acceptable.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, Direction face) {
		if (face != null) {
			return itemSides.get(slot)[face.ordinal()] == Side.INPUT;
		}
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, Direction face) {
		return itemSides.get(slot)[face.ordinal()] == Side.OUTPUT;
	}

	public FluidStack getFluid(int tank) {
		return this.fluidsCap.get(tank).get().map(MachinaFluidStorage::getFluidInTank).get();
	}

	public int getTankCapacity(int tank) {
		return this.fluidsCap.get(tank).get().map(MachinaFluidStorage::getTankCapacity).get();
	}

	public int getFluidMB(int tank) {
		return getFluid(tank).getAmount();
	}

	public float getFluidF(int tank) {
		int cap = getTankCapacity(tank);
		if (cap == 0) {
			return 0;
		}
		return (float) getFluidMB(tank) / (float) cap;
	}

	public int getEnergy() {
		return energy;
	}

	protected void setEnergy(int n) {
		this.energy = n;
	}

	public abstract int getMaxEnergy();

	public float getEnergyF() {
		if (this.getMaxEnergy() == 0) {
			return 0;
		}
		return (float) this.getEnergy() / (float) this.getMaxEnergy();
	}

	public int receiveEnergy(Direction from, int maxReceive, boolean simulate) {
		if (energyCap == null || !energyCap.isInput(from)) {
			return 0;
		}
		return receiveEnergy(maxReceive, simulate);
	}

	protected int receiveEnergy(int maxReceive, boolean simulate) {
		int old = getEnergy();
		int received = Math.min(getMaxEnergy() - old, maxReceive);
		if (received > 0) {
			if (!simulate) {
				setEnergy(old + received);
				this.setChanged();
			}
		}
		return received;
	}

	public boolean isEnergyFull() {
		return getEnergy() >= getMaxEnergy();
	}

	public boolean canConsumeEnergy(Direction side) {
		return energyCap != null && energyCap.isOutput(side);
	}

	public int consumeEnergy(int amount) {
		int prev = getEnergy();
		this.setEnergy(prev - amount);
		int next = getEnergy();
		if (next < 0) {
			setEnergy(0);
		} else if (next > getMaxEnergy()) {
			setEnergy(getMaxEnergy());
		}
		this.setChanged();
		return prev - getEnergy();
	}

	public int consumeEnergySim(int amount) {
		int prev = getEnergy();
		int next = prev - amount;
		if (next < 0) {
			next = 0;
		} else if (next > getMaxEnergy()) {
			next = getMaxEnergy();
		}
		return prev - next;
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
		this.items.clear();
		this.itemSides.clear();
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

	public boolean canOpen(Player player) {
		return canUnlock(player, this.lockKey, this.getDisplayName());
	}

	public static boolean canUnlock(Player player, LockCode lock, Component name) {
		if (!player.isSpectator() && !lock.unlocksWith(player.getMainHandItem())) {
			player.displayClientMessage(Component.translatable("container.isLocked", name), true);
			player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
			return false;
		} else {
			return true;
		}
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
		return this.canOpen(player) ? createMenu().apply(id, this.level, this.worldPosition, inv) : null;
	}

	protected abstract QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu();

	public void tick() {
		BlockState state = getBlockState();
		if (state.hasProperty(BlockProperties.LIT)) {
			if (this.level != null) {
				this.level.setBlock(worldPosition, state.setValue(BatteryBlock.LIT, isLit()), 3);
			}
		}

		if (this.getEnergy() > this.getMaxEnergy()) {
			this.setEnergy(this.getMaxEnergy());
			this.setChanged();
		}
	}

	public byte[] getSideConfig(SidedStorage storage) {
		return storage.getRawSideData();
	}

	public void updateSideConfig(String id, byte[] side) {
		byte[] old = null;

		if (id.startsWith("cap_energy")) {
			old = this.energyCap.getRawSideData();
			this.energyCap.setRawSideData(side);
		} else if (id.startsWith("cap_fluid_")) {
			int tank = Integer.parseInt(id.substring(10));
			old = this.fluidsCap.get(tank).getRawSideData();
			this.fluidsCap.get(tank).setRawSideData(side);
		} else if (id.startsWith("cap_item_")) {
			int slot = Integer.parseInt(id.substring(9));
			old = Side.getRaw(this.itemSides.get(slot));
			Side.fromRaw(this.itemSides.get(slot), side);
		}

		this.setChanged();

		if (old != null) {
			for (Direction d : Direction.values()) {
				int i = d.ordinal();
				if (old[i] != side[i]) {
					BlockPos pos = worldPosition.relative(d);
					BlockState state = null;
					if (level != null) {
						state = level.getBlockState(pos);
					}
					if (state != null) {
						state = state.updateShape(d.getOpposite(), getBlockState(), level, pos, worldPosition);
					}
					if (state != null) {
						level.setBlock(pos, state, 3);
					}
				}
			}
		}
	}

}