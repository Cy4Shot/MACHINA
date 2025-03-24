package com.machina.block.entity.machine;

import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.energy.EnergyBlockItemWrapper;
import com.machina.api.cap.sided.Side;
import com.machina.api.client.model.SidedBakedModel;
import com.machina.api.item.EnergyItem;
import com.machina.api.util.ItemStackUtil;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.BatteryMenu;
import com.machina.config.CommonConfig;
import com.machina.item.CapacitorItem;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class BatteryBlockEntity extends MachinaBlockEntity {
	int prev = -1;

	public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public BatteryBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.BATTERY.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		itemStorage(Side.NONES);
		itemStorage(Side.INPUTS);
		itemStorage(Side.OUTPUTS);
	}

	@Override
	public void tick() {
		if (this.level != null && this.level.isClientSide()) {
			super.tick();
			return;
		}

		// Force update energy
		int energy = getEnergy();
		if (energy != prev) {
			this.prev = energy;
			this.setChanged();
		}

		// Power IN
		ItemStack input = getItem(1);
		if (ItemStackUtil.hasEnergy(input)) {
			input.getCapability(ForgeCapabilities.ENERGY).ifPresent(storage -> {
				int extracted = storage.extractEnergy(CommonConfig.batteryChargeRate.get(), true);
				extracted = this.receiveEnergy(extracted, false);
				storage.extractEnergy(extracted, false);
				if (extracted > 0)
					this.setChanged();
			});
		}

		// Power OUT
		ItemStack output = getItem(2);
		if (ItemStackUtil.hasEnergy(output)) {
			output.getCapability(ForgeCapabilities.ENERGY).ifPresent(storage -> {
				int extracted = this.consumeEnergySim(CommonConfig.batteryDischargeRate.get());
				extracted = storage.receiveEnergy(extracted, false);
				this.consumeEnergy(extracted);
				if (extracted > 0)
					this.setChanged();
			});
		}

		// Send out energy
		BlockHelper.sendEnergy(level, worldPosition, energy, CommonConfig.batteryTransferRate.get(), this);

		super.tick();
	}

	@Override
	public boolean isLit() {
		return this.getEnergy() > 0;
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return BatteryMenu::new;
	}

	private <T> T doWithCapacitor(BiFunction<ItemStack, CapacitorItem, T> func, T def) {
		ItemStack cap = getItem(0);
		if (cap.isEmpty() || !(cap.getItem() instanceof CapacitorItem))
			return def;

		return func.apply(cap, (CapacitorItem) cap.getItem());
	}

	@Override
	public int getMaxEnergy() {
		return doWithCapacitor((s, i) -> i.getMaxEnergy(), 0);
	}

	@Override
	public int getEnergy() {
		return doWithCapacitor((s, i) -> EnergyItem.getEnergy(s), 0);
	}

	@Override
	protected void setEnergy(int n) {
		doWithCapacitor((s, i) -> EnergyItem.setEnergy(s, n), false);
	}

	public boolean hasCapacitor() {
		return doWithCapacitor((s, i) -> true, false);
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
		if (side != null) {
			if (cap == ForgeCapabilities.ENERGY) {
				if (energyCap.isNonNullMode(side)) {
					return doWithCapacitor((s, i) -> s.getCapability(ForgeCapabilities.ENERGY, side)
							.lazyMap(EnergyBlockItemWrapper::from).cast(), super.getCapability(cap, side));
				}
			}
		}

		return super.getCapability(cap, side);
	}

	@Override
	public @NotNull ModelData getModelData() {
		return ModelData.builder().with(SidedBakedModel.SIDES, getSideConfig(energyCap)).build();
	}

	@Override
	public boolean activeModel() {
		return true;
	}
}
