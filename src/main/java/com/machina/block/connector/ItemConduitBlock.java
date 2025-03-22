package com.machina.block.connector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.machina.api.block.ConnectorBlock;
import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.item.ConduitItemStorage;
import com.machina.api.util.block.BlockHelper;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;

public class ItemConduitBlock extends ConnectorBlock {

	private static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

	public ItemConduitBlock() {
		super(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(1f).sound(SoundType.METAL).noOcclusion());
	}

	@Override
	public boolean canConnect(BlockEntity be, Direction dir) {
		return BlockHelper.hasItem(be, dir);
	}

	@Override
	protected Map<BlockPos, Set<BlockPos>> getCache() {
		return CACHE;
	}

	@Override
	protected BlockEntityType<? extends ConnectorBlockEntity<ItemStack, ConduitItemStorage>> getBlockEntityType() {
		return BlockEntityInit.ITEM_CONDUIT.get();
	}

}
