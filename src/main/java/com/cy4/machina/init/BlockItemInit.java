/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
	public static final BlockItem ROCKET_PLATFORM_BLOCKITEM = createBlockItem(BlockInit.ROCKET_PLATFORM_BLOCK);

	@RegisterBlockItem
	public static final BlockItem ANIMATED_BUILDER_MOUNT_BLOCKITEM = createBlockItem(BlockInit.ANIMATED_BUILDER_MOUNT);

	@RegisterBlockItem
	public static final BlockItem ANIMATED_BUILDER_BLOCKITEM = createBlockItem(BlockInit.ANIMATED_BUILDER);

	@RegisterBlockItem
	public static final BlockItem PAD_SIZE_RELAY_BLOCKITEM = createBlockItem(BlockInit.PAD_SIZE_RELAY);

	@RegisterBlockItem
	public static final BlockItem CONSOLE_BLOCKITEM = createBlockItem(BlockInit.CONSOLE);

	@RegisterBlockItem
	public static final BlockItem ROCKET_BLOCKITEM = createBlockItem(BlockInit.ROCKET);

	@RegisterBlockItem
	public static final BlockItem ROCKET_MOUNT_BLOCKITEM = createBlockItem(BlockInit.ROCKET_MOUNT);

	@RegisterBlockItem
	public static final BlockItem PUMP_BLOCK_ITEM = createBlockItem(BlockInit.PUMP);

	@RegisterBlockItem
	public static final BlockItem TANK_BLOCK_ITEM = createBlockItem(BlockInit.TANK);

	private static BlockItem createBlockItem(Block block) {
		return new BlockItem(block, new Item.Properties().tab(MACHINA_ITEM_GROUP));
	}
}
