package com.cy4.machina.events;

import static com.cy4.machina.Machina.MOD_ID;

import com.cy4.machina.api.planet.attribute.PlanetAttributeType;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.nbt.IntNBT;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = MOD_ID, bus = Bus.MOD)
public class RegistryEvents {
	
	@SuppressWarnings("unchecked")
	public static final PlanetAttributeType<Integer> TEST_ATTRIBUTE_TYPE = (PlanetAttributeType<Integer>) new PlanetAttributeType<>("C", IntNBT::valueOf, nbt -> {
		if (nbt instanceof IntNBT) {
			return ((IntNBT) nbt).getAsInt();
		}
		return 0;
	}).setRegistryName(new MachinaRL("test"));
	
	@SubscribeEvent
	public static void registerPlanetAttributeType(final RegistryEvent.Register<PlanetAttributeType<?>> event) {
		event.getRegistry().register(TEST_ATTRIBUTE_TYPE);
	}

}
