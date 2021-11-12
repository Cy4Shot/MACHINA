package com.cy4.machina.client;

import com.cy4.machina.api.planet.trait.PlanetTraitSpriteUploader;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("resource")
public class ClientSetup {

	public ClientSetup(IEventBus modBus) {
		modBus.addListener(this::onBlockColourHandler);
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
	
}
