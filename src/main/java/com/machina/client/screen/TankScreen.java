package com.machina.client.screen;

import com.machina.block.container.TankContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class TankScreen extends NoJeiContainerScreen<TankContainer> {
	public TankScreen(TankContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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
		UIHelper.bindScifi();

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);
		this.blit(stack, x + 32, y + 102, 3, 200, 174, 30);

		// Slots
		this.blit(stack, x + 25, y + 37, 228, 184, 19, 19);
		this.blit(stack, x + 190, y + 37, 228, 184, 19, 19);
		this.blit(stack, x + 50, y + 38, 3, 130, 135, 18);

		// Decorators
		UIHelper.bindPrgrs();
		this.blit(stack, x + 20, y + 38, 0, 239, 29, 17);
		this.blit(stack, x + 185, y + 38, 0, 239, 29, 17);

		UIHelper.withAlpha(() -> {
			this.blit(stack, x + 29, y + 41, 101, 246, 10, 10);
			this.blit(stack, x + 194, y + 41, 111, 246, 10, 10);
		}, 0.5f);

		// Progress
		FluidStack fluid = this.menu.te.getFluid();
		int percentage = (int) (this.menu.te.propFull() * 132f);
		UIHelper.renderFluid(stack, fluid, x + 51, y + 39, percentage, 15, 132, 15, getBlitOffset(), pX, pY);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering((double) this.menu.te.stored() / 1000D, "B") + " / "
						+ MathUtil.engineering((double) this.menu.te.capacity() / 1000D, "B") + " - "
						+ String.format("%.01f", this.menu.te.propFull() * 100f) + "%",
				x + 117, y + 26, 0xFF_00fefe, 0xFF_0e0e0e);

		String title = StringUtils.translateScreen("tank.stored");
		String value = fluid.isEmpty() ? StringUtils.translateScreen("tank.none") : fluid.getDisplayName().getString();
		UIHelper.drawCenteredStringWithBorder(stack, title, x + 118 - UIHelper.getWidth(value) / 2, y + 60, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, value, x + 118 + UIHelper.getWidth(title) / 2, y + 60,
				fluid.isEmpty() ? 0xFF_ff0000 : fluid.getFluid().getAttributes().getColor(), 0xFF_0e0e0e);

		UIHelper.drawStringWithBorder(stack, "MACHINA://TANK/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
