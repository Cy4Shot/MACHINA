package com.machina.client.screen;

import com.machina.block.container.ShipConsoleContainer;
import com.machina.client.util.UIHelper;
import com.machina.network.MachinaNetwork;
import com.machina.network.message.C2SShipConsoleGUIButton;
import com.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class ShipConsoleScreen extends NoJeiContainerScreen<ShipConsoleContainer> {
	private static final MachinaRL SCIFI_EL = new MachinaRL("textures/gui/scifi_el.png");

	public ShipConsoleScreen(ShipConsoleContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
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
		this.blit(stack, x + 74, y + 28, 3, 130, 135, 18);
		this.blit(stack, x + 76, y + 30, 3, 103, percentage, 12);

		// Slots
		this.blit(stack, x + 20, y + 22, 160, 104, 23, 23);
		this.blit(stack, x + 47, y + 22, 186, 104, 23, 23);
		this.blit(stack, x + 20, y + 49, 160, 129, 23, 23);
		this.blit(stack, x + 47, y + 49, 186, 129, 23, 23);
		renderHintItem(stack, 0, x + 24, y + 26);
		renderHintItem(stack, 1, x + 49, y + 26);
		renderHintItem(stack, 2, x + 24, y + 51);
		renderHintItem(stack, 3, x + 49, y + 51);

		// Button
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.textureManager.bind(SCIFI_EL);
		if (pX > x + 74 && pX < x + 74 + 95 && pY > y + 49 && pY < y + 49 + 18) {
			this.blit(stack, x + 74, y + 49, 92, 174, 95, 18);
		} else {
			this.blit(stack, x + 74, y + 49, 92, 155, 95, 18);
		}
		UIHelper.drawStringWithBorder(stack, "Tingly Clicker", x + 78, y + 53, 0xFF_00fefe, 0xFF_0e0e0e);

		// Text
		UIHelper.drawStringWithBorder(stack, "Stage 1 / 5", x + 90, y + 4, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://SHIP_CONSOLE/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int xSize = 236, ySize = 99;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			if (pX > x + 74 && pX < x + 74 + 95 && pY > y + 49 && pY < y + 49 + 18) {
				MachinaNetwork.CHANNEL.sendToServer(new C2SShipConsoleGUIButton(this.menu.te.getBlockPos(), 2));
			}
		}
		return false;
	}

	private void renderHintItem(MatrixStack stack, int slot, int x, int y) {
		if (!this.menu.getSlot(slot).hasItem()) {
			ItemStack hint = this.menu.getCompletableSlot(slot).getBackground();
			String count = String.valueOf(hint.getCount());
			UIHelper.renderTintedItem(stack, hint, x, y, 35, 35, 35, 0.7f);
			UIHelper.drawStringWithBorder(stack, count, x + 10, y + 10, 0x9D_00fefe, 0x9D_0e0e0e);
		}
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
