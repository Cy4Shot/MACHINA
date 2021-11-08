package com.cy4.machina.events;

import com.cy4.machina.Machina;
import com.cy4.machina.world.DynamicDimensionHelper;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {
	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Machina.traitPoolManager);
	}

	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if (event.getPlayer().level instanceof ServerWorld) {
			ServerWorld world = DynamicDimensionHelper.createPlanet(event.getPlayer().getServer(), "0");

			DynamicDimensionHelper.sendPlayerToDimension((ServerPlayerEntity) event.getPlayer(), world,
					event.getPlayer().position());
		}
	}
}
