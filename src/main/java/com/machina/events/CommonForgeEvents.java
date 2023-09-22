package com.machina.events;

import com.machina.Machina;
import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.registration.Registration;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class CommonForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Registration.MULTIBLOCK_LOADER);
	}

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent e) {
		if (e.getEntity().level().isClientSide())
			return;

		Starchart.syncClient((ServerPlayer) e.getEntity());
	}

	@SubscribeEvent
	public static void onDebug(final ItemTossEvent event) {
		SolarSystem.SOLAR_SYSTEM.debug();
//		System.out.println(new BigDecimal(Double
//				.valueOf(Math.sqrt(
//						38 * Math.pow(10, 25) / (4D * Math.PI * 5.670374419D * Math.pow(10, -8) * Math.pow(5800, 4))))
//				.toString()).stripTrailingZeros().toPlainString());
	}
}
