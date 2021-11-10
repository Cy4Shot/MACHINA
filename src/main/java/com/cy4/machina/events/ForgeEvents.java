package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.network.MachinaNetwork;
import com.cy4.machina.network.message.to_server.NotifySendTraitsMessage;

import net.minecraft.client.world.ClientWorld;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {
	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Machina.traitPoolManager);
	}

//	@SubscribeEvent
//	public static void debug(ItemTossEvent event) {
//		if (event.getPlayer().level instanceof ServerWorld) {
//			ServerWorld world = DynamicDimensionHelper.createPlanet(event.getPlayer().getServer(), "0");
//
//			DynamicDimensionHelper.sendPlayerToDimension((ServerPlayerEntity) event.getPlayer(), world,
//					event.getPlayer().position());
//		}
//	}
	
	@SubscribeEvent
	public static void onWorldLoaded(WorldEvent.Load event) {
		if (event.getWorld().isClientSide()) {
			MachinaNetwork.CHANNEL.sendToServer(new NotifySendTraitsMessage((ClientWorld) event.getWorld()));
		}
	}
}
