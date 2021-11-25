/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.client.gui;

import java.util.LinkedList;
import java.util.List;

import com.cy4.machina.api.client.gui.element.GuiElement;
import com.cy4.machina.api.container.BaseContainer;
import com.cy4.machina.api.util.helper.RenderHelper;
import com.cy4.machina.util.MachinaRL;
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

	protected ResourceLocation textureRL(String name) {
		return new MachinaRL("textures/gui/" + name);
	}

}
