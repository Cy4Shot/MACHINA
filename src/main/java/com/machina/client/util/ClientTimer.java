package com.machina.client.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public final class ClientTimer {
	public static long gameTick = 0;
	public static float partialTick = 0;
	public static float deltaTick = 0;
	public static float totalTick = 0;

	private static void calcDelta() {
		float oldTotal = totalTick;
		totalTick = gameTick + partialTick;
		deltaTick = totalTick - oldTotal;
	}

	public static void renderTickBegin(float pt) {
		partialTick = pt;
	}

	public static void renderTickFinish() {
		calcDelta();
	}

	public static void endTick(Minecraft mc) {
		gameTick++;
		partialTick = 0;
		calcDelta();
	}

	public static void setup() {
		MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
			if (e.phase == TickEvent.Phase.END)
				endTick(Minecraft.getInstance());
		});

		MinecraftForge.EVENT_BUS.addListener((TickEvent.RenderTickEvent e) -> {
			if (e.phase == TickEvent.Phase.START)
				renderTickBegin(e.renderTickTime);
			else
				renderTickFinish();
		});
	}
}