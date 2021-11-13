package com.cy4.machina.client.gui;

import com.cy4.machina.client.util.Rectangle;
import com.cy4.machina.client.util.UIHelper;
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

	public StarchartScreen() {
		super(new TranslationTextComponent("machina.screen.starchart.title"));
	}

	@Override
	protected void init() {
		super.init();
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

		UIHelper.renderOverflowHidden(matrixStack, this::renderContainerBackground, (ms) -> ms.toString());
		UIHelper.renderContainerBorder(matrixStack, bound, this);
		UIHelper.drawStringWithBorder(matrixStack, this.title.getString(), bound.x0, bound.y0 - 12, 0xFF_cc00ff, 0xFF_0e0e0e);
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
				blit(matrixStack, currentX, currentY, textureSize * 1, 0,
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

}
