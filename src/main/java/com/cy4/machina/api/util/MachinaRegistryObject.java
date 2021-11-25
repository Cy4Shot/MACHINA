package com.cy4.machina.api.util;

import java.util.function.Supplier;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class MachinaRegistryObject<T extends IForgeRegistryEntry<?>> implements Supplier<T> {

	private final Supplier<T> sup;
	private ResourceLocation name;
	private final IForgeRegistry<? super T> registry;

	public MachinaRegistryObject(Supplier<T> sup, IForgeRegistry<? super T> registry) {
		this.sup = sup;
		this.registry = registry;
	}

	public static <F extends Fluid> MachinaRegistryObject<F> fluid(Supplier<F> sup) {
		return new MachinaRegistryObject<>(sup, ForgeRegistries.FLUIDS);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get() {
		return (T) registry.getValue(name);
	}

	@SuppressWarnings("unused")
	private Supplier<T> getDeclaredValue() { return sup; }

	@SuppressWarnings("unused")
	private void setName(ResourceLocation name) { this.name = name; }

}
