package com.machina.client.screen;

import java.util.Arrays;

import com.machina.block.container.PressurizedChamberContainer;
import com.machina.block.tile.base.IMultiFluidTileEntity;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.config.CommonConfig;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SPressurizedChamberClear;
import com.machina.network.c2s.C2SPressurizedChamberRunning;
import com.machina.util.color.Color;
import com.machina.util.math.MathUtil;
import com.machina.util.server.HeatUtils;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
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

		FluidStack s1 = this.menu.te.getFluid(0);
		FluidStack s2 = this.menu.te.getFluid(1);
		FluidStack s3 = this.menu.te.getFluid(2);

		int p1 = (int) ((float) s1.getAmount() / (float) this.menu.te.tanks.get(0).getCapacity() * 91f);
		int p2 = (int) ((float) s2.getAmount() / (float) this.menu.te.tanks.get(1).getCapacity() * 91f);
		int p3 = (int) ((float) s3.getAmount() / (float) this.menu.te.tanks.get(2).getCapacity() * 91f);

		UIHelper.renderFluid(stack, s1, x + 5, y + 23, 15, p1, 15, 91, getBlitOffset(), pX, pY);
		UIHelper.renderFluid(stack, s2, x + 25, y + 23, 15, p2, 15, 91, getBlitOffset(), pX, pY);
		UIHelper.renderFluid(stack, s3, x + 45, y + 23, 15, p3, 15, 91, getBlitOffset(), pX, pY);

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

		if (pX > x + 44 && pX < x + 44 + 17 && pY > y + 4 && pY < y + 4 + 17) {
			this.blit(stack, x + 44, y + 4, 237, 17, 17, 17);
			UIHelper.renderLabel(stack, Arrays.asList(StringUtils.translateCompScreen("pressurized_chamber.clear")), pX,
					pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			UIHelper.bindTrmnl();
		} else {
			this.blit(stack, x + 44, y + 4, 237, 0, 17, 17);
		}

		this.blit(stack, x + 80, y + 84, 0, 202, 117, 32);

		if (this.menu.te.isRunning) {
			if (pX > x + 197 && pX < x + 197 + 17 && pY > y + 91 && pY < y + 91 + 17) {
				this.blit(stack, x + 197, y + 91, 237, 141, 17, 17);
				UIHelper.renderLabel(stack, Arrays.asList(StringUtils.translateCompScreen("pressurized_chamber.pause")),
						pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
				UIHelper.bindTrmnl();
			} else {
				this.blit(stack, x + 197, y + 91, 237, 124, 17, 17);
			}
		} else {
			if (pX > x + 197 && pX < x + 197 + 17 && pY > y + 91 && pY < y + 91 + 17) {
				this.blit(stack, x + 197, y + 91, 237, 107, 17, 17);
				UIHelper.renderLabel(stack, Arrays.asList(StringUtils.translateCompScreen("pressurized_chamber.start")),
						pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
				UIHelper.bindTrmnl();
			} else {
				this.blit(stack, x + 197, y + 91, 237, 90, 17, 17);
			}
		}

		UIHelper.bindScifi();

		this.blit(stack, x + 23, y + 170, 228, 184, 19, 19);

		this.blit(stack, x + 32, y + 212, 3, 200, 174, 30);

		this.blit(stack, x + 80, y + 20, 3, 130, 135, 18);
		int energy = (int) (this.menu.te.propFull() * 129f);
		this.blit(stack, x + 82, y + 22, 3, 103, energy, 12);

		this.blit(stack, x + 80, y + 56, 3, 130, 135, 18);
		int heat = (int) (this.menu.te.heatFull() * 129f);
		this.blit(stack, x + 82, y + 58, 3, 115, heat, 12);

		this.blit(stack, x + 80, y + 151, 3, 130, 135, 18);
		this.blit(stack, x + 137, y + 170, 228, 184, 19, 19);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.getEnergy(), "RF") + " / "
						+ MathUtil.engineering(this.menu.te.getMaxEnergy(), "RF") + " - "
						+ String.format("%.01f", this.menu.te.propFull() * 100f) + "%",
				x + 145, y + 8, 0xFF_00fefe, 0xFF_0e0e0e);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering(this.menu.te.normalized(), "K") + " / "
						+ MathUtil.engineering(CommonConfig.maxHeat.get(), "K") + " - "
						+ String.format("%.01f", this.menu.te.heatFull() * 100f) + "%",
				x + 145, y + 44, 0xFF_00fefe, 0xFF_0e0e0e);

		FluidStack s4 = this.menu.te.getFluid(3);
		int p4 = (int) ((float) s4.getAmount() / (float) this.menu.te.tanks.get(3).getCapacity() * 132f);
		UIHelper.renderFluid(stack, s4, x + 81, y + 152, p4, 15, 132, 15, getBlitOffset(), pX, pY);

		UIHelper.drawCenteredStringWithBorder(stack,
				MathUtil.engineering((double) s4.getAmount() / (double) IMultiFluidTileEntity.BUCKET, "B") + " / "
						+ MathUtil.engineering((double) this.menu.te.tanks.get(3).getCapacity()
								/ (double) IMultiFluidTileEntity.BUCKET, "B")
						+ " - " + String.format("%.01f", this.menu.te.tanks.get(3).propFull() * 100f) + "%",
				x + 146, y + 128, 0xFF_00fefe, 0xFF_0e0e0e);

		String title = StringUtils.translateScreen("pressurized_chamber.stored");
		String value = s4.isEmpty() ? StringUtils.translateScreen("pressurized_chamber.none")
				: s4.getDisplayName().getString();
		UIHelper.drawCenteredStringWithBorder(stack, title, x + 146 - UIHelper.getWidth(value) / 2, y + 140,
				0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, value, x + 146 + UIHelper.getWidth(title) / 2, y + 140,
				s4.isEmpty() ? 0xFF_ff0000 : s4.getFluid().getAttributes().getColor(), 0xFF_0e0e0e);

		if (this.menu.te.result != "") {
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translate(this.menu.te.result), x + 138, y + 100,
					new Color(this.menu.te.color).maxBrightness().toInt(), 0xFF_0e0e0e);
			if (this.menu.te.heat >= this.menu.te.reqHeat) {
				if (this.menu.te.isRunning) {
					UIHelper.drawCenteredStringWithBorder(stack,
							StringUtils.translateScreen("pressurized_chamber.crafting"), x + 138, y + 89, 0xFF_00fefe,
							0xFF_0e0e0e);
				} else {
					UIHelper.drawCenteredStringWithBorder(stack,
							StringUtils.translateScreen("pressurized_chamber.ready"), x + 138, y + 89, 0xFF_00fefe,
							0xFF_0e0e0e);
				}
			} else {
				UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("pressurized_chamber.heat"),
						x + 138, y + 89, 0xFF_ff0000, 0xFF_0e0e0e);
			}

		} else {
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("pressurized_chamber.waiting"),
					x + 138, y + 89, 0xFF_ff0000, 0xFF_0e0e0e);
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("pressurized_chamber.no_recipe"),
					x + 138, y + 100, 0xFF_ff0000, 0xFF_0e0e0e);
		}

		// Draw decorators

		UIHelper.bindPrgrs();

		if (this.menu.te.result != "") {
			int req = (int) (HeatUtils.propFull(this.menu.te.reqHeat, this.menu.te.getLevel().dimension()) * 129f);
			this.blit(stack, x + 82 + req, y + 57, 97, 242, 2, 14);
		}

		this.blit(stack, x + 18, y + 171, 0, 239, 29, 17);
		this.blit(stack, x + 132, y + 171, 0, 239, 29, 17);
		this.blit(stack, x + 112, y + 168, 29, 243, 68, 13);

		boolean r1 = this.menu.te.tanks.get(0).isEmpty();
		boolean r2 = this.menu.te.tanks.get(1).isEmpty();
		boolean r3 = this.menu.te.tanks.get(2).isEmpty();
		boolean r4 = this.menu.te.getItem(0).isEmpty();

		// Left
		if (!r1) {
			this.blit(stack, x + 10, y + 115, 0, 0, 20, 30);
		} else {
			this.blit(stack, x + 10, y + 115, 44, 0, 20, 30);
		}

		// Middle
		if (!r2) {
			this.blit(stack, x + 30, y + 115, 20, 0, 4, 26);
		} else {
			this.blit(stack, x + 30, y + 115, 64, 0, 4, 26);
		}

		// Right
		if (!r3) {
			this.blit(stack, x + 34, y + 115, 24, 0, 20, 30);
		} else {
			this.blit(stack, x + 34, y + 115, 68, 0, 20, 30);
		}

		// Catalyst
		if (!r4) {
			this.blit(stack, x + 30, y + 144, 20, 29, 4, 27);
		} else {
			this.blit(stack, x + 30, y + 144, 64, 29, 4, 27);
		}

		// Centre Node
		if (!(r1 && r2 && r3 && r4)) {
			this.blit(stack, x + 31, y + 141, 21, 26, 2, 2);
		} else {
			this.blit(stack, x + 31, y + 141, 65, 26, 2, 2);
		}

		UIHelper.drawStringWithBorder(stack, "MACHINA://PRESS_CHAMBER/", x + 6, y + 195, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pButton) {
		if (pButton == 0) {
			int xSize = 236, ySize = 210;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;

			// Clear 1
			if (pX > x + 4 && pX < x + 4 + 17 && pY > y + 4 && pY < y + 4 + 17) {
				UIHelper.click();
				MachinaNetwork.CHANNEL.sendToServer(new C2SPressurizedChamberClear(this.menu.te.getBlockPos(), 0));
			}

			// Clear 2
			if (pX > x + 24 && pX < x + 24 + 17 && pY > y + 4 && pY < y + 4 + 17) {
				UIHelper.click();
				MachinaNetwork.CHANNEL.sendToServer(new C2SPressurizedChamberClear(this.menu.te.getBlockPos(), 1));
			}

			// Clear 3
			if (pX > x + 44 && pX < x + 44 + 17 && pY > y + 4 && pY < y + 4 + 17) {
				UIHelper.click();
				MachinaNetwork.CHANNEL.sendToServer(new C2SPressurizedChamberClear(this.menu.te.getBlockPos(), 2));
			}

			// Pause / Run
			if (pX > x + 197 && pX < x + 197 + 17 && pY > y + 91 && pY < y + 91 + 17) {
				UIHelper.click();
				MachinaNetwork.CHANNEL.sendToServer(new C2SPressurizedChamberRunning(this.menu.te.getBlockPos()));
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
