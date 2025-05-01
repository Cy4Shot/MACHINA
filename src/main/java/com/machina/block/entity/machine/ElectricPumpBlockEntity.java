package com.machina.block.entity.machine;

import com.google.common.base.Predicates;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.ElectricPumpMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricPumpBlockEntity extends MachinaBlockEntity {

	public ElectricPumpBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ElectricPumpBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ELECTRIC_PUMP.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
	}

	@Override
	public void tick() {
		if (this.level != null && this.level.isClientSide()) {
			return;
		}

	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return ElectricPumpMenu::new;
	}

	@Override
	public boolean activeModel() {
		return true;
	}

	@Override
	public int getMaxEnergy() {
		// TODO Config
		return 10_000_000;
	}
}
