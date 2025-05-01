package com.machina.block.entity.machine;

import com.google.common.base.Predicates;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.AtmosphericSeparatorMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AtmosphericSeparatorBlockEntity extends MachinaBlockEntity {

	public AtmosphericSeparatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public AtmosphericSeparatorBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ATMOSPHERIC_SEPARATOR.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
		fluidStorage(16_000, Predicates.alwaysTrue(), Side.OUTPUTS);
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
		return AtmosphericSeparatorMenu::new;
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
