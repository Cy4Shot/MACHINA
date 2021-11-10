package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.starchart.Starchart;
import com.google.common.collect.Lists;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {
	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Machina.traitPoolManager);
	}

	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if (event.getPlayer().level instanceof ServerWorld) {
			new Starchart(Lists.newArrayList(ServerLifecycleHooks.getCurrentServer().getAllLevels()).get(0).getSeed())
					.debugStarchart();
		}
	}
}
