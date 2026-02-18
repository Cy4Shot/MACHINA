package com.machina.block.connector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.machina.api.block.ConnectorBlock;
import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.item.ConduitItemStorage;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.reflect.QuintFunction;
import com.machina.block.menu.connector.ItemConduitMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemConduitBlock extends ConnectorBlock {

	private static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

	public ItemConduitBlock() {
		super(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(1f).sound(SoundType.METAL).noOcclusion());
	}

	@Override
	public boolean canConnect(Level level, BlockPos pos, Direction dir) {
		return BlockHelper.hasItem(level, pos, dir);
	}

	@Override
	protected Map<BlockPos, Set<BlockPos>> getCache() {
		return CACHE;
	}

	@Override
	protected BlockEntityType<? extends ConnectorBlockEntity<ItemStack, ConduitItemStorage>> getBlockEntityType() {
		return BlockEntityInit.ITEM_CONDUIT.get();
	}

	@Override
	public QuintFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, Direction, AbstractContainerMenu> getMenu() {
		return ItemConduitMenu::new;
	}
}
