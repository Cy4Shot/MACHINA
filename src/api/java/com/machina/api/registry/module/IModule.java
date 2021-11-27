package com.machina.api.registry.module;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IModule {

	default void onCommonSetup(final FMLCommonSetupEvent event) {
		
	}
	
	default void onClientSetup(final FMLClientSetupEvent event) {
		
	}
	
}
