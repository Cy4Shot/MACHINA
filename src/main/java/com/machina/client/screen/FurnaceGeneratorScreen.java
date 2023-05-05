package com.machina.client.screen;

import com.machina.block.container.FurnaceGeneratorContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class FurnaceGeneratorScreen extends NoJeiContainerScreen<FurnaceGeneratorContainer> {

	public FurnaceGeneratorScreen(FurnaceGeneratorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
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
		UIHelper.blit(stack, x + 32, y + 102, 3, 200, 174, 30);

		// Progress
		int percentage = (int) (this.menu.te.getEnergyProp() * 129f);
		UIHelper.blit(stack, x + 50, y + 31, 3, 130, 135, 18);
		UIHelper.blit(stack, x + 52, y + 33, 3, 103, percentage, 12);
		
		// Slot
		UIHelper.blit(stack, x + 108, y + 50, 228, 184, 19, 19);
		UIHelper.bindPrgrs();
		UIHelper.blit(stack, x + 103, y + 51, 0, 239, 29, 17);
		UIHelper.blit(stack, x + 83, y + 48, 29, 243, 68, 13);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF") + " - "
						+ String.format("%.01f", this.menu.te.getEnergyProp() * 100f) + "%",
				x + 117, y + 15, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://FURNACE_GEN/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
