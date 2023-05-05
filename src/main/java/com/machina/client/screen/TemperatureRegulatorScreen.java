package com.machina.client.screen;

import com.machina.block.container.TemperatureRegulatorContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.StringUtils;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class TemperatureRegulatorScreen extends NoJeiContainerScreen<TemperatureRegulatorContainer> {
	public TemperatureRegulatorScreen(TemperatureRegulatorContainer pMenu, PlayerInventory pPlayerInventory,
			ITextComponent pTitle) {
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

		// Progress
		int percentage = (int) (this.menu.te.propFull() * 129f);
		UIHelper.blit(stack, x + 50, y + 38, 3, 130, 135, 18);
		UIHelper.blit(stack, x + 52, y + 40, 3, 115, percentage, 12);

		UIHelper.drawCenteredStringWithBorder(stack,
				StringUtils.translateScreen("temperature_regulator.stored")
						+ MathUtil.engineering(this.menu.te.normalizedHeat(), "K"),
				x + 117, y + 26, 0xFF_00fefe, 0xFF_0e0e0e);

		UIHelper.drawStringWithBorder(stack, "MACHINA://TEMP_REG/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
