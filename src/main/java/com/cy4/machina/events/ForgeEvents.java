package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.world.data.StarchartData;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.world.WorldEvent;
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

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if (event.getPlayer().level instanceof ServerWorld) {
			StarchartData.getDefaultInstance(ServerLifecycleHooks.getCurrentServer()).starchart.debugStarchart();
		}
	}

	@SubscribeEvent
	public static void onWorldLoaded(WorldEvent.Load event) {
		if (event.getWorld().isClientSide()) {
//			MachinaNetwork.CHANNEL.sendToServer(new NotifySendTraitsMessage((ClientWorld) event.getWorld()));
		}
	}
}
