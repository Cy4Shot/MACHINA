package com.machina.client.screen;

import com.machina.block.container.ShipConsoleContainer;
import com.machina.client.util.UIHelper;
import com.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ShipConsoleScreen extends ContainerScreen<ShipConsoleContainer> {

	public ShipConsoleScreen(ShipConsoleContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		this.imageWidth = this.width; // Hide JEI!
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);
		super.render(stack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(stack, pMouseX, pMouseY);
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
	}

	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.textureManager.bind(new MachinaRL("textures/gui/scifi_el.png"));
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);
		this.blit(stack, x + 54, y + 40, 3, 130, 135, 18);
		this.blit(stack, x + 30, y + 37, 160, 104, 23, 23);
		this.blit(stack, x + 190, y + 37, 186, 104, 23, 23);
		
		int percentage = (int)((0.5f) * 129f);
		this.blit(stack, x + 56, y + 42, 3, 103, percentage, 12);

		UIHelper.drawStringWithBorder(stack, "Stage 1 / 5", x + 90, y + 4, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://SHIP_CONSOLE/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}
	
	@Override
	public int getXSize() {
		return 176;
	}

}
