/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.client.gui.element;

import com.machina.api.starchart.PlanetData;
import com.machina.client.gui.StarchartScreen;
import com.machina.client.util.UIHelper;
import com.machina.client.util.UIHelper.StippleType;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlanetNodeElement extends Widget {

	StarchartScreen screen;
	PlanetData data;

	public PlanetNodeElement(float pX, float pY, StarchartScreen screen, PlanetData data) {
		super((int) pX, (int) pY, 3, 3, new StringTextComponent(""));
		this.screen = screen;
		active = true;
		visible = true;
		this.data = data;
	}

	public PlanetData getData() { return data; }

	@Override
	public void onClick(double pMouseX, double pMouseY) {
		super.onClick(pMouseX, pMouseY);

		screen.selected = this;
	}

	@Override
	public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bind(StarchartScreen.SC_RS);
		Vector2f centre = screen.getNewCentre();

		float offsetC = (float) (8 * Math.cos(mc.levelRenderer.ticks / 10f) + 8);
		int textureSize = 3;

		if (true) {
			UIHelper.line(matrixStack, centre.x, centre.y, x + textureSize / 2f, y + textureSize / 2f, 0xFFFFFFFF, 3f,
					StippleType.DASHED);
		}
		UIHelper.betterBlit(matrixStack, x, y, textureSize * (int) offsetC, 0, textureSize, textureSize, 128);

		if (screen.selected != null && screen.selected.equals(this)) {
			UIHelper.box(matrixStack, x - 2, y - 2, x + textureSize + 2, y + textureSize + 2, 0xFFFFFFFF, 4f,
					StippleType.FULL);
		}
	}

}
