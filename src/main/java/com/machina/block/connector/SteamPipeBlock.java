package com.machina.block.connector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.machina.api.block.ConnectorBlock;
import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.steam.PipeSteamStorage;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.reflect.QuintFunction;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.items.IItemHandler;

public class SteamPipeBlock extends ConnectorBlock {

	private static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

	public SteamPipeBlock() {
		super(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(1f).sound(SoundType.METAL).noOcclusion());
	}

	@Override
	public boolean canConnect(Level level, BlockPos pos, Direction dir) {
		return BlockHelper.hasSteam(level, pos);
	}

	@Override
	protected Map<BlockPos, Set<BlockPos>> getCache() {
		return CACHE;
	}

	@Override
	protected BlockEntityType<? extends ConnectorBlockEntity<Integer, PipeSteamStorage>> getBlockEntityType() {
		return BlockEntityInit.STEAM_PIPE.get();
	}

	@Override
	public QuintFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, Direction, AbstractContainerMenu> getMenu() {
		return null;
	}
}
