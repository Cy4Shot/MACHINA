package com.machina.modules.test;

import com.machina.api.client.gui.BaseScreen;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class TestScreen extends BaseScreen<TestContainer> {

	protected TestScreen(TestContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle, textureRL("test_screen.png"));
		
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 220;
		this.imageHeight = 201;
	}
	
	@Override
	protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
		System.out.println(this.menu.data.get(0));
		super.renderBg(pMatrixStack, pPartialTicks, pX, pY);
		for (int i = 0; i <= 76; ++i) {
			if (this.menu.getEnergyScaled() >= i) {
				this.blit(pMatrixStack, this.leftPos + 161, this.topPos + 4 + 76 - i, 249, 0 + 76 - i, 7, 1);
			}
		}
	}

}
