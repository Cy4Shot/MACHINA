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

package com.machina.modules.pump;

import static com.machina.api.ModIDs.MACHINA;

import com.machina.api.annotation.RL;
import com.machina.api.module.IModule;
import com.machina.api.module.Module;
import com.machina.api.registry.annotation.AutoBlockItem;
import com.machina.api.registry.annotation.RegisterBlock;
import com.machina.api.registry.annotation.RegisterTileEntityType;

import net.minecraft.tileentity.TileEntityType;

@Module(id = @RL(modid = MACHINA, path = "pump"))
public class PumpModule implements IModule {

	@AutoBlockItem
	@RegisterBlock("pump")
	public static final PumpBlock PUMP_BLOCK = new PumpBlock();
	
	@RegisterTileEntityType("pump")
	public static final TileEntityType<PumpTileEntity> PUMP_TILE_ENTITY_TYPE = TileEntityType.Builder
			.of(PumpTileEntity::new, PUMP_BLOCK).build(null);

}
