/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
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

package com.machina.api.client.gui.element;

import javax.annotation.Nonnull;

import com.cy4.machina.util.MachinaRL;
import com.machina.api.client.gui.IFontRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

/**
 * Not done yet
 *
 * @author matyrobbrt
 *
 */
public class GuiElement extends Widget implements IFontRenderer {

	public final ResourceLocation texture;
	public final int renderX;
	public final int renderY;

	public GuiElement(int pX, int pY, int pWidth, int pHeight, int renderX, int renderY, ITextComponent pMessage,
			ResourceLocation texture) {
		super(pX, pY, pWidth, pHeight, pMessage);
		this.texture = texture;
		this.renderX = renderX;
		this.renderY = renderY;
	}

	@Override
	public int getXSize() { return width; }

	@SuppressWarnings("resource")
	@Override
	public FontRenderer getFont() { return Minecraft.getInstance().font; }

	public void drawBg(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontrenderer = minecraft.font;
		minecraft.getTextureManager().bind(texture);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
		// int i = this.getYImage(this.isHovered());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		this.blit(pMatrixStack, renderX, renderY, x, y, width / 2, height);
		this.blit(pMatrixStack, renderX, renderY, x, y, width / 2, height);
		this.renderBg(pMatrixStack, minecraft, pMouseX, pMouseY);
		int j = getFGColor();
		drawCenteredString(pMatrixStack, fontrenderer, this.getMessage(), x + width / 2, y + (height - 8) / 2,
				j | MathHelper.ceil(alpha * 255.0F) << 24);
	}

	protected static ResourceLocation texturePath(String name) {
		return new MachinaRL("textures/gui/element/" + name);
	}

}
