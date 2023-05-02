package com.machina.client.screen;

import com.machina.block.container.AtmosphericSeparatorContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SAtmosphericSeparatorSelect;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class AtmosphericSeparatorScreen extends NoJeiContainerScreen<AtmosphericSeparatorContainer> {

	public AtmosphericSeparatorScreen(AtmosphericSeparatorContainer menu, PlayerInventory inv, ITextComponent title) {
		super(menu, inv, title);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public void render(MatrixStack stack, int pX, int pY, float par) {
		UIHelper.resetColor();

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);

		// Render
		super.render(stack, pX, pY, par);
		this.renderTooltip(stack, pX, pY);
	}

	@Override
	protected void renderBg(MatrixStack stack, float par, int pX, int pY) {

		int size = FluidInit.ATMOSPHERE.size();

		UIHelper.bindLarge();
		int xSize = 237, ySize = 4 + size * 20 + 19;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 0, 0, xSize, 4);

		for (int i = 0; i < size; i++) {
			boolean s = i == this.menu.te.selected;
			UIHelper.bindLarge();
			int newX = x + 4;
			int newY = y + 4 + i * 20;

			this.blit(stack, x, newY, 0, 4, xSize, 20);

			FluidObject obj = FluidInit.ATMOSPHERE.get(i);

			if (pX > newX + 20 && pX < newX + 20 + 94 && pY > newY && pY < newY + 18) {
				this.blit(stack, newX + 20, newY, 0, 229, 94, 18);
			} else {
				if (s) {
					this.blit(stack, newX + 20, newY, 0, 211, 94, 18);
				} else {
					this.blit(stack, newX + 20, newY, 94, 211, 94, 18);
				}
			}

			this.blit(stack, newX, newY, 94, 229, 18, 18);
			FluidStack f = new FluidStack(obj.fluid(), 1);
			UIHelper.renderFluid(stack, f, newX + 1, newY + 1, 15, 15, 15, 15, getBlitOffset(), pX, pY, false);

			if (s) {

				if ((int) (this.menu.te.rate * 1000) > 0) {
					UIHelper.drawStringWithBorder(stack,
							StringUtils.translateScreen("atmospheric_seperator.producing")
									+ MathUtil.engineering(this.menu.te.rate, "B/t"),
							newX + 116, newY + 4, 0xFF_00fefe, 0xFF_0e0e0e);
				} else {
					UIHelper.drawStringWithBorder(stack,
							StringUtils.translateScreen("atmospheric_seperator.not_enough"), newX + 116, newY + 4,
							0xFF_ff0000, 0xFF_0e0e0e);
				}
			} else {
				UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("atmospheric_seperator.no"),
						newX + 116, newY + 4, 0xFF_ff0000, 0xFF_0e0e0e);

			}

			UIHelper.drawStringWithBorder(stack, obj.chem().getDisplayName(), newX + 24, newY + 4, 0xFF_00fefe,
					0xFF_0e0e0e);
		}

		UIHelper.bindLarge();
		this.blit(stack, x, y + 4 + size * 20, 0, 192, xSize, 19);

		UIHelper.drawStringWithBorder(stack, "MACHINA://ATM_SEPERATOR/", x + 5, y + 8 + size * 20, 0xFF_00fefe,
				0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int button) {
		if (button == 0) {
			int xSize = 237, ySize = 4 + FluidInit.ATMOSPHERE.size() * 20 + 19;
			for (int i = 0; i < FluidInit.ATMOSPHERE.size(); i++) {
				int newX = (this.width - xSize) / 2 + 4;
				int newY = (this.height - ySize) / 2 + 4 + i * 20;
				if (pX > newX + 20 && pX < newX + 20 + 94 && pY > newY && pY < newY + 18) {
					MachinaNetwork.CHANNEL.sendToServer(new C2SAtmosphericSeparatorSelect(menu.te.getBlockPos(), i));
					UIHelper.click();
					return true;
				}
			}
		}
		return super.mouseReleased(pX, pY, button);
	}

	@Override
	protected void renderLabels(MatrixStack stack, int pX, int pY) {
	}

}
