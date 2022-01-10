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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.client.screen;

import java.util.LinkedList;
import java.util.List;

import com.machina.api.container.BaseContainer;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.helper.RenderHelper;
import com.machina.client.screen.element.GuiElement;
import com.machina.client.util.IGui;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class BaseScreen<C extends BaseContainer> extends ContainerScreen<C> implements IGui {

	public final C container;
	public final ResourceLocation guiTexture;
	public final List<GuiElement> elements = new LinkedList<>();

	protected int displayNameColour = 0x9e13c3;

	protected BaseScreen(C pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle,
			ResourceLocation guiTexture) {
		super(pMenu, pPlayerInventory, pTitle);
		this.container = pMenu;
		this.guiTexture = guiTexture;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		minecraft.textureManager.bind(this.guiTexture);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		this.blit(pMatrixStack, x, y, 0, 0, imageWidth, imageHeight);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		elements.forEach(elem -> elem.drawBg(matrixStack, mouseX, mouseY, RenderHelper.getPartialTick()));
		if (container.containerName != null) {
			font.draw(matrixStack, container.containerName, titleLabelX, titleLabelY, displayNameColour);
		}
		font.draw(matrixStack, inventory.getDisplayName(), inventoryLabelX, inventoryLabelY, displayNameColour);
	}

	protected static ResourceLocation textureRL(String name) {
		return new MachinaRL("textures/gui/" + name);
	}

}
