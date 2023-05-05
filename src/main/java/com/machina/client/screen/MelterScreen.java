package com.machina.client.screen;

import com.machina.block.container.MelterContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.recipe.impl.MelterRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.util.Color;
import com.machina.util.MachinaRL;
import com.machina.util.StringUtils;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class MelterScreen extends NoJeiContainerScreen<MelterContainer> {
	public MelterScreen(MelterContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - getXSize()) / 2;
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

	@SuppressWarnings("resource")
	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {
		UIHelper.bindScifi();

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		UIHelper.blit(stack, x, y, 2, 3, xSize, ySize);
		UIHelper.blit(stack, x + 32, y + 102, 3, 200, 174, 30);

		// Slots
		UIHelper.blit(stack, x + 181, y + 35, 228, 184, 19, 19);
		UIHelper.blit(stack, x + 158, y + 60, 3, 230, 67, 18);

		// Info Window
		UIHelper.bindTrmnl();
		UIHelper.blit(stack, x + 15, y + 38, 0, 202, 117, 32);
		if (this.menu.te.rec.equals(new MachinaRL("empty"))) {
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("melter.notfound"), x + 73, y + 44,
					0xFF_ff0000, 0xFF_0e0e0e);
		} else {
			MelterRecipe recipe = (MelterRecipe) RecipeInit
					.getRecipes(RecipeInit.MELTER_RECIPE, Minecraft.getInstance().level.getRecipeManager())
					.get(this.menu.te.rec);
			String time = this.menu.te.hasPower() ? " ("
					+ MathUtil.engineering((double) (this.menu.te.getCookTime() - this.menu.te.progress) / 20D, "s")
					+ ")" : "";
			String prod = StringUtils.toEllipsis(StringUtils.translate(recipe.output.getTranslationKey()),
					20 - time.length(), 0);
			UIHelper.drawCenteredStringWithBorder(stack, prod + time, x + 73, y + 44,
					new Color(recipe.output.getFluid().getAttributes().getColor()).maxBrightness().toInt(),
					0xFF_0e0e0e);
		}
		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF"),
				x + 72, y + 56, this.menu.te.getEnergy() == 0 ? 0xFF_ff0000 : 0xFF_00fefe, 0xFF_0e0e0e);

		// Slot Deco
		UIHelper.bindPrgrs();
		if (this.menu.te.getItem(0).isEmpty()) {
			UIHelper.blit(stack, x + 176, y + 36, 0, 183, 29, 17);
		} else {
			if (this.menu.te.rec.equals(new MachinaRL("empty"))) {
				UIHelper.blit(stack, x + 176, y + 36, 0, 222, 29, 17);
			} else {
				UIHelper.blit(stack, x + 176, y + 36, 0, 239, 29, 17);
			}
		}
		if (this.menu.te.getItem(0).isEmpty() || this.menu.te.getFluid(0).isEmpty()) {
			UIHelper.blit(stack, x + 153, y + 60, 0, 183, 4, 17);
			UIHelper.blit(stack, x + 225, y + 60, 25, 183, 4, 17);
		} else {
			if (!this.menu.te.hasSpace()) {
				UIHelper.blit(stack, x + 153, y + 60, 0, 222, 4, 17);
				UIHelper.blit(stack, x + 225, y + 60, 25, 222, 4, 17);
			} else {
				UIHelper.blit(stack, x + 153, y + 60, 0, 239, 4, 17);
				UIHelper.blit(stack, x + 225, y + 60, 25, 239, 4, 17);
			}
		}

		// Progress
		int prog = (int) ((float) this.menu.te.progress / (float) this.menu.te.getCookTime() * 7f);
		UIHelper.blit(stack, x + 158, y + 42, prog * 32, 128, 16, 16);
		UIHelper.blit(stack, x + 206, y + 42, 16 + prog * 32, 128, 16, 16);

		// Title Deco
		UIHelper.blit(stack, x + 115, y + 22, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 59, y + 23, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 121, y + 23, 88, 0, 56, 4);

		// Fluid
		UIHelper.renderFluid(stack, this.menu.te.getFluid(0), x + 159, y + 61, (int) (this.menu.te.propFull(0) * 64f),
				15, 64, 15, getBlitOffset(), pX, pY, true);

		// Text
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("melter.title"), x + xSize / 2, y + 10,
				0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://CHEM_MELTER/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
