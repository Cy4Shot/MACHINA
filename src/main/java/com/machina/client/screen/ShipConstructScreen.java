package com.machina.client.screen;

import com.machina.block.container.ShipConstructContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.item.ShipComponentItem;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SShipConsoleGUIButton;
import com.machina.util.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class ShipConstructScreen extends NoJeiContainerScreen<ShipConstructContainer> {

	public ShipConstructScreen(ShipConstructContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public int getSlotColor(int index) {
		if (index < this.menu.te.getContainerSize())
			return 0x00_FFFFFF;
		else
			return super.getSlotColor(index);
	}

	@Override
	public void tick() {
		super.tick();

		if (menu.te.completed) {
			UIHelper.close();
			this.onClose();
		}
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
		int percentage = (int) (((float) this.menu.te.progress / 100f) * 129f);
		UIHelper.blit(stack, x + 74, y + 28, 3, 130, 135, 18);
		UIHelper.blit(stack, x + 76, y + 30, 3, 103, percentage, 12);

		// Slots
		UIHelper.blit(stack, x + 20, y + 22, 3, 150, 23, 23);
		UIHelper.blit(stack, x + 47, y + 22, 29, 150, 23, 23);
		UIHelper.blit(stack, x + 20, y + 49, 3, 175, 23, 23);
		UIHelper.blit(stack, x + 47, y + 49, 29, 175, 23, 23);
		renderHintItem(stack, 0, x + 24, y + 26);
		renderHintItem(stack, 1, x + 49, y + 26);
		renderHintItem(stack, 2, x + 24, y + 51);
		renderHintItem(stack, 3, x + 49, y + 51);

		// Button
		UIHelper.bindScifi();
		if (pX > x + 74 && pX < x + 74 + 95 && pY > y + 49 && pY < y + 49 + 18 && this.menu.te.progress == 0
				&& this.menu.areSlotsComplete()) {
			UIHelper.blit(stack, x + 74, y + 49, 56, 169, 95, 18);
		} else {
			UIHelper.blit(stack, x + 74, y + 49, 56, 150, 95, 18);
		}
		String buttonText = StringUtils.translateScreen("ship_console.missing");
		if (this.menu.areSlotsComplete()) {
			if (this.menu.te.erroredPos.size() > 0)
				buttonText = StringUtils.translateScreen("ship_console.obstructed");
			else
				buttonText = StringUtils.translateScreen("ship_console.craft");
		} else if (this.menu.te.progress != 0) {
			buttonText = StringUtils.translateScreen("ship_console.crafting");
		}
		UIHelper.drawStringWithBorder(stack, buttonText, x + 78, y + 53, 0xFF_00fefe, 0xFF_0e0e0e);

		// Text
		String stage = String.format(StringUtils.translateScreen("ship_console.stage") + " %d / 5", this.menu.te.stage);
		String comp = ShipComponentItem.getNameForStage(this.menu.te.stage);
		UIHelper.drawStringWithBorder(stack, stage, x + 90, y + 4, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, comp, x + 120, y + 14, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://SHIP_CONSOLE/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int xSize = 236, ySize = 99;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			if (pX > x + 74 && pX < x + 74 + 95 && pY > y + 49 && pY < y + 49 + 18) {
				if (this.menu.areSlotsComplete()) {
					MachinaNetwork.CHANNEL.sendToServer(new C2SShipConsoleGUIButton(this.menu.te.getBlockPos()));
					UIHelper.click();
					return true;
				}
			}
		}
		return super.mouseReleased(pX, pY, pButton);
	}

	private void renderHintItem(MatrixStack stack, int slot, int x, int y) {
		if (!this.menu.getSlot(slot).hasItem() && this.menu.te.progress == 0) {
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
