/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.util.helper;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;

public class RenderHelper {

	public static void resetColour() {
		RenderSystem.color4f(1, 1, 1, 1);
	}

	public static float getPartialTick() { return Minecraft.getInstance().getFrameTime(); }

}
