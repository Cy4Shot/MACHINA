package com.machina.client.screen;

import java.util.Arrays;

import com.machina.block.container.PressurizedChamberContainer;
import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.registration.init.FluidInit;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class PressurizedChamberScreen extends NoJeiContainerScreen<PressurizedChamberContainer> {

	public PressurizedChamberScreen(PressurizedChamberContainer pMenu, PlayerInventory pPlayerInventory,
			ITextComponent pTitle) {
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

		FluidStack s1 = new FluidStack(FluidInit.NITROGEN.fluid(), 1);
		FluidStack s2 = new FluidStack(FluidInit.CARBON_DIOXIDE.fluid(), 1);
		FluidStack s3 = new FluidStack(Fluids.WATER, 1);

		UIHelper.renderFluid(stack, s1, x + 5, y + 23, 15, 52, 15, 91, getBlitOffset(), pX, pY);
		UIHelper.renderFluid(stack, s2, x + 25, y + 23, 15, 12, 15, 91, getBlitOffset(), pX, pY);
		UIHelper.renderFluid(stack, s3, x + 45, y + 23, 15, 83, 15, 91, getBlitOffset(), pX, pY);

		UIHelper.bindTrmnl();

		if (pX > x + 4 && pX < x + 4 + 17 && pY > y + 4 && pY < y + 4 + 17) {
			this.blit(stack, x + 4, y + 4, 237, 17, 17, 17);
			UIHelper.renderLabel(stack,
					Arrays.asList(StringUtils.translateComp("machina.screen.pressurized_chamber.clear")), pX, pY,
					0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			this.blit(stack, x + 4, y + 4, 237, 0, 17, 17);
		}

		if (pX > x + 24 && pX < x + 24 + 17 && pY > y + 4 && pY < y + 4 + 17) {
			this.blit(stack, x + 24, y + 4, 237, 17, 17, 17);
			UIHelper.renderLabel(stack,
					Arrays.asList(StringUtils.translateComp("machina.screen.pressurized_chamber.clear")), pX, pY,
					0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			this.blit(stack, x + 24, y + 4, 237, 0, 17, 17);
		}

		if (pX > x + 44 && pX < x + 44 + 17 && pY > y + 4 && pY < y + 4 + 17) {
			this.blit(stack, x + 44, y + 4, 237, 17, 17, 17);
			UIHelper.renderLabel(stack,
					Arrays.asList(StringUtils.translateComp("machina.screen.pressurized_chamber.clear")), pX, pY,
					0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			this.blit(stack, x + 44, y + 4, 237, 0, 17, 17);
		}

		this.blit(stack, x + 80, y + 84, 0, 202, 117, 32);

		// Running
		if (false) {
			if (pX > x + 197 && pX < x + 197 + 17 && pY > y + 91 && pY < y + 91 + 17) {
				this.blit(stack, x + 197, y + 91, 237, 141, 17, 17);
				UIHelper.renderLabel(stack,
						Arrays.asList(StringUtils.translateComp("machina.screen.pressurized_chamber.pause")), pX, pY,
						0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
				UIHelper.bindTrmnl();
			} else {
				this.blit(stack, x + 197, y + 91, 237, 124, 17, 17);
			}
		} else {
			if (pX > x + 197 && pX < x + 197 + 17 && pY > y + 91 && pY < y + 91 + 17) {
				this.blit(stack, x + 197, y + 91, 237, 107, 17, 17);
				UIHelper.renderLabel(stack,
						Arrays.asList(StringUtils.translateComp("machina.screen.pressurized_chamber.start")), pX, pY,
						0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
				UIHelper.bindTrmnl();
			} else {
				this.blit(stack, x + 197, y + 91, 237, 90, 17, 17);
			}
		}

		UIHelper.bindScifi();

		this.blit(stack, x + 23, y + 170, 228, 184, 19, 19);

		this.blit(stack, x + 32, y + 212, 3, 200, 174, 30);

		this.blit(stack, x + 80, y + 20, 3, 130, 135, 18);
		this.blit(stack, x + 82, y + 22, 3, 103, 129, 12);

		this.blit(stack, x + 80, y + 56, 3, 130, 135, 18);
		this.blit(stack, x + 82, y + 58, 3, 115, 129, 12);

		this.blit(stack, x + 80, y + 151, 3, 130, 135, 18);
		this.blit(stack, x + 137, y + 170, 228, 184, 19, 19);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF") + " - "
						+ String.format("%.01f", this.menu.te.propFull() * 100f) + "%",
				x + 145, y + 8, 0xFF_00fefe, 0xFF_0e0e0e);

		UIHelper.drawCenteredStringWithBorder(stack, MathUtil.engineering(0, "K") + " / "
				+ MathUtil.engineering(1000, "K") + " - " + String.format("%.01f", 0 * 100f) + "%", x + 145, y + 44,
				0xFF_00fefe, 0xFF_0e0e0e);

		FluidStack s4 = new FluidStack(FluidInit.AMMONIA.fluid(), 1);
		UIHelper.renderFluid(stack, s4, x + 81, y + 152, 132, 15, 132, 15, getBlitOffset(), pX, pY);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering((double) 0 / (double) IFluidTileEntity.BUCKET, "B") + " / "
						+ MathUtil.engineering((double) 10000 / (double) IFluidTileEntity.BUCKET, "B") + " - "
						+ String.format("%.01f", 0 * 100f) + "%",
				x + 146, y + 128, 0xFF_00fefe, 0xFF_0e0e0e);

		String title = StringUtils.translate("machina.screen.tank.stored");
		String value = s4.isEmpty() ? StringUtils.translate("machina.screen.tank.none")
				: s4.getDisplayName().getString();
		UIHelper.drawCenteredStringWithBorder(stack, title, x + 146 - UIHelper.getWidth(value) / 2, y + 140,
				0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, value, x + 146 + UIHelper.getWidth(title) / 2, y + 140,
				s4.isEmpty() ? 0xFF_ff0000 : s4.getFluid().getAttributes().getColor(), 0xFF_0e0e0e);

		// In Progress
		if (false) {
			UIHelper.drawCenteredStringWithBorder(stack,
					StringUtils.translate("machina.screen.pressurized_chamber.crafting"), x + 137, y + 89, 0xFF_00fefe,
					0xFF_0e0e0e);
		} else {
			// Has Required Items
			if (false) {
				UIHelper.drawCenteredStringWithBorder(stack,
						StringUtils.translate("machina.screen.pressurized_chamber.ready"), x + 137, y + 89, 0xFF_00fefe,
						0xFF_0e0e0e);
			} else {
				UIHelper.drawCenteredStringWithBorder(stack,
						StringUtils.translate("machina.screen.pressurized_chamber.waiting"), x + 137, y + 89,
						0xFF_ff0000, 0xFF_0e0e0e);
			}
		}

		// Recipe found
		if (false) {
			UIHelper.drawCenteredStringWithBorder(stack,
					StringUtils.translate("machina.screen.pressurized_chamber.recipe"), x + 138, y + 100, 0xFF_00fefe,
					0xFF_0e0e0e);
		} else {
			UIHelper.drawCenteredStringWithBorder(stack,
					StringUtils.translate("machina.screen.pressurized_chamber.no_recipe"), x + 138, y + 100,
					0xFF_ff0000, 0xFF_0e0e0e);
		}

		UIHelper.bindPrgrs();

		this.blit(stack, x + 18, y + 171, 0, 239, 29, 17);
		this.blit(stack, x + 132, y + 171, 0, 239, 29, 17);
		this.blit(stack, x + 112, y + 168, 29, 243, 68, 13);

		// Left
		if (true) {
			this.blit(stack, x + 10, y + 115, 0, 0, 20, 30);
		} else {
			this.blit(stack, x + 10, y + 115, 44, 0, 20, 30);
		}

		// Middle
		if (true) {
			this.blit(stack, x + 30, y + 115, 20, 0, 4, 26);
		} else {
			this.blit(stack, x + 30, y + 115, 64, 0, 4, 26);
		}

//		// Right
		if (true) {
			this.blit(stack, x + 34, y + 115, 24, 0, 20, 30);
		} else {
			this.blit(stack, x + 34, y + 115, 68, 0, 20, 30);
		}
//
//		// Catalyst
		if (true) {
			this.blit(stack, x + 30, y + 144, 20, 29, 4, 27);
		} else {
			this.blit(stack, x + 30, y + 144, 64, 29, 4, 27);
		}
//
//		// Centre Node
		if (true) {
			this.blit(stack, x + 31, y + 141, 21, 26, 2, 2);
		} else {
			this.blit(stack, x + 31, y + 141, 65, 26, 2, 2);
		}

		UIHelper.drawStringWithBorder(stack, "MACHINA://PRESS_CHAMBER/", x + 6, y + 195, 0xFF_00fefe, 0xFF_0e0e0e);
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
