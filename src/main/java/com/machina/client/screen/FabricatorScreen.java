package com.machina.client.screen;

import com.machina.block.container.FabricatorContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.registration.init.ItemInit;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class FabricatorScreen extends NoJeiContainerScreen<FabricatorContainer> {

	public FabricatorScreen(FabricatorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public int getSlotColor(int index) {
		return 0x44_FFFFFF;
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

		// Back
		int xSize = 237, ySize = 201;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		UIHelper.bindBlprt();
		UIHelper.blit(stack, x, y, 0, 72, xSize, ySize - 17);
		UIHelper.bindScifi();
		UIHelper.blit(stack, x + 32, y + 187, 3, 200, 174, 30);
		
		// Slots
		for (int sy = 0; sy < 4; sy++) {
			for (int sx = 0; sx < 4; sx++) {
				UIHelper.blit(stack, x + 20 + sx * 20, y + 40 + sy * 20, 228, 184, 19, 19);
			}
		}
		UIHelper.blit(stack, x + 130, y + 70, 228, 184, 19, 19);
		UIHelper.blit(stack, x + 190, y + 70, 228, 184, 19, 19);
		
		// Deco
		UIHelper.bindPrgrs();
		UIHelper.blit(stack, x + 115, y + 24, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 59, y + 25, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 121, y + 25, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 125, y + 71, 0, 239, 29, 17);
		UIHelper.blit(stack, x + 185, y + 71, 0, 239, 29, 17);
		UIHelper.blit(stack, x + 167, y + 74, 121, 246, 6, 10);
		UIHelper.blit(stack, x + 107, y + 74, 101, 246, 10, 10);
		UIHelper.renderTintedItem(stack, ItemInit.BLUEPRINT.get().getDefaultInstance(), x + 131, y + 71, 35, 35, 35, 0.8f);
		
		UIHelper.drawCenteredStringWithBorder(stack, "Fabrication Bench", x + xSize / 2, y + 12, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://FABRICATION_BENCH/", x + 8, y + 170, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pB) {
		return super.mouseReleased(pX, pY, pB);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
