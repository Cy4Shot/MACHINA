package com.machina.block.entity.machine;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.RocketPartBenchMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RocketPartBenchBlockEntity extends MachinaBlockEntity {

	public RocketPartBenchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public RocketPartBenchBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ROCKET_PART_BENCH.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);

		for (int i = 0; i < 12; i++) {
			itemStorage(Side.NONES);
		}
	}

	@Override
	public boolean hasItemIO() {
		return false;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 10_000_000;
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return RocketPartBenchMenu::new;
	}
}