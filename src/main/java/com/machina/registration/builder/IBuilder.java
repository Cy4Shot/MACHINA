package com.machina.registration.builder;

import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IBuilder<T extends IForgeRegistryEntry<T>, P> {
	
	P build();

}
