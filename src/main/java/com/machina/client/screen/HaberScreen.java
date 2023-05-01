package com.machina.client.screen;

import java.util.Arrays;

import com.machina.block.container.HaberContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class HaberScreen extends NoJeiContainerScreen<HaberContainer> {

	public HaberScreen(HaberContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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
		UIHelper.bindLarge();

		int xSize = 236, ySize = 210;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 0, 0, xSize, ySize);

		this.blit(stack, x + 4, y + 22, 237, 0, 18, 94);
		this.blit(stack, x + 24, y + 22, 237, 0, 18, 94);
		this.blit(stack, x + 44, y + 22, 237, 0, 18, 94);

		FluidStack s1 = this.menu.te.getTank(0).getFluid();
		FluidStack s2 = this.menu.te.getTank(1).getFluid();

		int p1 = (int) ((float) s1.getAmount() / (float) this.menu.te.getTank(0).getCapacity() * 91f);
		int p2 = (int) ((float) s2.getAmount() / (float) this.menu.te.getTank(1).getCapacity() * 91f);

		UIHelper.renderFluid(stack, s1, x + 5, y + 23, 15, p1, 15, 91, getBlitOffset(), pX, pY);
		UIHelper.renderFluid(stack, s2, x + 25, y + 23, 15, p2, 15, 91, getBlitOffset(), pX, pY);

		UIHelper.bindTrmnl();

		if (pX > x + 4 && pX < x + 4 + 17 && pY > y + 4 && pY < y + 4 + 17) {
			this.blit(stack, x + 4, y + 4, 237, 17, 17, 17);
			UIHelper.renderLabel(stack, Arrays.asList(StringUtils.translateCompScreen("pressurized_chamber.clear")), pX,
					pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			this.blit(stack, x + 4, y + 4, 237, 0, 17, 17);
		}

		if (pX > x + 24 && pX < x + 24 + 17 && pY > y + 4 && pY < y + 4 + 17) {
			this.blit(stack, x + 24, y + 4, 237, 17, 17, 17);
			UIHelper.renderLabel(stack, Arrays.asList(StringUtils.translateCompScreen("pressurized_chamber.clear")), pX,
					pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			this.blit(stack, x + 24, y + 4, 237, 0, 17, 17);
		}

		this.blit(stack, x + 80, y + 84, 0, 202, 117, 32);

		UIHelper.bindScifi();

		this.blit(stack, x + 23, y + 170, 228, 184, 19, 19);

		this.blit(stack, x + 32, y + 212, 3, 200, 174, 30);

		this.blit(stack, x + 80, y + 20, 3, 130, 135, 18);
		int energy = (int) (this.menu.te.getEnergyProp() * 129f);
		this.blit(stack, x + 82, y + 22, 3, 103, energy, 12);

		this.blit(stack, x + 80, y + 151, 3, 130, 135, 18);
		this.blit(stack, x + 137, y + 170, 228, 184, 19, 19);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF") + " - "
						+ String.format("%.01f", this.menu.te.getEnergyProp() * 100f) + "%",
				x + 145, y + 8, 0xFF_00fefe, 0xFF_0e0e0e);

		FluidStack s3 = this.menu.te.getTank(2).getFluid();
		int p3 = (int) ((float) s3.getAmount() / (float) this.menu.te.getTank(2).getCapacity() * 132f);
		UIHelper.renderFluid(stack, s3, x + 81, y + 152, p3, 15, 132, 15, getBlitOffset(), pX, pY);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering((double) s3.getAmount() / (double) 1000, "B") + " / "
						+ MathUtil.engineering((double) this.menu.te.getTank(2).getCapacity() / (double) 1000, "B")
						+ " - " + String.format("%.01f", this.menu.te.getTank(2).propFull() * 100f) + "%",
				x + 146, y + 128, 0xFF_00fefe, 0xFF_0e0e0e);

		UIHelper.drawStringWithBorder(stack, "MACHINA://HABER_PROCESS/", x + 6, y + 195, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int xSize = 236, ySize = 210;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;

//			if (pX > x + 4 && pX < x + 4 + 17 && pY > y + 4 && pY < y + 4 + 17) {
//				UIHelper.click();
//				MachinaNetwork.CHANNEL.sendToServer(new C2SPressurizedChamberClear(this.menu.te.getBlockPos(), 0));
//			}
//
//			if (pX > x + 24 && pX < x + 24 + 17 && pY > y + 4 && pY < y + 4 + 17) {
//				UIHelper.click();
//				MachinaNetwork.CHANNEL.sendToServer(new C2SPressurizedChamberClear(this.menu.te.getBlockPos(), 1));
//			}
		}
		return super.mouseReleased(pX, pY, pButton);
	}

	@Override
	public int getXSize() {
		return 236;
	}

	@Override
	public int getYSize() {
		return 240;
	}
}
