package com.machina.block.machine;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.ElectricPumpBlockEntity;
import com.machina.block.menu.ElectricPumpMenu;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class ElectricPumpBlock extends MachineBlock {

	public ElectricPumpBlock(Properties props) {
		super(props.noOcclusion().isRedstoneConductor(ElectricPumpBlock::never).isSuffocating(ElectricPumpBlock::never)
				.isViewBlocking(ElectricPumpBlock::never));
	}

	public static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
		return false;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.ELECTRIC_PUMP.get();
	}

	@Override
	protected boolean isTickable() {
		return true;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return ElectricPumpBlockEntity.class;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(ElectricPumpBlock::new);
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return ElectricPumpMenu::new;
	}
}