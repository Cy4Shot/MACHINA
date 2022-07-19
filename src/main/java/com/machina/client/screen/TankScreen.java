package com.machina.client.screen;

import com.machina.block.container.TankContainer;
import com.machina.client.ClientStarchart;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.registration.init.AttributeInit;
import com.machina.util.color.Color;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.machina.world.data.PlanetData;
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

		// Progress
		FluidStack fluid = this.menu.te.getFluid();
		int percentage = (int) (this.menu.te.propFull() * 132f);
		this.blit(stack, x + 50, y + 38, 3, 130, 135, 18);
		UIHelper.renderFluid(stack, fluid, x + 51, y + 39, percentage, 15, this.getBlitOffset());

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.stored(), "B") + " / "
						+ MathUtil.engineering(this.menu.te.capacity(), "B") + " - "
						+ String.format("%.01f", this.menu.te.propFull() * 100f) + "%",
				x + 117, y + 26, 0xFF_00fefe, 0xFF_0e0e0e);

//		String name = fluid.getDisplayName().getString();
//
//		UIHelper.drawCenteredStringWithBorder(stack, "Stored: ", x + 117 - UIHelper.getWidth(name) / 2, y + 60,
//				0xFF_00fefe, 0xFF_0e0e0e);
//
//		UIHelper.drawCenteredStringWithBorder(stack, name, x + 117 + UIHelper.getWidth(name) / 2, y + 60,
//				fluid.getFluid().getAttributes().getColor(), 0xFF_0e0e0e);

		String title = "Stored: ";
		String value = fluid.isEmpty() ? "None " : fluid.getDisplayName().getString();
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
