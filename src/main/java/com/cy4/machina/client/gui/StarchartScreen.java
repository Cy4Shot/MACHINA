package com.cy4.machina.client.gui;

import com.cy4.machina.client.util.Rectangle;
import com.cy4.machina.client.util.UIHelper;
import com.cy4.machina.client.util.UIHelper.StippleType;
import com.cy4.machina.starchart.Starchart;
import com.cy4.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StarchartScreen extends Screen {

	public static final ResourceLocation SC_BG = new MachinaRL("textures/gui/starchart/starchart_bg.png");
	public static final ResourceLocation SC_RS = new MachinaRL("textures/gui/starchart/starchart_rs.png");

	Starchart sc;

	public StarchartScreen(Starchart sc) {
		super(new TranslationTextComponent("machina.screen.starchart.title"));
		this.sc = sc;
	}

	public Rectangle getContainerBounds() {
		Rectangle bounds = new Rectangle();
		bounds.x0 = (int) (width * 0.2);
		bounds.y0 = (int) (height * 0.2);
		bounds.x1 = (int) (width * 0.8);
		bounds.y1 = (int) (height * 0.8);
		return bounds;
	}

	@Override
	public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(matrixStack, pMouseX, pMouseY, pPartialTicks); // Buttons

		Rectangle bound = this.getContainerBounds();

		// Background
		UIHelper.renderOverflowHidden(matrixStack, this::renderContainerBackground, (ms) -> ms.toString());

		// Elements
		renderPlanetNode(matrixStack, pMouseX, pMouseY);

		// Border
		UIHelper.renderContainerBorder(matrixStack, bound);
		UIHelper.drawStringWithBorder(matrixStack, this.title.getString(), bound.x0, bound.y0 - 12, 0xFF_cc00ff,
				0xFF_0e0e0e);
	}

	private void renderContainerBackground(MatrixStack matrixStack) {
		assert this.minecraft != null;

		this.minecraft.getTextureManager().bind(SC_BG);

		Rectangle containerBounds = getContainerBounds();

		int textureSize = 1536;
		int currentX = containerBounds.x0;
		int currentY = containerBounds.y0;
		int uncoveredWidth = containerBounds.getWidth();
		int uncoveredHeight = containerBounds.getHeight();
		while (uncoveredWidth > 0) {
			while (uncoveredHeight > 0) {
				UIHelper.blit(matrixStack, currentX, currentY, textureSize * 1, 0,
						Math.min(textureSize, uncoveredWidth), Math.min(textureSize, uncoveredHeight));
				uncoveredHeight -= textureSize;
				currentY += textureSize;
			}

			// Decrement
			uncoveredWidth -= textureSize;
			currentX += textureSize;

			// Reset
			uncoveredHeight = containerBounds.getHeight();
			currentY = containerBounds.y0;
		}
	}

	private void renderPlanetNode(MatrixStack matrixStack, float mouseX, float mouseY) {
		assert this.minecraft != null;
		this.minecraft.getTextureManager().bind(SC_RS);
		Rectangle containerBounds = getContainerBounds();

		float offsetC = (float) (8 * Math.cos(this.minecraft.levelRenderer.ticks / 10f) + 8);
		float offsetS = (float) (8 * Math.sin(this.minecraft.levelRenderer.ticks / 10f) + 8);
		int textureSize = 3;

		UIHelper.betterBlit(matrixStack, mouseX + offsetC / 8f, mouseY + offsetS / 8f, textureSize * (int) offsetC, 0,
				textureSize, textureSize, 128);
		UIHelper.line(matrixStack, containerBounds.x0, containerBounds.y0, mouseX + offsetC / 8f, mouseY + offsetS / 8f,
				0xFFFFFFFF, 3f, StippleType.DASHED);
		UIHelper.line(matrixStack, containerBounds.x0, containerBounds.y1, mouseX + offsetC / 8f, mouseY + offsetS / 8f,
				0xFFFFFFFF, 3f, StippleType.DASHED);
		UIHelper.line(matrixStack, containerBounds.x1, containerBounds.y0, mouseX + offsetC / 8f, mouseY + offsetS / 8f,
				0xFFFFFFFF, 3f, StippleType.DASHED);
		UIHelper.line(matrixStack, containerBounds.x1, containerBounds.y1, mouseX + offsetC / 8f, mouseY + offsetS / 8f,
				0xFFFFFFFF, 3f, StippleType.DASHED);

	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
