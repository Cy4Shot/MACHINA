package com.machina.client.cinema.effect;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import nick1st.fancyvideo.api.MediaPlayer;
import nick1st.fancyvideo.api.MediaPlayers;

public class OverlayVideoEffect {

	public static final Minecraft mc = Minecraft.getInstance();

	public static boolean render = false;
	public static ResourceLocation rl = null;
	public static float opacity = 0f;

	private static int id = 0;

	public static void setup(ResourceLocation r, float o) {
		rl = r;
		opacity = 0;
		id = MediaPlayer.getNew();
		MediaPlayers.getPlayer(id)
				.prepare("https://download1501.mediafire.com/qe7953n1fdrg/yslfmhttymn3sfl/machinalogo.mp4");
		MediaPlayers.getPlayer(id).volume(100);
		MediaPlayers.getPlayer(id).playPrepared();
	}

	public static void video(RenderGameOverlayEvent.Pre event) {
		if (!render)
			return;

		int width = mc.getWindow().getGuiScaledWidth();
		int height = mc.getWindow().getGuiScaledHeight();

		MediaPlayers.getPlayer(id).bindFrame();
		RenderSystem.enableBlend();
		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		AbstractGui.blit(event.getMatrixStack(), 0, 0, 0.0F, 0.0F, width, height, width, height);
		RenderSystem.disableBlend();
	}
}