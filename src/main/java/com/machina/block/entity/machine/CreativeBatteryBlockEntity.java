package com.machina.block.entity.machine;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.CreativeBatteryMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeBatteryBlockEntity extends MachinaBlockEntity {

	public CreativeBatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public CreativeBatteryBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.CREATIVE_BATTERY.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.OUTPUTS);
	}

	@Override
	public void tick() {
        if (this.level != null && this.level.isClientSide()) return;

        // Send out energy
		BlockHelper.sendEnergy(level, worldPosition, 1_000_000_000, 1_000_000_000, this);

		super.tick();
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return CreativeBatteryMenu::new;
	}

	@Override
	public int getEnergy() {
		return 1_000_000_000;
	}

	@Override
	public int getMaxEnergy() {
		return 1_000_000_000;
	}

	@Override
	protected void setEnergy(int n) {
	}

	@Override
	public int consumeEnergy(int amount) {
		return amount;
	}

	@Override
	public int consumeEnergySim(int amount) {
		return amount;
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
