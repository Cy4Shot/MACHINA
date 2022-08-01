package com.machina.client.cinema;

import com.machina.Machina;
import com.machina.client.cinema.effect.OverlayEffect;
import com.machina.client.util.ClientTimer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
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
		if (!mc.isPaused()) {
			if (this.isActive()) {
				mc.setScreen(null);
				if (type == TickEvent.Type.CLIENT) {
					current.transform(mc.isPaused() ? 0f : partial);

					if (elapsed == 0)
						current.begin(partial);

					current.onClientTick(elapsed, partial);

					elapsed++;
					if (elapsed == current.getDuration()) {
						current.finish(partial);
						current = null;
						elapsed = 0;
					}
				} else if (type == TickEvent.Type.RENDER) {
					current.onRenderTick(elapsed, partial);
				}
			}
		}
	}

	public boolean isActive() {
		return current != null;
	}

	public void setCinematic(Cinematic c) {
		if (!isActive()) {
			mc.setScreen(null);
			this.current = c;
		}
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

		MinecraftForge.EVENT_BUS.addListener((RenderGameOverlayEvent.Pre e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				OverlayEffect.overlay(e);
				e.setCanceled(true);
			}
		});

		MinecraftForge.EVENT_BUS.addListener((GuiOpenEvent e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				if (e.getGui() != null || e.getGui() instanceof IngameMenuScreen) {
					e.setCanceled(true);
				}
			}
		});

		MinecraftForge.EVENT_BUS.addListener((RenderHandEvent e) -> {
			if (INSTANCE.isActive() && !mc.isPaused()) {
				e.setCanceled(true);
			}
		});
		
		Machina.LOGGER.info("Cinematics set up.");
	}
}