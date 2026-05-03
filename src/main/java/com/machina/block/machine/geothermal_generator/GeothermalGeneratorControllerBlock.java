package com.machina.block.machine.geothermal_generator;

import com.machina.api.block.MultiblockBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.geothermal_generator.GeothermalGeneratorControllerBlockEntity;
import com.machina.block.menu.GeothermalGeneratorMenu;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandler;

public class GeothermalGeneratorControllerBlock extends MultiblockBlock {

	public GeothermalGeneratorControllerBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean isMaster() {
		return true;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return GeothermalGeneratorControllerBlockEntity.class;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.GEOTHERMAL_GENERATOR_CONTROLLER.get();
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return GeothermalGeneratorMenu::new;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(GeothermalGeneratorControllerBlock::new);
	}

	@Override
	protected boolean isTickable() {
		return true;
	}
}
