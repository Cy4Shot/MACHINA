package com.machina.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.machina.block.container.AtmosphericSeperatorContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class AtmosphericSeperatorScreen extends NoJeiContainerScreen<AtmosphericSeperatorContainer> {

	public AtmosphericSeperatorScreen(AtmosphericSeperatorContainer menu, PlayerInventory inv, ITextComponent title) {
		super(menu, inv, title);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public void render(MatrixStack stack, int pX, int pY, float par) {
		UIHelper.resetColor();

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);

		// Render
		super.render(stack, pX, pY, par);
		this.renderTooltip(stack, pX, pY);
	}

	@Override
	protected void renderBg(MatrixStack stack, float par, int pX, int pY) {
		UIHelper.bindLarge();
		int xSize = 237, ySize = 211;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 0, 0, xSize, ySize);

		for (int i = 0; i < FluidInit.ATMOSPHERE.size(); i++) {
			UIHelper.bindLarge();
			int newX = x + 4;
			int newY = y + 4 + i * 20;

			FluidObject obj = FluidInit.ATMOSPHERE.get(i);

			if (pX > newX && pX < newX + 94 && pY > newY && pY < newY + 18) {
				this.blit(stack, newX, newY, 0, 229, 94, 18);
			} else {
				if (i == this.menu.te.selected) {
					this.blit(stack, newX, newY, 0, 211, 94, 18);
				} else {
					this.blit(stack, newX, newY, 94, 211, 94, 18);
				}
			}

			this.blit(stack, newX + 96, newY, 94, 211, 94, 18);
			UIHelper.renderFluid(stack, new FluidStack(obj.fluid(), 1), newX + 97, newY + 1, 91, 15, getBlitOffset());

			if (pX > newX + 96 && pX < newX + 96 + 94 && pY > newY && pY < newY + 18) {
				List<ITextComponent> tooltip = new ArrayList<>();
				tooltip.add(StringUtils.toComp(obj.chem().getDisplayName()));
				UIHelper.renderLabel(stack, tooltip, pX, pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			}

			UIHelper.drawStringWithBorder(stack, obj.chem().getDisplayName(), newX + 4, newY + 4, 0xFF_00fefe,
					0xFF_0e0e0e);
		}

		UIHelper.drawStringWithBorder(stack, "MACHINA://ATM_SEPERATOR/", x + 6, y + 194, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	protected void renderLabels(MatrixStack stack, int pX, int pY) {
	}

}
