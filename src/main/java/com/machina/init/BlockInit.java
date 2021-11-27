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

import com.machina.api.registry.annotation.RegisterBlock;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.block.AnimatedBuilder;
import com.machina.block.AnimatedBuilderMount;
import com.machina.block.ConsoleBlock;
import com.machina.block.PadSizeRelay;
import com.machina.block.PumpBlock;
import com.machina.block.RocketBlock;
import com.machina.block.RocketMount;
import com.machina.block.TankBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

@RegistryHolder
public class BlockInit {

	@RegisterBlock("rocket_platform_block")
	public static final Block ROCKET_PLATFORM_BLOCK = new Block(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

	@RegisterBlock("animated_builder_mount")
	public static final Block ANIMATED_BUILDER_MOUNT = new AnimatedBuilderMount(
			AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

	@RegisterBlock("animated_builder")
	public static final Block ANIMATED_BUILDER = new AnimatedBuilder(
			AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

	@RegisterBlock("pad_size_relay")
	public static final Block PAD_SIZE_RELAY = new PadSizeRelay(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

	@RegisterBlock("pad_console")
	public static final Block CONSOLE = new ConsoleBlock(
			AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE).noOcclusion());

	@RegisterBlock("rocket")
	public static final Block ROCKET = new RocketBlock(
			AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE).noOcclusion());

	@RegisterBlock("rocket_mount")
	public static final Block ROCKET_MOUNT = new RocketMount(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

	@RegisterBlock("tank")
	public static final TankBlock TANK = new TankBlock(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK));

	@RegisterBlock("pump")
	public static final PumpBlock PUMP = new PumpBlock();
}
