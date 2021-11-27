/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.init;

import static com.machina.Machina.MACHINA_ITEM_GROUP;

import com.machina.api.registry.annotation.RegisterBlockItem;
import com.machina.api.registry.annotation.RegistryHolder;

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
