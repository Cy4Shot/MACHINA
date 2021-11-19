package com.cy4.machina.init;

import static com.cy4.machina.Machina.MACHINA_ITEM_GROUP;

import com.cy4.machina.api.annotation.registries.RegisterBlockItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

@RegistryHolder
public final class BlockItemInit {

	public static final Block[] AUTO_BLOCK_ITEMS = new Block[] {};

	@RegisterBlockItem
	public static final BlockItem ROCKET_PLATFORM_BLOCKITEM = new BlockItem(BlockInit.ROCKET_PLATFORM_BLOCK,
			new Item.Properties().tab(MACHINA_ITEM_GROUP));

	@RegisterBlockItem
	public static final BlockItem ANIMATED_BUILDER_MOUNT_BLOCKITEM = new BlockItem(BlockInit.ANIMATED_BUILDER_MOUNT,
			new Item.Properties().tab(MACHINA_ITEM_GROUP));

	@RegisterBlockItem
	public static final BlockItem ANIMATED_BUILDER_BLOCKITEM = new BlockItem(BlockInit.ANIMATED_BUILDER,
			new Item.Properties().tab(MACHINA_ITEM_GROUP));

	@RegisterBlockItem
	public static final BlockItem PAD_SIZE_RELAY_BLOCKITEM = new BlockItem(BlockInit.PAD_SIZE_RELAY,
			new Item.Properties().tab(MACHINA_ITEM_GROUP));

	@RegisterBlockItem
	public static final BlockItem CONSOLE_BLOCKITEM = new BlockItem(BlockInit.CONSOLE,
			new Item.Properties().tab(MACHINA_ITEM_GROUP));

	@RegisterBlockItem
	public static final BlockItem ROCKET_BLOCKITEM = new BlockItem(BlockInit.ROCKET,
			new Item.Properties().tab(MACHINA_ITEM_GROUP));

	@RegisterBlockItem
	public static final BlockItem ROCKET_MOUNT_BLOCKITEM = new BlockItem(BlockInit.ROCKET_MOUNT,
			new Item.Properties().tab(MACHINA_ITEM_GROUP));

	@RegisterBlockItem
	public static final BlockItem PUMP_BLOCK_ITEM = createBlockItem(BlockInit.PUMP);

	@RegisterBlockItem
	public static final BlockItem TANK_BLOCK_ITEM = createBlockItem(BlockInit.TANK);

	private static BlockItem createBlockItem(Block block) {
		return new BlockItem(block, new Item.Properties().tab(MACHINA_ITEM_GROUP));
	}
}
