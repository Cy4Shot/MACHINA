package com.machina.client.screen;

import com.machina.block.container.StateConverterContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.Color;
import com.machina.util.StringUtils;
import com.machina.util.helper.HeatHelper;
import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class StateConverterScreen extends NoJeiContainerScreen<StateConverterContainer> {
	public StateConverterScreen(StateConverterContainer pMenu, PlayerInventory pPlayerInventory,
			ITextComponent pTitle) {
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

	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {
		UIHelper.bindScifi();

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);

		// Progress
		int percentage = (int) (this.menu.te.heatFull() * 129f);
		UIHelper.blit(stack, x + 50, y + 20, 3, 130, 135, 18);
		UIHelper.blit(stack, x + 52, y + 22, 3, 115, percentage, 12);

		// Fluids
		UIHelper.blit(stack, x + 50, y + 60, 3, 230, 67, 18);
		UIHelper.blit(stack, x + 118, y + 60, 3, 230, 67, 18);
		int p1 = (int) (this.menu.te.propFull(0) * 64f);
		int p2 = (int) (this.menu.te.propFull(1) * 64f);
		FluidStack f1 = this.menu.te.getFluid(0);
		FluidStack f2 = this.menu.te.getFluid(1);
		UIHelper.renderFluid(stack, f1, x + 51, y + 61, p1, 15, 64, 15, getBlitOffset(), pX, pY, true);
		UIHelper.renderFluid(stack, f2, x + 119, y + 61, p2, 15, 64, 15, getBlitOffset(), pX, pY, true);

		UIHelper.drawCenteredStringWithBorder(stack,
				StringUtils.translateScreen("state_converter.stored")
						+ MathUtil.engineering(this.menu.te.normalized(), "K"),
				x + 117, y + 8, 0xFF_00fefe, 0xFF_0e0e0e);

		int c1 = f1.isEmpty() ? 0xFF_ff0000
				: new Color(f1.getFluid().getAttributes().getColor()).maxBrightness().toInt();
		int c2 = f2.isEmpty() ? 0xFF_ff0000
				: new Color(f2.getFluid().getAttributes().getColor()).maxBrightness().toInt();
		String s1 = f1.isEmpty() ? StringUtils.translateScreen("state_converter.none")
				: f1.getDisplayName().getString();
		String s2 = f2.isEmpty() ? StringUtils.translateScreen("state_converter.none")
				: f2.getDisplayName().getString();
		int l1 = UIHelper.getWidth(s1);
		int l2 = UIHelper.getWidth(s2);
		UIHelper.drawCenteredStringWithBorder(stack, "->", x + 117 + (l1 - l2) / 2, y + 45, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, s1, x + 50, y + 44, c1, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, s2, x + 184 - l2, y + 44, c2, 0xFF_0e0e0e);

		UIHelper.bindPrgrs();
		RegistryKey<World> dim = this.menu.te.getLevel().dimension();

		int req = (int) (HeatHelper.propFull(this.menu.te.reqHeat, dim) * 129f);
		if (this.menu.te.above)
			UIHelper.blit(stack, x + 51 + req, y + 21, 97, 242, 2, 14);
		else
			UIHelper.blit(stack, x + 51 + req, y + 21, 99, 242, 2, 14);

		UIHelper.drawStringWithBorder(stack, "MACHINA://STATE_CONV/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
