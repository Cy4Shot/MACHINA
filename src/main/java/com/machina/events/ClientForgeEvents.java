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

package com.machina.events;

import com.machina.Machina;
import com.machina.api.client.ClientDataHolder;
import com.machina.api.util.Color;
import com.machina.api.util.PlanetUtils;
import com.machina.api.world.data.PlanetData;
import com.machina.init.PlanetAttributeTypesInit;

import net.minecraft.client.Minecraft;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void fogSetup(FogColors event) {
		RegistryKey<World> dim = Minecraft.getInstance().level.dimension();
		if (PlanetUtils.isDimensionPlanet(dim)) {
			PlanetData data = ClientDataHolder.getPlanetDataByID(PlanetUtils.getId(dim));
			Color color = data.getAttribute(PlanetAttributeTypesInit.FOG_COLOUR);
			Float density = data.getAttribute(PlanetAttributeTypesInit.FOG_DENSITY);
			event.setRed(color.getRed() / 255f * density);
			event.setGreen(color.getGreen() / 255f * density);
			event.setBlue(color.getBlue() / 255f * density);
		}
	}
}
