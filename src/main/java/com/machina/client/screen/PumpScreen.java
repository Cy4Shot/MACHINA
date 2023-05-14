package com.machina.client.screen;

import java.util.Collections;
import java.util.List;

import com.machina.block.container.PumpContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.StringUtils;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.fluids.FluidStack;

public class PumpScreen extends NoJeiContainerScreen<PumpContainer> {

	public PumpScreen(PumpContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;

		// Back
		UIHelper.bindScifi();
		UIHelper.blit(stack, x, y, 2, 3, xSize, ySize);
		UIHelper.blit(stack, x + 44, y + 60, 3, 230, 67, 18);
		UIHelper.blit(stack, x + 124, y + 60, 3, 230, 67, 18);
		UIHelper.blit(stack, x + 214, y + 4, 239, 2, 17, 74);

		// Title Deco
		UIHelper.bindPrgrs();
		UIHelper.blit(stack, x + 115, y + 22, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 59, y + 23, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 121, y + 23, 88, 0, 56, 4);

		// Energy Progress
		int p = (int) (this.menu.te.getEnergyProp() * 69f);
		UIHelper.blit(stack, x + 216, y + 75 - p, 119, 144 + 69 - p, 12, p);

		// Slot Deco
		if (this.menu.te.getFluid(0).isEmpty()) {
			UIHelper.blit(stack, x + 39, y + 60, 0, 222, 4, 17);
			UIHelper.blit(stack, x + 111, y + 60, 25, 222, 4, 17);
		} else {
			if (!this.menu.te.hasWaterSpace()) {
				UIHelper.blit(stack, x + 39, y + 60, 0, 183, 4, 17);
				UIHelper.blit(stack, x + 111, y + 60, 25, 183, 4, 17);
			} else {
				UIHelper.blit(stack, x + 39, y + 60, 0, 239, 4, 17);
				UIHelper.blit(stack, x + 111, y + 60, 25, 239, 4, 17);
			}
		}
		if (this.menu.te.getFluid(1).isEmpty()) {
			UIHelper.blit(stack, x + 119, y + 60, 0, 222, 4, 17);
			UIHelper.blit(stack, x + 191, y + 60, 25, 222, 4, 17);
		} else {
			if (!this.menu.te.hasBrineSpace()) {
				UIHelper.blit(stack, x + 119, y + 60, 0, 183, 4, 17);
				UIHelper.blit(stack, x + 191, y + 60, 25, 183, 4, 17);
			} else {
				UIHelper.blit(stack, x + 119, y + 60, 0, 239, 4, 17);
				UIHelper.blit(stack, x + 191, y + 60, 25, 239, 4, 17);
			}
		}

		// Extra Text
		float scale = 0.7f;
		float iscale = 1f / scale;
		RenderSystem.scalef(scale, scale, scale);

		ITextComponent comp;
		int col = 0xFF_ff0000;
		if (!this.menu.te.hasPower()) {
			comp = StringUtils.translateScreenComp("pump.power");
		} else if (!this.menu.te.hasWaterSpace() || !this.menu.te.hasBrineSpace()) {
			comp = StringUtils.translateScreenComp("pump.output");
		} else {
			comp = StringUtils.translateScreenComp("pump.extra");
			col = 0xFF_00fefe;
		}

		List<IReorderingProcessor> desc = LanguageMap.getInstance()
				.getVisualOrder(StringUtils.findOptimalLines(comp, 210));

		for (int k1 = 0; k1 < desc.size(); ++k1) {
			UIHelper.drawCenteredStringWithBorder(stack, desc.get(k1), (x + 118) * iscale, (y + 36 + k1 * 9) * iscale,
					col, 0xFF_0e0e0e);
		}
		RenderSystem.scalef(iscale, iscale, iscale);

		// Fluids
		int p1 = (int) (this.menu.te.propFull(0) * 64f);
		int p2 = (int) (this.menu.te.propFull(1) * 64f);
		FluidStack f1 = this.menu.te.getFluid(0);
		FluidStack f2 = this.menu.te.getFluid(1);
		UIHelper.renderFluid(stack, f1, x + 45, y + 61, p1, 15, 64, 15, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, f2, x + 125, y + 61, p2, 15, 64, 15, getBlitOffset(), pX, pY, true);

		// Energy Label
		if (pX > x + 213 && pX < x + 213 + 17 && pY > y + 3 && pY < y + 3 + 74) {
			UIHelper.renderUnboundLabel(stack,
					Collections.singletonList(StringUtils.toComp(MathUtil.engineering(this.menu.te.getEnergy(), "RF")
							+ " / " + MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF"))),
					pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
		}

		// Text
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("pump.title"), x + xSize / 2, y + 10,
				0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://INDUSTRY_PUMP/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
