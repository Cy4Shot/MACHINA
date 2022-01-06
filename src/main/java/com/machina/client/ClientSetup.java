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

package com.machina.client;

import static com.machina.api.ModIDs.MACHINA;

import com.machina.api.planet.trait.PlanetTraitSpriteUploader;
import com.machina.client.dimension.CustomDimensionRenderInfo;
import com.machina.client.particle.ElectricitySparkParticle;
import com.machina.init.FluidInit;
import com.machina.init.ParticleTypesInit;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.Fluid;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("resource")
@Mod.EventBusSubscriber(modid = MACHINA, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup extends com.matyrobbrt.lib.ClientSetup {

	public ClientSetup(IEventBus modBus) {
		super(modBus);
		modBus.addListener(this::onBlockColourHandler);

		CustomDimensionRenderInfo.registerDimensionRenderInfo();
	}

	@SubscribeEvent
	public void onBlockColourHandler(ColorHandlerEvent.Block event) {
		Minecraft minecraft = Minecraft.getInstance();
		PlanetTraitSpriteUploader spriteUploader = new PlanetTraitSpriteUploader(minecraft.textureManager);
		IResourceManager resourceManager = minecraft.getResourceManager();
		if (resourceManager instanceof IReloadableResourceManager) {
			IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) resourceManager;
			reloadableResourceManager.registerReloadListener(spriteUploader);
		}
		PlanetTraitSpriteUploader.setInstance(spriteUploader);
	}

	@SubscribeEvent
	public static void registerRenderers(final FMLClientSetupEvent event) {
		translucent(FluidInit.LIQUID_HYDROGEN.get());
		translucent(FluidInit.LIQUID_HYDROGEN_FLOWING.get());
		translucent(FluidInit.LIQUID_HYDROGEN_BLOCK);
	}

	private static void translucent(Fluid fluid) {
		RenderTypeLookup.setRenderLayer(fluid, RenderType.translucent());
	}

	private static void translucent(Block block) {
		RenderTypeLookup.setRenderLayer(block, RenderType.translucent());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onParticleFactoryRegister(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		mc.particleEngine.register(ParticleTypesInit.ELECTRICITY_SPARK, ElectricitySparkParticle.Factory::new);
	}

}
