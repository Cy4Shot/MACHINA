package com.machina.block.machine.steam_turbine;

import com.machina.api.block.MultiblockBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.steam_turbine.SteamTurbineGlassBlockEntity;
import com.machina.block.menu.SteamTurbineMenu;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandler;

public class SteamTurbineGlassBlock extends MultiblockBlock {

	public SteamTurbineGlassBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return SteamTurbineGlassBlockEntity.class;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.STEAM_TURBINE_GLASS.get();
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return SteamTurbineMenu::new;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(SteamTurbineGlassBlock::new);
	}
}
