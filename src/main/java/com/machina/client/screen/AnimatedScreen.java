package com.machina.client.screen;

import com.machina.client.util.ClientTimer;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public abstract class AnimatedScreen extends Screen {

	protected AnimatedScreen(ITextComponent title, int introTick) {
		super(title);
	}

	@Override
	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		System.out.println("=================");
		System.out.println(pPartialTicks);
		System.out.println(ClientTimer.deltaTick);
		System.out.println(ClientTimer.gameTick);
		System.out.println(ClientTimer.partialTick);
		System.out.println(ClientTimer.totalTick);
	}

	public void renderIntroAnimation(MatrixStack stack, int tick) {

	}

}
