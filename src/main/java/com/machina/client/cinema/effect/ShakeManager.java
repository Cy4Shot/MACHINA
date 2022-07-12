package com.machina.client.cinema.effect;

import java.util.Collection;

import com.machina.network.BaseNetwork;
import com.machina.network.MachinaNetwork;
import com.machina.network.message.S2CShakeScreen;
import com.machina.util.math.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public class ShakeManager {
	private static final Minecraft mc = Minecraft.getInstance();
	private static float intensity;
	private static int duration;

	public static void shake(int shakeForT, float strength) {
		duration = shakeForT;
		intensity = strength;
	}

	public static void tick() {
		if (!mc.isPaused() && mc.screen == null) {
			duration--;
		}
	}

	public static void draw() {
		if (!mc.isPaused() && mc.screen == null && duration > 0) {
			Entity camera = mc.getCameraEntity();
			if (camera != null) {
				camera.xRot = camera.xRot + (float) Math.sin(Math.random() * MathUtil.TWO_PI) * intensity;
				camera.yRot = camera.yRot + (float) Math.sin(Math.random() * MathUtil.TWO_PI) * intensity;
			}
		}
	}

	public static void setup() {
		MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
			if (e.phase == TickEvent.Phase.END)
				tick();
		});

		MinecraftForge.EVENT_BUS.addListener((TickEvent.RenderTickEvent e) -> {
			if (e.phase == TickEvent.Phase.END)
				draw();
		});
	}

	public static void shakePlayerCameras(Collection<ServerPlayerEntity> players, float d, float i) {
		S2CShakeScreen pack = new S2CShakeScreen(i, (int) (d * 20));
		players.forEach(p -> BaseNetwork.sendTo(MachinaNetwork.CHANNEL, pack, p));
	}
}
