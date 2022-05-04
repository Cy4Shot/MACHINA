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

	private static final MachinaRL SCIFI_EL = new MachinaRL("textures/gui/scifi_el.png");

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
	public int getSlotColor(int index) {
		return 0x00_FFFFFF;
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
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.textureManager.bind(SCIFI_EL);

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);

		// Progress
		int percentage = (int) ((0.9f) * 129f);
		this.blit(stack, x + 74, y + 38, 3, 130, 135, 18);
		this.blit(stack, x + 74 + 2, y + 38 + 2, 3, 103, percentage, 12);

		// Slots

		this.blit(stack, x + 20, y + 22, 160, 104, 23, 23);
		this.blit(stack, x + 47, y + 22, 186, 104, 23, 23);
		this.blit(stack, x + 20, y + 49, 160, 129, 23, 23);
		this.blit(stack, x + 47, y + 49, 186, 129, 23, 23);

		if (!this.menu.getSlot(0).hasItem()) {
			UIHelper.renderTintedItem(stack, this.menu.getCompletableSlot(0).getBackground(), x + 24, y + 26, 35, 35,
					35, 0.7f);
		}
		if (!this.menu.getSlot(1).hasItem()) {
			UIHelper.renderTintedItem(stack, this.menu.getCompletableSlot(1).getBackground(), x + 49, y + 26, 35, 35,
					35, 0.7f);
		}
		if (!this.menu.getSlot(2).hasItem()) {
			UIHelper.renderTintedItem(stack, this.menu.getCompletableSlot(2).getBackground(), x + 24, y + 51, 35, 35,
					35, 0.7f);
		}
		if (!this.menu.getSlot(3).hasItem()) {
			UIHelper.renderTintedItem(stack, this.menu.getCompletableSlot(3).getBackground(), x + 49, y + 51, 35, 35,
					35, 0.7f);
		}

		// Text
		UIHelper.drawStringWithBorder(stack, "Stage 1 / 5", x + 90, y + 4, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://SHIP_CONSOLE/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
