package com.cy4.machina.api.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface Renderable {

	void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);

}
