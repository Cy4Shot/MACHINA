/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.events;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public abstract class CraftingScreenEvent extends Event {

	private final CraftingScreen screen;
	private final Minecraft minecraft;

	protected CraftingScreenEvent(CraftingScreen screen, Minecraft minecraft) {
		this.screen = screen;
		this.minecraft = minecraft;
	}

	public CraftingScreen getScreen() { return screen; }

	public Minecraft getMinecraft() { return minecraft; }

	public static class RenderBackground extends CraftingScreenEvent {

		public final MatrixStack matrixStack;
		public final float partialTicks;
		public final int x;
		public final int y;

		private RenderBackground(CraftingScreen screen, Minecraft minecraft, MatrixStack matrixStack,
				float partialTicks, int x, int y) {
			super(screen, minecraft);
			this.matrixStack = matrixStack;
			this.partialTicks = partialTicks;
			this.x = x;
			this.y = y;
		}

	}

	public static class Render extends CraftingScreenEvent {

		public final MatrixStack matrixStack;
		public final float partialTicks;
		public final int x;
		public final int y;

		private Render(CraftingScreen screen, Minecraft minecraft, MatrixStack matrixStack, float partialTicks, int x,
				int y) {
			super(screen, minecraft);
			this.matrixStack = matrixStack;
			this.partialTicks = partialTicks;
			this.x = x;
			this.y = y;
		}

	}

	public static void onRenderBg(CraftingScreen screen, Minecraft minecraft, MatrixStack matrixStack,
			float partialTicks, int x, int y) {
		MinecraftForge.EVENT_BUS.post(new RenderBackground(screen, minecraft, matrixStack, partialTicks, x, y));
	}

	public static void onRender(CraftingScreen screen, Minecraft minecraft, MatrixStack matrixStack, float partialTicks,
			int x, int y) {
		MinecraftForge.EVENT_BUS.post(new Render(screen, minecraft, matrixStack, partialTicks, x, y));
	}

}
