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

package com.cy4.machina.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.cy4.machina.client.gui.element.PlanetNodeElement;
import com.cy4.machina.client.gui.element.ScrollableContainer;
import com.cy4.machina.client.util.UIHelper;
import com.machina.api.client.ClientStarchartHolder;
import com.machina.api.client.gui.IBoundedGui;
import com.machina.api.client.util.Rectangle;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.starchart.PlanetData;
import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StarchartScreen extends Screen implements IBoundedGui {

	public static final ResourceLocation SC_BG = new MachinaRL("textures/gui/starchart/starchart_bg.png");
	public static final ResourceLocation SC_RS = new MachinaRL("textures/gui/starchart/starchart_rs.png");

	List<Vector2f> positions = new ArrayList<>();
	List<PlanetNodeElement> nodes = new ArrayList<>();
	private ScrollableContainer planetDescriptions;

	public PlanetNodeElement selected = null;

	public StarchartScreen() {
		super(new TranslationTextComponent("machina.screen.starchart.title"));

		planetDescriptions = new ScrollableContainer(this::renderDescription,
				new StringTextComponent("Planet Statistics"));
	}

	@Override
	protected void init() {
		super.init();

		createStarSystem(40D, 10D);

		for (PlanetNodeElement ne : nodes) {
			this.addWidget(ne);
		}
	}

	public Vector2f getNewCentre() {
		Vector2f centre = getCentre();
		return new Vector2f(centre.x - 70, centre.y);
	}

	private void createStarSystem(double variance, double min) {

		positions.clear();
		nodes.clear();
		Vector2f centre = getNewCentre();

		for (int i = 1; i <= ClientStarchartHolder.getStarchart().planets.size(); i++) {
			double angle = (Math.PI * 2) / ClientStarchartHolder.getStarchart().planets.size() * i;

			double r = ClientStarchartHolder.getPlanetDataByID(i - 1).dist * variance + min;

			float x = (float) (r * Math.cos(angle));
			float y = (float) (r * Math.sin(angle));

			positions.add(new Vector2f(centre.x - x, centre.y - y));
			nodes.add(new PlanetNodeElement(centre.x - x, centre.y - y, this,
					ClientStarchartHolder.getPlanetDataByID(i - 1)));
		}
	}

	@Override
	public Rectangle getContainerBounds() {
		Rectangle bounds = new Rectangle();
		bounds.x0 = (int) (width * 0.2);
		bounds.y0 = (int) (height * 0.2);
		bounds.x1 = (int) (width * 0.8);
		bounds.y1 = (int) (height * 0.8);
		return bounds;
	}

	public Rectangle getDescriptionsBounds() {
		Rectangle headingBounds = getContainerBounds();
		Rectangle descriptionsBounds = new Rectangle();
		descriptionsBounds.x0 = headingBounds.x0 + headingBounds.getWidth() / 2;
		descriptionsBounds.y0 = headingBounds.y0 + 20;
		descriptionsBounds.x1 = headingBounds.x1;
		descriptionsBounds.y1 = headingBounds.y1 - 10;
		return descriptionsBounds;
	}

	@Override
	public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(matrixStack, pMouseX, pMouseY, pPartialTicks); // Buttons

		Rectangle bound = this.getContainerBounds();

		// Background
		UIHelper.renderOverflowHidden(matrixStack, this::renderContainerBackground, MatrixStack::toString);

		// Elements
		renderStarSystem(matrixStack, pMouseX, pMouseY, pPartialTicks);

		// Description
		planetDescriptions.setBounds(getDescriptionsBounds());
		planetDescriptions.render(matrixStack, pMouseX, pMouseY, pPartialTicks);

		// Border
		UIHelper.renderContainerBorder(matrixStack, bound);
		UIHelper.drawStringWithBorder(matrixStack, title.getString(), bound.x0, bound.y0 - 12, 0xFF_cc00ff,
				0xFF_0e0e0e);
	}

	private void renderContainerBackground(MatrixStack matrixStack) {
		assert minecraft != null;

		minecraft.getTextureManager().bind(SC_BG);

		Rectangle containerBounds = getContainerBounds();

		int textureSize = 1536;
		int currentX = containerBounds.x0;
		int currentY = containerBounds.y0;
		int uncoveredWidth = containerBounds.getWidth();
		int uncoveredHeight = containerBounds.getHeight();
		while (uncoveredWidth > 0) {
			while (uncoveredHeight > 0) {
				UIHelper.blit(matrixStack, currentX, currentY, textureSize * 1, 0,
						Math.min(textureSize, uncoveredWidth), Math.min(textureSize, uncoveredHeight));
				uncoveredHeight -= textureSize;
				currentY += textureSize;
			}

			// Decrement
			uncoveredWidth -= textureSize;
			currentX += textureSize;

			// Reset
			uncoveredHeight = containerBounds.getHeight();
			currentY = containerBounds.y0;
		}
	}

	private void renderStarSystem(MatrixStack matrixStack, int mX, int mY, float pTicks) {
		nodes.forEach(node -> node.render(matrixStack, mX, mY, pTicks));
	}

	private void renderDescription(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		planetDescriptions.setInnerHeight(UIHelper.renderWrappedText(matrixStack, getDescriptionForPlanet(),
				planetDescriptions.getRenderableBounds().getWidth(), 10) * 10 + 20);
		RenderSystem.enableDepthTest();
	}

	private IFormattableTextComponent getDescriptionForPlanet() {
		if (selected == null) {
			return new StringTextComponent("Terra Prime").setStyle(Style.EMPTY.withColor(Color.fromRgb(0xFF_2fc256)));
		}

		PlanetData planet = selected.getData();
		int color = planet.getColour().getRGB();

		planetDescriptions.title = new StringTextComponent("Information - \"" + planet.getName() + "\"");
		IFormattableTextComponent text = new StringTextComponent("Traits:\n")
				.setStyle(Style.EMPTY.withColor(Color.fromRgb(color)));
		for (PlanetTrait t : planet.getTraits()) {
			text.append("   > ").setStyle(Style.EMPTY.withColor(Color.fromRgb(color)));
			text.append(t.getName());
			text.append("\n");
		}
		text.append("\n");

		// Add Extra Data
		text.append(new StringTextComponent("Stats:\n").setStyle(Style.EMPTY.withColor(Color.fromRgb(color))));
		text.append("   > Pres: " + planet.getAtm() + "\n").setStyle(Style.EMPTY.withColor(Color.fromRgb(color)));
		text.append("   > Temp: " + planet.getTemp() + "\n").setStyle(Style.EMPTY.withColor(Color.fromRgb(color)));
		text.append("   > Dist: " + planet.getDist() + "\n").setStyle(Style.EMPTY.withColor(Color.fromRgb(color)));

		text.append("ID  " + ClientStarchartHolder.getStarchart().planets.getValues().indexOf(planet) + "\n");

		return text;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (getContainerBounds().contains((int) mouseX, (int) mouseY)) {
			planetDescriptions.mouseScrolled(mouseX, mouseY, delta);
		}
		return super.mouseScrolled(mouseX, mouseY, delta);
	}

	@Override
	public void mouseMoved(double pMouseX, double pMouseY) {
		if (getContainerBounds().contains((int) pMouseX, (int) pMouseY)) {
			planetDescriptions.mouseMoved(pMouseX, pMouseY);
		}
		super.mouseMoved(pMouseX, pMouseY);
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (getContainerBounds().contains((int) pMouseX, (int) pMouseY)) {
			planetDescriptions.mouseClicked(pMouseX, pMouseY, pButton);
		}
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		if (getContainerBounds().contains((int) pMouseX, (int) pMouseY)) {
			planetDescriptions.mouseReleased(pMouseX, pMouseY, pButton);
		}
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean isPauseScreen() { return false; }

}
