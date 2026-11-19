package com.machina.block.entity.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.item.ConduitItemStorage;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedItemConduitBlockEntity extends ConnectorBlockEntity<ItemStack, ConduitItemStorage> {

	public AdvancedItemConduitBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public AdvancedItemConduitBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ADVANCED_ITEM_CONDUIT.get(), pos, state);
	}

	@Override
	public int getRate() {
		return CommonConfig.advancedConduitTransferRate.get();
	}

	@Override
	public ConduitItemStorage createStorage(Direction side) {
		return new ConduitItemStorage(this, side);
	}

	@Override
	public int slotsPerSide() {
		return 1;
	}
}
