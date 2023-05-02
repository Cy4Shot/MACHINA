package com.machina.client.screen;

import com.machina.block.container.FuelStorageUnitContainer;
import com.machina.block.tile.machine.FuelStorageUnitTileEntity;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.helper.HeatHelper;
import com.machina.util.math.MathUtil;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class FuelStorageUnitScreen extends NoJeiContainerScreen<FuelStorageUnitContainer> {
	public FuelStorageUnitScreen(FuelStorageUnitContainer pMenu, PlayerInventory pPlayerInventory,
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
		UIHelper.bindScifi();

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);
		this.blit(stack, x + 32, y + 102, 3, 200, 174, 30);

		int temp = (int) (this.menu.te.heatFull() * 129f);
		this.blit(stack, x + 50, y + 20, 3, 130, 135, 18);
		this.blit(stack, x + 52, y + 22, 3, 115, temp, 12);

		// Slots
		this.blit(stack, x + 24, y + 57, 228, 184, 19, 19);
		this.blit(stack, x + 191, y + 57, 228, 184, 19, 19);
		renderHintItem(stack, 0, x + 25, y + 58);
		renderHintItem(stack, 1, x + 192, y + 58);
		UIHelper.bindScifi();

		// Progress
		FluidStack fluid = this.menu.te.getFluid();
		int fl = (int) (this.menu.te.propFull() * 132f);
		this.blit(stack, x + 50, y + 58, 3, 130, 135, 18);
		UIHelper.renderFluid(stack, fluid, x + 51, y + 59, fl, 15, 132, 15, getBlitOffset(), pX, pY, true);

		// Decorate Slots
		UIHelper.bindPrgrs();
		this.blit(stack, x + 19, y + 58, 0, 239, 29, 17);
		this.blit(stack, x + 186, y + 58, 0, 239, 29, 17);

		RegistryKey<World> dim = this.menu.te.getLevel().dimension();
		float max = FuelStorageUnitTileEntity.maxTemp;
		float heat = this.menu.te.normalizedHeat();

		int req = (int) (HeatHelper.propFull(max, dim) * 129f);
		this.blit(stack, x + 51 + req, y + 21, 99, 242, 2, 14);

		UIHelper.drawCenteredStringWithBorder(stack,
				StringUtils.translateScreen("fuel_storage.stored") + MathUtil.engineering(heat, "K"), x + 117, y + 7,
				heat > max ? 0xFF_ff0000 : 0xFF_00fefe, 0xFF_0e0e0e);

		if (heat > max) {
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("fuel_storage.depleting"), x + 117,
					y + 46, 0xFF_ff0000, 0xFF_0e0e0e);
		} else {
			UIHelper.drawCenteredStringWithBorder(stack,
					MathUtil.engineering((double) this.menu.te.stored() / (double) 1000, "B") + " / "
							+ MathUtil.engineering((double) this.menu.te.capacity() / (double) 1000, "B") + " - "
							+ String.format("%.01f", this.menu.te.propFull() * 100f) + "%",
					x + 117, y + 46, 0xFF_00fefe, 0xFF_0e0e0e);
		}

		UIHelper.drawStringWithBorder(stack, "MACHINA://FUEL_STORAGE/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	private void renderHintItem(MatrixStack stack, int slot, int x, int y) {
		if (!this.menu.getSlot(slot).hasItem()) {
			ItemStack hint = this.menu.getCompletableSlot(slot).getBackground();
			UIHelper.renderTintedItem(stack, hint, x, y, 35, 35, 35, 0.7f);
		}
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
