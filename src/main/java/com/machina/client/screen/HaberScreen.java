package com.machina.client.screen;

import java.util.List;

import com.machina.block.container.HaberContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.fluids.FluidStack;

public class HaberScreen extends NoJeiContainerScreen<HaberContainer> {

	public HaberScreen(HaberContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;

		if (!this.menu.te.formed) {
			Minecraft.getInstance().setScreen(new UnformedMultiblockScreen<>(this.menu, this.title));
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
		UIHelper.bindLarge();

		// Back
		int xSize = 236, ySize = 210;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		UIHelper.blit(stack, x, y, 0, 0, xSize, ySize);

		// Fluid Boxes
		UIHelper.blit(stack, x + 14, y + 42, 237, 0, 18, 94);
		UIHelper.blit(stack, x + 34, y + 42, 237, 0, 18, 94);
		UIHelper.blit(stack, x + 54, y + 42, 237, 0, 18, 94);
		UIHelper.blit(stack, x + 204, y + 42, 237, 0, 18, 94);

		// Info Window
		UIHelper.bindTrmnl();
		UIHelper.blit(stack, x + 79, y + 42, 138, 184, 118, 72);

		boolean p = this.menu.te.hasPower();
		boolean m = this.menu.te.hasMethane();
		boolean w = this.menu.te.hasWater();
		boolean n = this.menu.te.hasNitrogen();
		boolean c = this.menu.te.hasCatalyst();
		boolean o = this.menu.te.hasOutput();
		boolean a = p && m && w && n && c && o;

		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen(a ? "haber.active" : "haber.inactive"),
				x + 137, y + 46, a ? 0xFF_00fefe : 0xFF_ff0000, 0xFF_0e0e0e);

		String ok = StringUtils.translateScreen("haber.ok");
		String nk = StringUtils.translateScreen("haber.missing");

		float scale = 0.7f;
		float iscale = 1f / scale;
		RenderSystem.scalef(scale, scale, scale);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("haber.power") + (p ? ok : nk),
				(x + 137) * iscale, (y + 56) * iscale, p ? 0xFF_00fefe : 0xFF_ff0000, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("haber.methane") + (m ? ok : nk),
				(x + 137) * iscale, (y + 63) * iscale, m ? 0xFF_00fefe : 0xFF_ff0000, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("haber.water") + (w ? ok : nk),
				(x + 137) * iscale, (y + 70) * iscale, w ? 0xFF_00fefe : 0xFF_ff0000, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("haber.nitrogen") + (n ? ok : nk),
				(x + 137) * iscale, (y + 77) * iscale, n ? 0xFF_00fefe : 0xFF_ff0000, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("haber.catalyst") + (c ? ok : nk),
				(x + 137) * iscale, (y + 84) * iscale, c ? 0xFF_00fefe : 0xFF_ff0000, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("haber.output") + (o ? ok : nk),
				(x + 137) * iscale, (y + 91) * iscale, o ? 0xFF_00fefe : 0xFF_ff0000, 0xFF_0e0e0e);
		RenderSystem.scalef(iscale, iscale, iscale);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF"),
				x + 137, y + 100, 0xFF_00fefe, 0xFF_0e0e0e);

		// Energy
		UIHelper.bindPrgrs();
		UIHelper.blit(stack, x + 76, y + 118, 131, 157, 125, 18);
		UIHelper.blit(stack, x + 78, y + 120, 131, 145, (int) (this.menu.te.getEnergyProp() * 119f), 12);

		// Hotbar
		UIHelper.bindScifi();
		UIHelper.blit(stack, x + 32, y + 212, 3, 200, 174, 30);

		// Slot
		UIHelper.blit(stack, x + 86, y + 140, 228, 184, 19, 19);

		UIHelper.bindPrgrs();

		// Title Deco
		UIHelper.blit(stack, x + 115, y + 22, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 59, y + 23, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 121, y + 23, 88, 0, 56, 4);

		// Slot Decoration
		if (m) {
			UIHelper.blit(stack, x + 14, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 14, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 14, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 14, y + 136, 0, 171, 17, 4);
		}
		if (w) {
			UIHelper.blit(stack, x + 34, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 34, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 34, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 34, y + 136, 0, 171, 17, 4);
		}
		if (n) {
			UIHelper.blit(stack, x + 54, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 54, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 54, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 54, y + 136, 0, 171, 17, 4);
		}
		if (o) {
			UIHelper.blit(stack, x + 204, y + 37, 0, 175, 17, 4);
			UIHelper.blit(stack, x + 204, y + 136, 0, 179, 17, 4);
		} else {
			UIHelper.blit(stack, x + 204, y + 37, 0, 167, 17, 4);
			UIHelper.blit(stack, x + 204, y + 136, 0, 171, 17, 4);
		}
		if (c) {
			UIHelper.blit(stack, x + 81, y + 141, 0, 239, 29, 17);
		} else {
			UIHelper.blit(stack, x + 81, y + 141, 0, 183, 29, 17);
		}

		// Line Decoration
		if (m) {
			UIHelper.blit(stack, x + 20, y + 142, 0, 20, 19, 9);
		} else {
			UIHelper.blit(stack, x + 20, y + 142, 44, 20, 19, 9);
		}

		if (w && m) {
			UIHelper.blit(stack, x + 40, y + 142, 20, 30, 4, 4);
			UIHelper.blit(stack, x + 39, y + 146, 88, 10, 6, 6);
		} else {
			UIHelper.blit(stack, x + 40, y + 142, 64, 30, 4, 4);
			UIHelper.blit(stack, x + 39, y + 146, 88, 16, 6, 6);
		}

		if (n && w && m) {
			UIHelper.blit(stack, x + 45, y + 147, 25, 25, 14, 4);
			UIHelper.blit(stack, x + 60, y + 142, 20, 30, 4, 4);
			UIHelper.blit(stack, x + 59, y + 146, 88, 10, 6, 6);
			UIHelper.blit(stack, x + 65, y + 147, 25, 25, 14, 4);
		} else {
			UIHelper.blit(stack, x + 45, y + 147, 69, 25, 14, 4);
			UIHelper.blit(stack, x + 60, y + 142, 64, 30, 4, 4);
			UIHelper.blit(stack, x + 59, y + 146, 88, 16, 6, 6);
			UIHelper.blit(stack, x + 65, y + 147, 69, 25, 14, 4);
		}

		if (a) {
			UIHelper.blit(stack, x + 111, y + 147, 25, 25, 14, 4);
			UIHelper.blit(stack, x + 125, y + 147, 25, 25, 14, 4);
			UIHelper.blit(stack, x + 139, y + 147, 25, 25, 14, 4);
			UIHelper.blit(stack, x + 153, y + 147, 25, 25, 14, 4);
			UIHelper.blit(stack, x + 167, y + 147, 25, 25, 14, 4);
			UIHelper.blit(stack, x + 181, y + 147, 25, 25, 14, 4);
			UIHelper.blit(stack, x + 195, y + 142, 25, 20, 19, 9);
		} else {
			UIHelper.blit(stack, x + 111, y + 147, 69, 25, 14, 4);
			UIHelper.blit(stack, x + 125, y + 147, 69, 25, 14, 4);
			UIHelper.blit(stack, x + 139, y + 147, 69, 25, 14, 4);
			UIHelper.blit(stack, x + 153, y + 147, 69, 25, 14, 4);
			UIHelper.blit(stack, x + 167, y + 147, 69, 25, 14, 4);
			UIHelper.blit(stack, x + 181, y + 147, 69, 25, 14, 4);
			UIHelper.blit(stack, x + 195, y + 142, 69, 20, 19, 9);
		}

		// Fluids
		FluidStack s1 = this.menu.te.getTank(0).getFluid();
		FluidStack s2 = this.menu.te.getTank(1).getFluid();
		FluidStack s3 = this.menu.te.getTank(2).getFluid();
		FluidStack s4 = this.menu.te.getTank(3).getFluid();
		int p1 = (int) ((float) s1.getAmount() / (float) this.menu.te.getTank(0).getCapacity() * 91f);
		int p2 = (int) ((float) s2.getAmount() / (float) this.menu.te.getTank(1).getCapacity() * 91f);
		int p3 = (int) ((float) s3.getAmount() / (float) this.menu.te.getTank(2).getCapacity() * 91f);
		int p4 = (int) ((float) s4.getAmount() / (float) this.menu.te.getTank(3).getCapacity() * 91f);
		UIHelper.renderFluid(stack, s1, x + 15, y + 43, 15, p1, 15, 91, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, s2, x + 55, y + 43, 15, p2, 15, 91, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, s3, x + 35, y + 43, 15, p3, 15, 91, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, s4, x + 205, y + 43, 15, p4, 15, 91, getBlitOffset(), pX, pY, true);

		// Extra Text
		RenderSystem.scalef(scale, scale, scale);
		List<IReorderingProcessor> desc = LanguageMap.getInstance()
				.getVisualOrder(StringUtils.findOptimalLines(StringUtils.translateScreenComp("haber.extra"), 200));

		for (int k1 = 0; k1 < desc.size(); ++k1) {
			UIHelper.drawCenteredStringWithBorder(stack, desc.get(k1), (x + 118) * iscale, (y + 167 + k1 * 9) * iscale,
					0xFF_00fefe, 0xFF_0e0e0e);
		}
		RenderSystem.scalef(iscale, iscale, iscale);

		// Text
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("haber.title"), x + xSize / 2, y + 10,
				0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://HABER_PROCESS/", x + 6, y + 195, 0xFF_00fefe, 0xFF_0e0e0e);
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
