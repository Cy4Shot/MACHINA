package com.machina.client.cinema;

import com.machina.client.util.ClientTimer;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.FORGE)
public class CinematicHandler {

	public static final CinematicHandler INSTANCE = new CinematicHandler();
	private static final Minecraft mc = Minecraft.getInstance();
	private Cinematic current = null;
	private int elapsed = 0;

	public void tick(TickEvent.Type type, float partial) {
		if (!mc.isPaused() && type == TickEvent.Type.CLIENT) {
			if (this.isActive()) {
				current.transform(mc.isPaused() ? 0f : partial);

				if (elapsed == 0) {
					current.begin(partial);
				}

				current.onClientTick((float) elapsed / (float) current.getDuration(), partial);

				elapsed++;
				if (elapsed == current.getDuration()) {
					current.finish(partial);
					current = null;
					elapsed = 0;
				}
			}
		}
	}

	public boolean isActive() {
		return current != null;
	}

	public void setCinematic(Cinematic c) {
		if (!isActive())
			this.current = c;
	}

	@SubscribeEvent
	public static void onPlayerRender(RenderPlayerEvent.Pre event) {
		if (event.getPlayer() == mc.player && INSTANCE.isActive())
			event.setCanceled(true);
	}

	public static void setup() {
		MinecraftForge.EVENT_BUS.addListener((TickEvent e) -> {
			if (e.phase == TickEvent.Phase.START && e.side == LogicalSide.CLIENT)
				INSTANCE.tick(e.type, ClientTimer.partialTick);
		});
	}
}