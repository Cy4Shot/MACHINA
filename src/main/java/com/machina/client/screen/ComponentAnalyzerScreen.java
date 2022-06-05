package com.machina.client.screen;

import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.client.util.UIHelper;
import com.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ComponentAnalyzerScreen extends NoJeiContainerScreen<ComponentAnalyzerContainer> {
	private static final MachinaRL SCIFI_EL = new MachinaRL("textures/gui/scifi_el.png");

	public ComponentAnalyzerScreen(ComponentAnalyzerContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public int getSlotColor(int index) {
		if (index < this.menu.te.getContainerSize())
			return 0x00_FFFFFF;
		else
			return super.getSlotColor(index);
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
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.textureManager.bind(SCIFI_EL);

		// Back
		int xSize = 236, ySize = 99;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.blit(stack, x, y, 2, 3, xSize, ySize);
		this.blit(stack, x + 32, y + 102, 3, 200, 174, 30);

		// Progress
		int percentage = (int) (((float) this.menu.te.progress / 100f) * 129f);
		this.blit(stack, x + 50, y + 38, 3, 130, 135, 18);
		this.blit(stack, x + 52, y + 40, 3, 103, percentage, 12);

		// Slots
		this.blit(stack, x + 24, y + 36, 3, 150, 23, 23);
		this.blit(stack, x + 187, y + 36, 29, 150, 23, 23);

		// Text
		if (this.menu.te.progress == 0)
			UIHelper.drawStringWithBorder(stack, "Insert Component", x + 73, y + 42, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://COMP_ANALYZER/", x + 8, y + 82, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
