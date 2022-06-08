package com.machina.client.screen;

import com.machina.block.container.BatteryContainer;
import com.machina.client.util.UIHelper;
import com.machina.network.MachinaNetwork;
import com.machina.network.message.C2SUpdateEnergySide;
import com.machina.util.MachinaRL;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class BatteryScreen extends NoJeiContainerScreen<BatteryContainer> {
	private static final MachinaRL SCIFI_EL = new MachinaRL("textures/gui/scifi_el.png");

	private boolean energyWindow = false;

	public BatteryScreen(BatteryContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.textureManager.bind(SCIFI_EL);

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);
		this.blit(stack, x + 212, y + 4, 228, 184, 19, 19);
		if (pX > x + 212 && pX < x + 212 + 19 && pY > y + 4 && pY < y + 4 + 19) {
			this.blit(stack, x + 216, y + 6, 180 + 11, 212, 11, 15);
		} else {
			this.blit(stack, x + 216, y + 6, 180, 212, 11, 15);
		}

		// Energy Window
		if (energyWindow) {
			this.blit(stack, x + xSize + 4, y, 152, 103, 87, 81);
			int x2 = x + xSize + 15;
			int y2 = y + 13;

			this.blit(stack, x2 + 4, y2, 228, 184, 19, 19);
			this.blit(stack, x2 + 25, y2, 228, 184, 19, 19);
			this.blit(stack, x2 + 4, y2 + 21, 228, 184, 19, 19);
			this.blit(stack, x2 + 25, y2 + 21, 228, 184, 19, 19);
			this.blit(stack, x2 + 46, y2 + 21, 228, 184, 19, 19);
			this.blit(stack, x2 + 25, y2 + 42, 228, 184, 19, 19);

			this.blit(stack, x2 + 9, y2 + 5, 236, 208 + this.menu.te.sides[3] * 12, 9, 9); // South
			this.blit(stack, x2 + 28, y2 + 6, 203, 208 + this.menu.te.sides[1] * 12, 12, 6); // Up
			this.blit(stack, x2 + 10, y2 + 24, 215, 208 + this.menu.te.sides[4] * 12, 6, 12); // West
			this.blit(stack, x2 + 30, y2 + 26, 227, 208 + this.menu.te.sides[2] * 12, 9, 9); // North
			this.blit(stack, x2 + 52, y2 + 24, 221, 208 + this.menu.te.sides[5] * 12, 6, 12); // Down
			this.blit(stack, x2 + 28, y2 + 48, 203, 214 + this.menu.te.sides[0] * 12, 12, 6); // East
		}

		// Progress
		int percentage = (int) (this.menu.te.propFull() * 129f);
		this.blit(stack, x + 50, y + 38, 3, 130, 135, 18);
		this.blit(stack, x + 52, y + 40, 3, 103, percentage, 12);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF") + " - "
						+ String.format("%.01f", this.menu.te.propFull() * 100f) + "%",
				x + 117, y + 22, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://BATTERY/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int xSize = 236, ySize = 99;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			if (pX > x + 212 && pX < x + 212 + 19 && pY > y + 4 && pY < y + 4 + 19) {
				this.energyWindow = !this.energyWindow;
				UIHelper.click();
			}

			if (energyWindow) {
				int x2 = x + xSize + 15;
				int y2 = y + 13;

				boolean changed = false;
				int[] data = this.menu.te.sides;

				if (pX > x2 + 4 && pX < x2 + 4 + 19 && pY > y2 && pY < y2 + 19) {
					data[3]++;
					if (data[3] > 3)
						data[3] = 0;
					changed = true;
				}

				if (pX > x2 + 25 && pX < x2 + 25 + 19 && pY > y2 && pY < y2 + 19) {
					data[1]++;
					if (data[1] > 3)
						data[1] = 0;
					changed = true;
				}

				if (pX > x2 + 4 && pX < x2 + 4 + 19 && pY > y2 + 21 && pY < y2 + 21 + 19) {
					data[4]++;
					if (data[4] > 3)
						data[4] = 0;
					changed = true;
				}

				if (pX > x2 + 25 && pX < x2 + 25 + 19 && pY > y2 + 21 && pY < y2 + 21 + 19) {
					data[2]++;
					if (data[2] > 3)
						data[2] = 0;
					changed = true;
				}

				if (pX > x2 + 46 && pX < x2 + 46 + 19 && pY > y2 + 21 && pY < y2 + 21 + 19) {
					data[5]++;
					if (data[5] > 3)
						data[5] = 0;
					changed = true;
				}

				if (pX > x2 + 25 && pX < x2 + 25 + 19 && pY > y2 + 42 && pY < y2 + 42 + 19) {
					data[0]++;
					if (data[0] > 3)
						data[0] = 0;
					changed = true;
				}

				if (changed) {
					MachinaNetwork.CHANNEL.sendToServer(new C2SUpdateEnergySide(this.menu.te.getBlockPos(), data));
					UIHelper.click();
				}
			}
		}
		return super.mouseReleased(pX, pY, pButton);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
