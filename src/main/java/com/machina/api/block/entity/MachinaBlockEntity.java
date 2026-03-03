package com.machina.api.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.machina.api.cap.energy.MachinaEnergyStorage;
import com.machina.api.cap.fluid.MachinaTank;
import com.machina.api.cap.sided.ISideAdapter;
import com.machina.api.cap.sided.MultiSidedStorage;
import com.machina.api.cap.sided.Side;
import com.machina.api.cap.sided.SidedStorage;
import com.machina.api.util.block.BlockProperties;
import com.machina.block.machine.BatteryBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * Abstract class to allow Machina BlockEntities to store items, fluids and
 * energy.
 *
 * @author Cy4Shot
 * @since Machina v0.1.0
 */
public abstract class MachinaBlockEntity extends ContainerBlockEntity {

	protected MultiSidedStorage<MachinaEnergyStorage> energyCap;
	protected NonNullList<Side[]> itemSides = NonNullList.create();
	protected final NonNullList<Side[]> fluidSides = NonNullList.create();

	private int energy;
	private final NonNullList<MachinaTank> tanks = NonNullList.create();

	public abstract void createStorages();

	public int itemStorage(Side[] sides) {
		this.itemStorage();
		this.itemSides.add(sides.clone());
		return this.itemSides.size() - 1;
	}

	public void energyStorage(Side[] sides) {
		this.energyCap = new MultiSidedStorage<>("cap_energy", this, MachinaEnergyStorage::new, sides.clone());
	}

	public int fluidStorage(int capacity, Predicate<FluidStack> validator, Side[] sides) {
		this.tanks.add(new MachinaTank(this, capacity, validator, this.tanks.size(), this::sync));
		this.fluidSides.add(sides.clone());
		return this.fluidSides.size() - 1;
	}

	public MachinaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.createStorages();
	}

	public void forEachStorage(Consumer<SidedStorage> consumer) {
		if (this.energyCap != null)
			consumer.accept(energyCap);
	}

	public Supplier<ISideAdapter> getEnergyAdapter() {
		return () -> this.energyCap;
	}

	public Supplier<ISideAdapter> getFluidAdapter(int tank) {
		return () -> new ISideAdapter() {
			@Override
			public Side get(Direction d) {
				return MachinaBlockEntity.this.fluidSides.get(tank)[d.ordinal()];
			}

			@Override
			public void cycle(Direction d) {
				Side.cycle(MachinaBlockEntity.this.fluidSides.get(tank), d, MachinaBlockEntity.this,
						"cap_fluid_" + tank);
			}
		};
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

	public IEnergyStorage getEnergyStorage(Direction side) {
        if (this.energyCap == null) {
            return null;
        }

		return this.energyCap.getCap(side);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		forEachStorage(s -> s.loadAdditional(tag));
		this.energy = tag.getInt("energy");
		this.itemSides = NonNullList.create();
		ListTag sides = tag.getList("sides_item", Tag.TAG_COMPOUND);
		for (int i = 0; i < sides.size(); i++) {
			this.itemSides.add(Side.deserialize(sides.getCompound(i)));
		}
		ListTag fluidSides = tag.getList("sides_fluid", Tag.TAG_COMPOUND);
		for (int i = 0; i < fluidSides.size(); i++) {
			this.fluidSides.add(Side.deserialize(fluidSides.getCompound(i)));
		}
		ListTag tanks = tag.getList("tanks", Tag.TAG_COMPOUND);
		for (int i = 0; i < tanks.size(); i++) {
			this.tanks.get(i).readFromNBT(registries, tanks.getCompound(i));
		}
		this.setChanged();

	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		forEachStorage(s -> s.saveAdditional(tag));
		tag.putInt("energy", energy);
		ListTag sides = new ListTag();
		for (Side[] itemSide : this.itemSides) {
			sides.add(Side.serialize(itemSide));
		}
		tag.put("sides_item", sides);
		ListTag fluidSides = new ListTag();
		for (Side[] fluidSide : this.fluidSides) {
			fluidSides.add(Side.serialize(fluidSide));
		}
		tag.put("sides_fluid", fluidSides);
		ListTag tanks = new ListTag();
		for (MachinaTank tank : this.tanks) {
			CompoundTag tankTag = new CompoundTag();
			tank.writeToNBT(registries, tankTag);
			tanks.add(tankTag);
		}
		tag.put("tanks", tanks);
		super.saveAdditional(tag, registries);
	}

	public boolean isLit() {
		return false;
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
	public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction face) {
		return itemSides.get(slot)[face.ordinal()] == Side.OUTPUT;
	}

	public FluidStack getFluid(int tank) {
		return this.tanks.get(tank).getFluid();
	}

	public int getTankCapacity(int tank) {
		return this.tanks.get(tank).getCapacity();
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

	public int getTanks() {
		return this.tanks.size();
	}

	public List<MachinaTank> getAllTanks() {
		return this.tanks;
	}

	public MachinaTank getTank(int id) {
		return this.tanks.get(id);
	}

	@SuppressWarnings("removal")
	public boolean hasFluid(FluidStack other) {
		for (int i = 0; i < this.getTanks(); i++) {
			FluidStack a = this.getFluid(i);
			if (this.getFluid(i).isFluidEqual(other) && other.getAmount() <= a.getAmount()) {
				return true;
			}
		}
		return false;
	}

	public int fill(int tank, FluidStack resource, FluidAction action) {
		return this.tanks.get(tank).fill(resource, action);
	}

	public @NotNull FluidStack drain(int tank, int amount, FluidAction action) {
		return this.tanks.get(tank).drain(amount, action);
	}

	public int fill(Direction dir, FluidStack resource, FluidAction action) {
		for (MachinaTank tank : this.tanks) {
			if (fluidSides.get(tank.id)[dir.ordinal()].isInput()) {
				if (tank.fill(resource, FluidAction.SIMULATE) > 0) {
					return tank.fill(resource, action);
				}
			}
		}
		return 0;
	}

	public FluidStack drain(Direction dir, FluidStack resource, FluidAction action) {
		for (MachinaTank tank : this.tanks) {
			if (fluidSides.get(tank.id)[dir.ordinal()].isOutput()) {
				if (!tank.drain(resource, FluidAction.SIMULATE).isEmpty()) {
					return tank.drain(resource, action);
				}
			}
		}
		return FluidStack.EMPTY;
	}

	public FluidStack drain(Direction dir, int maxDrain, FluidAction action) {
		for (MachinaTank tank : this.tanks) {
			if (fluidSides.get(tank.id)[dir.ordinal()].isOutput()) {
				if (!tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty()) {
					return tank.drain(maxDrain, action);
				}
			}
		}
		return FluidStack.EMPTY;
	}

	public void setFluid(int tank, FluidStack stack) {
		this.tanks.get(tank).setFluid(stack);
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
	public boolean hasItemIO() {
		return true;
	}

	@Override
	public void clearContent() {
		super.clearRemoved();
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
			old = Side.getRaw(this.fluidSides.get(tank));
			Side.fromRaw(this.fluidSides.get(tank), side);
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