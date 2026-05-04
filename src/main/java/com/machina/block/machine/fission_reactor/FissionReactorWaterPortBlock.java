package com.machina.block.machine.fission_reactor;

import com.machina.api.block.MultiblockBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.fission_reactor.FissionReactorWaterPortBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandler;

public class FissionReactorWaterPortBlock extends MultiblockBlock {

	public FissionReactorWaterPortBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return FissionReactorWaterPortBlockEntity.class;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.FISSION_REACTOR_WATER_PORT.get();
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return null;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(FissionReactorWaterPortBlock::new);
	}
}
