/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface Renderable {

	void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);

}
