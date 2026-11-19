package com.machina.block.machine.steam_turbine;

import com.machina.api.block.MultiblockBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.steam_turbine.SteamTurbineBladeAssemblyBlockEntity;
import com.machina.block.menu.SteamTurbineMenu;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class SteamTurbineBladeAssemblyBlock extends MultiblockBlock {

	public SteamTurbineBladeAssemblyBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return SteamTurbineBladeAssemblyBlockEntity.class;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.STEAM_TURBINE_BLADE_ASSEMBLY.get();
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return SteamTurbineMenu::new;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(SteamTurbineBladeAssemblyBlock::new);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == BlockEntityInit.STEAM_TURBINE_BLADE_ASSEMBLY.get()
				? (lvl, pos, st, be) -> ((SteamTurbineBladeAssemblyBlockEntity) be).tick()
				: null;
	}
}
