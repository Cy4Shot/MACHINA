package com.machina.events;

import com.machina.Machina;
import com.machina.network.BaseNetwork;
import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CResearchToast;
import com.machina.registration.Registration;
import com.machina.research.Research;
import com.machina.research.ResearchTree;
import com.machina.util.server.ServerHelper;
import com.machina.world.data.ResearchData;
import com.machina.world.data.StarchartData;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class CommonForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Registration.TRAIT_POOL_MANAGER);
	}

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent e) {
		if (e.getEntity().level.isClientSide())
			return;
		MinecraftServer server = e.getEntity().getServer();
		ServerPlayerEntity player = (ServerPlayerEntity) e.getPlayer();
		StarchartData.getDefaultInstance(server).syncClient(player);
		ResearchData.getDefaultInstance(server).syncClient(player);
	}

	@SubscribeEvent
	public static void onLivingTick(final PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START))
			return;

		if (!(event.player instanceof ServerPlayerEntity))
			return;

		ServerPlayerEntity player = (ServerPlayerEntity) event.player;
		if (player.tickCount % 20 != 0)
			return;

		ResearchTree tree = ResearchData.getResearchForPlayer(ServerHelper.server(), player);
		for (Research r : tree.unlockedResearches()) {
			if (r.isComplete(player.inventory)) {
				tree.complete(r);
				ResearchData.getDefaultInstance(ServerHelper.server()).setDirty();
				BaseNetwork.sendTo(MachinaNetwork.CHANNEL, new S2CResearchToast(r.getId()), player);
			}
		}
	}

	// Debug Event.
	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if (event.getEntity().level.isClientSide())
			return;
	}
}
