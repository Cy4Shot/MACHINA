package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class DataMapsInit {

	private static final List<DataMapType<?, ?>> TYPES = new ArrayList<>();

	public static final DataMapType<Fluid, Integer> CHEMICAL_BURNABLE = register("chemical_burnable", Registries.FLUID,
			ExtraCodecs.POSITIVE_INT);

	private static final <T, R> DataMapType<R, T> register(String name, ResourceKey<Registry<R>> registry,
			Codec<T> codec) {
		DataMapType<R, T> type = DataMapType.builder(MachinaRL.create(name), registry, codec).synced(codec, false)
				.build();
		TYPES.add(type);
		return type;
	}
	
	@SubscribeEvent
	public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
	    TYPES.forEach(event::register);
	}
}
