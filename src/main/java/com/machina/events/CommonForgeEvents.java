package com.machina.events;

import com.machina.Machina;
import com.machina.client.ClientResearch;
import com.machina.network.BaseNetwork;
import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CResearchToast;
import com.machina.registration.Registration;
import com.machina.research.Research;
import com.machina.research.ResearchTree;
import com.machina.util.helper.ItemStackHelper;
import com.machina.util.helper.ServerHelper;
import com.machina.world.data.ResearchData;
import com.machina.world.data.StarchartData;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class CommonForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Registration.MULTIBLOCK_MANAGER);
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

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		PlayerEntity player = event.getPlayer();
		boolean client = player.level.isClientSide();

		ItemStack crafted = event.getCrafting();
		IInventory grid = event.getInventory();

		ResearchTree tree = client ? ClientResearch.getResearch()
				: ResearchData.getResearchForPlayer(ServerHelper.server(), (ServerPlayerEntity) player);

		if (tree.canCraft(crafted))
			return;

		if (client) {
			Minecraft mc = Minecraft.getInstance();
			mc.gui.setOverlayMessage(TextComponentUtils.mergeStyles(
					new StringTextComponent("You do not have the research to craft this item."),
					Style.EMPTY.withColor(Color.fromRgb(0xFF_00fefe)).withBold(true)), false);
			mc.setScreen(null);
		}

		for (int i = 0; i < grid.getContainerSize(); i++) {
			ItemStack s = grid.getItem(i);
			if (s != ItemStack.EMPTY) {
				ItemStack d = s.copy();
				d.setCount(1);
				player.drop(d, false, false);
			}
		}

		int s = ItemStackHelper.getSlotFor(player.inventory, crafted);

		if (s != -1) {
			ItemStack stack = player.inventory.getItem(s);
			if (stack.getCount() < crafted.getCount())
				crafted.setCount(stack.getCount());
			stack.shrink(crafted.getCount());

		} else {
			crafted.shrink(crafted.getCount());
		}
	}
}
