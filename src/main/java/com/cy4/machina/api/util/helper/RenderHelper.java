package com.cy4.machina.api.util.helper;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;

public class RenderHelper {

	public static void resetColour() {
		RenderSystem.color4f(1, 1, 1, 1);
	}
	
    public static float getPartialTick() {
        return Minecraft.getInstance().getFrameTime();
    }

}
