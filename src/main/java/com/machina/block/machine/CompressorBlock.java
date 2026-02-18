package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.block.menu.CompressorMenu;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandler;

public class CompressorBlock extends LitMachineBlock {

	public CompressorBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.COMPRESSOR.get();
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return CompressorBlockEntity.class;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(CompressorBlock::new);
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return CompressorMenu::new;
	}
}