package com.machina.client.screen;

import java.util.Collections;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.machina.block.container.MixerContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SClearTank;
import com.machina.util.StringUtils;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.fluids.FluidStack;

public class MixerScreen extends NoJeiContainerScreen<MixerContainer> {
	public MixerScreen(MixerContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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
		int xSize = 236, ySize = 210;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;

		// Back
		UIHelper.bindLarge();
		UIHelper.blit(stack, x, y, 0, 0, xSize, ySize);

		// Fluid Slots
		UIHelper.blit(stack, x + 14, y + 42, 237, 0, 18, 94);
		UIHelper.blit(stack, x + 34, y + 42, 237, 0, 18, 94);
		UIHelper.blit(stack, x + 184, y + 42, 237, 0, 18, 94);
		UIHelper.blit(stack, x + 204, y + 42, 237, 0, 18, 94);
		FluidStack s1 = this.menu.te.getTank(0).getFluid();
		FluidStack s2 = this.menu.te.getTank(1).getFluid();
		FluidStack s3 = this.menu.te.getTank(2).getFluid();
		FluidStack s4 = this.menu.te.getTank(3).getFluid();
		int p1 = (int) ((float) s1.getAmount() / (float) this.menu.te.getTank(0).getCapacity() * 91f);
		int p2 = (int) ((float) s2.getAmount() / (float) this.menu.te.getTank(1).getCapacity() * 91f);
		int p3 = (int) ((float) s3.getAmount() / (float) this.menu.te.getTank(2).getCapacity() * 91f);
		int p4 = (int) ((float) s4.getAmount() / (float) this.menu.te.getTank(3).getCapacity() * 91f);

		// Info Window
		UIHelper.bindTrmnl();
		UIHelper.blit(stack, x + 59, y + 42, 0, 202, 117, 32);

		if (this.menu.te.rec.getPath().equals("empty")) {
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("mixer.notfound"), x + 118, y + 48,
					0xFF_ff0000, 0xFF_0e0e0e);
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("mixer.awaiting"), x + 118, y + 59,
					0xFF_ff0000, 0xFF_0e0e0e);
		} else {
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("mixer.found"), x + 118, y + 48,
					0xFF_00fefe, 0xFF_0e0e0e);
			if (this.menu.te.outputsFull()) {
				UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("mixer.output"), x + 118,
						y + 59, 0xFF_ff0000, 0xFF_0e0e0e);
			} else {
				if (this.menu.te.hasPower()) {
					UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("mixer.running"), x + 118,
							y + 59, 0xFF_00fefe, 0xFF_0e0e0e);
				} else {
					UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("mixer.power"), x + 118,
							y + 59, 0xFF_ff0000, 0xFF_0e0e0e);
				}
			}
		}

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF"),
				x + 118, y + 80, 0xFF_00fefe, 0xFF_0e0e0e);

		// Buttons
		List<ITextComponent> clear = Collections.singletonList(StringUtils.translateCompScreen("mixer.clear"));
		UIHelper.bindTrmnl();
		if (pX > x + 14 && pX < x + 14 + 17 && pY > y + 18 && pY < y + 18 + 17) {
			UIHelper.blit(stack, x + 14, y + 18, 237, 17, 17, 17);
			UIHelper.renderLabel(stack, clear, pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			UIHelper.blit(stack, x + 14, y + 18, 237, 0, 17, 17);
		}
		if (pX > x + 34 && pX < x + 34 + 17 && pY > y + 18 && pY < y + 18 + 17) {
			UIHelper.blit(stack, x + 34, y + 18, 237, 17, 17, 17);
			UIHelper.renderLabel(stack, clear, pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			UIHelper.blit(stack, x + 34, y + 18, 237, 0, 17, 17);
		}
		if (pX > x + 184 && pX < x + 184 + 17 && pY > y + 18 && pY < y + 18 + 17) {
			UIHelper.blit(stack, x + 184, y + 18, 237, 17, 17, 17);
			UIHelper.renderLabel(stack, clear, pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			UIHelper.blit(stack, x + 184, y + 18, 237, 0, 17, 17);
		}
		if (pX > x + 204 && pX < x + 204 + 17 && pY > y + 18 && pY < y + 18 + 17) {
			UIHelper.blit(stack, x + 204, y + 18, 237, 17, 17, 17);
			UIHelper.renderLabel(stack, clear, pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			UIHelper.blit(stack, x + 204, y + 18, 237, 0, 17, 17);
		}

		// Slots
		UIHelper.bindScifi();
		UIHelper.blit(stack, x + 32, y + 212, 3, 200, 174, 30);
		UIHelper.blit(stack, x + 109, y + 116, 228, 184, 19, 19);
		UIHelper.blit(stack, x + 23, y + 160, 228, 184, 19, 19);
		UIHelper.blit(stack, x + 193, y + 160, 228, 184, 19, 19);

		// Energy
		UIHelper.bindPrgrs();
		UIHelper.blit(stack, x + 55, y + 92, 131, 157, 125, 18);
		UIHelper.blit(stack, x + 57, y + 94, 131, 145, (int) (this.menu.te.getEnergyProp() * 119f), 12);

		// Title Deco
		UIHelper.blit(stack, x + 115, y + 22, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 59, y + 23, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 121, y + 23, 88, 0, 56, 4);

		// Slot Deco
		if (!s1.isEmpty()) {
			UIHelper.blit(stack, x + 14, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 14, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 14, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 14, y + 136, 0, 171, 17, 4);
		}
		if (!s2.isEmpty()) {
			UIHelper.blit(stack, x + 34, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 34, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 34, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 34, y + 136, 0, 171, 17, 4);
		}
		if (!s3.isEmpty()) {
			UIHelper.blit(stack, x + 184, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 184, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 184, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 184, y + 136, 0, 171, 17, 4);
		}
		if (!s4.isEmpty()) {
			UIHelper.blit(stack, x + 204, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 204, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 204, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 204, y + 136, 0, 171, 17, 4);
		}
		if (!this.menu.te.getItem(0).isEmpty()) {
			UIHelper.blit(stack, x + 18, y + 161, 0, 239, 29, 17);
		} else {
			UIHelper.blit(stack, x + 18, y + 161, 0, 183, 29, 17);
		}
		if (!this.menu.te.getItem(1).isEmpty()) {
			UIHelper.blit(stack, x + 104, y + 117, 0, 239, 29, 17);
		} else {
			UIHelper.blit(stack, x + 104, y + 117, 0, 183, 29, 17);
		}
		if (!this.menu.te.getItem(1).isEmpty()) {
			UIHelper.blit(stack, x + 188, y + 161, 0, 239, 29, 17);
		} else {
			UIHelper.blit(stack, x + 188, y + 161, 0, 183, 29, 17);
		}

		// Arrows
		UIHelper.blit(stack, x + 68, y + 120, 121, 246, 6, 10);
		UIHelper.blit(stack, x + 88, y + 120, 121, 246, 6, 10);
		UIHelper.blit(stack, x + 142, y + 120, 121, 246, 6, 10);
		UIHelper.blit(stack, x + 162, y + 120, 121, 246, 6, 10);
		UIHelper.blit(stack, x + 27, y + 147, 118, 240, 10, 6);
		UIHelper.blit(stack, x + 197, y + 147, 108, 240, 10, 6);

		// Fluids
		UIHelper.renderFluid(stack, s1, x + 15, y + 43, 15, p1, 15, 91, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, s2, x + 35, y + 43, 15, p2, 15, 91, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, s3, x + 185, y + 43, 15, p3, 15, 91, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, s4, x + 205, y + 43, 15, p4, 15, 91, getBlitOffset(), pX, pY, true);

		// Extra Text
		float scale = 0.7f;
		float iscale = 1f / scale;
		RenderSystem.scalef(scale, scale, scale);
		List<IReorderingProcessor> desc = LanguageMap.getInstance()
				.getVisualOrder(StringUtils.findOptimalLines(StringUtils.translateScreenComp("mixer.extra"), 170));

		for (int k1 = 0; k1 < desc.size(); ++k1) {
			UIHelper.drawCenteredStringWithBorder(stack, desc.get(k1), (x + 118) * iscale, (y + 162 + k1 * 9) * iscale,
					0xFF_00fefe, 0xFF_0e0e0e);
		}
		RenderSystem.scalef(iscale, iscale, iscale);

		// Text
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("mixer.title"), x + xSize / 2, y + 10,
				0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://MIXER_CHAMBER/", x + 6, y + 195, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_1) {
			int xSize = 236, ySize = 210;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;

			if (pX > x + 14 && pX < x + 14 + 17 && pY > y + 18 && pY < y + 18 + 17) {
				MachinaNetwork.CHANNEL.sendToServer(new C2SClearTank(this.menu.te.getBlockPos(), 0));
				UIHelper.click();
			}
			if (pX > x + 34 && pX < x + 34 + 17 && pY > y + 18 && pY < y + 18 + 17) {
				MachinaNetwork.CHANNEL.sendToServer(new C2SClearTank(this.menu.te.getBlockPos(), 1));
				UIHelper.click();
			}
			if (pX > x + 184 && pX < x + 184 + 17 && pY > y + 18 && pY < y + 18 + 17) {
				MachinaNetwork.CHANNEL.sendToServer(new C2SClearTank(this.menu.te.getBlockPos(), 2));
				UIHelper.click();
			}
			if (pX > x + 204 && pX < x + 204 + 17 && pY > y + 18 && pY < y + 18 + 17) {
				MachinaNetwork.CHANNEL.sendToServer(new C2SClearTank(this.menu.te.getBlockPos(), 3));
				UIHelper.click();
			}
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
