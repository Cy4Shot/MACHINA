package com.machina.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class UIHelper {

	public static final Minecraft mc = Minecraft.getInstance();

	public static void drawStringShadow(GuiGraphics gui, String text, float x, float y, int col) {
		gui.drawString(mc.font, text, (int) x + 2, (int) y + 2, 0);
		gui.drawString(mc.font, text, (int) x, (int) y, col);
	}
	
	public static void drawStringShadow(GuiGraphics gui, Component text, float x, float y, int col) {
		gui.drawString(mc.font, text, (int) x + 2, (int) y + 2, 0);
		gui.drawString(mc.font, text, (int) x, (int) y, col);
	}
}
