package com.cy4.machina.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.cy4.machina.api.client.gui.IBoundedGui;
import com.cy4.machina.client.gui.element.PlanetNodeElement;
import com.cy4.machina.client.util.Rectangle;
import com.cy4.machina.client.util.UIHelper;
import com.cy4.machina.starchart.Starchart;
import com.cy4.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StarchartScreen extends Screen implements IBoundedGui{

	public static final ResourceLocation SC_BG = new MachinaRL("textures/gui/starchart/starchart_bg.png");
	public static final ResourceLocation SC_RS = new MachinaRL("textures/gui/starchart/starchart_rs.png");

	Starchart sc;
	List<Vector2f> positions = new ArrayList<>();
	List<PlanetNodeElement> nodes = new ArrayList<>();

	public StarchartScreen(Starchart sc) {
		super(new TranslationTextComponent("machina.screen.starchart.title"));
		this.sc = sc;
	}

	@Override
	protected void init() {
		super.init();

		createStarSystem(50D);
		
		for(PlanetNodeElement ne : this.nodes) this.addWidget(ne);
	}

	private void createStarSystem(double radius) {

		this.positions.clear();
		this.nodes.clear();
		Vector2f centre = getCentre();

		for (int i = 1; i <= sc.planets.size(); i++) {
			double angle = (Math.PI * 2) / sc.planets.size() * i;

			float x = (float) (radius * Math.cos(angle));
			float y = (float) (radius * Math.sin(angle));

			this.positions.add(new Vector2f(centre.x - x, centre.y - y));
			this.nodes.add(new PlanetNodeElement(centre.x - x, centre.y - y, this));
		}
	}

	public Rectangle getContainerBounds() {
		Rectangle bounds = new Rectangle();
		bounds.x0 = (int) (width * 0.2);
		bounds.y0 = (int) (height * 0.2);
		bounds.x1 = (int) (width * 0.8);
		bounds.y1 = (int) (height * 0.8);
		return bounds;
	}

	@Override
	public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(matrixStack, pMouseX, pMouseY, pPartialTicks); // Buttons

		Rectangle bound = this.getContainerBounds();

		// Background
		UIHelper.renderOverflowHidden(matrixStack, this::renderContainerBackground, MatrixStack::toString);

		// Elements
		renderStarSystem(matrixStack, pMouseX, pMouseY, pPartialTicks);

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
		this.nodes.forEach(node -> node.render(matrixStack, mX, mY, pTicks));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
