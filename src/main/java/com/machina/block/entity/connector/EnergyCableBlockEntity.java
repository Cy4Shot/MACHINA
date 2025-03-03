package com.machina.block.entity.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.energy.CableEnergyStorage;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class EnergyCableBlockEntity extends ConnectorBlockEntity<Integer, CableEnergyStorage> {

	public EnergyCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public EnergyCableBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ENERGY_CABLE.get(), pos, state);
	}

	@Override
	public int getRate() {
		return CommonConfig.cableTransferRate.get();
	}

	@Override
	public CableEnergyStorage createStorage(Direction side) {
		return new CableEnergyStorage(this, side);
	}

	@Override
	public Capability<?> getCapability() {
		return ForgeCapabilities.ENERGY;
	}

	@Override
	public QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> getMenu() {
		return QuadFunction.none();
	}
}
