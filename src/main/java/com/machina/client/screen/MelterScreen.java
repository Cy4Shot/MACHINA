package com.machina.client.screen;

import com.machina.block.container.MelterContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class MelterScreen extends NoJeiContainerScreen<MelterContainer> {
	public MelterScreen(MelterContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - getXSize()) / 2;
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);

		// Render
		super.render(stack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(stack, pMouseX, pMouseY);
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
	}

	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {
		UIHelper.bindScifi();

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		UIHelper.blit(stack, x, y, 2, 3, xSize, ySize);

		// Fluids
		UIHelper.blit(stack, x + 50, y + 60, 3, 230, 67, 18);
		UIHelper.blit(stack, x + 118, y + 60, 3, 230, 67, 18);
//		UIHelper.renderFluid(stack, f1, x + 51, y + 61, p1, 15, 64, 15, getBlitOffset(), pX, pY, true);
//		UIHelper.renderFluid(stack, f2, x + 119, y + 61, p2, 15, 64, 15, getBlitOffset(), pX, pY, true);

		UIHelper.drawStringWithBorder(stack, "MACHINA://STATE_CONV/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
