/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.api.util;

import java.util.function.BiFunction;

import net.minecraft.util.math.BlockPos;

public enum DiagonalDirection {

	NORTH_WEST("north_west", (originalPos, count) -> originalPos.north(count).west(count)),
	NORTH_EAST("north_east", (originalPos, count) -> originalPos.north(count).east(count)),

	SOUTH_WEST("south_west", (originalPos, count) -> originalPos.south(count).west(count)),
	SOUTH_EAST("south_east", (originalPos, count) -> originalPos.south(count).east(count));

	private BiFunction<BlockPos, Integer, BlockPos> relativeFunc;
	public final String name;

	DiagonalDirection(String name, BiFunction<BlockPos, Integer, BlockPos> relativeFunc) {
		this.relativeFunc = relativeFunc;
		this.name = name;
	}

	public BlockPos relative(BlockPos pos, int count) {
		return relativeFunc.apply(pos, count);
	}

}
