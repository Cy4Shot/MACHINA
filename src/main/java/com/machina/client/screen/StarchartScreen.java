package com.machina.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.machina.client.ClientStarchart;
import com.machina.client.screen.element.PlanetNodeElement;
import com.machina.client.util.IBoundedGui;
import com.machina.client.util.Rectangle;
import com.machina.client.util.UIHelper;
import com.machina.registration.init.PlanetAttributeTypesInit;
import com.machina.util.MachinaRL;
import com.machina.world.data.PlanetData;
import com.mojang.blaze3d.matrix.MatrixStack;

import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StarchartScreen extends Screen implements IBoundedGui {

	public static final ResourceLocation SC_BG = new MachinaRL("textures/gui/starchart/starchart_bg.png");
	public static final ResourceLocation SC_RS = new MachinaRL("textures/gui/starchart/starchart_rs.png");

	List<Vector2f> positions = new ArrayList<>();
	List<PlanetNodeElement> nodes = new ArrayList<>();

	public PlanetNodeElement selected = null;

	public StarchartScreen() {
		super(new TranslationTextComponent("machina.screen.starchart.title"));
	}

	@Override
	protected void init() {
		super.init();

		createStarSystem(val -> 20 * val + 10);

		for (PlanetNodeElement ne : nodes) {
			this.addWidget(ne);
		}
	}

	public Vector2f getNewCentre() {
		Vector2f centre = getCentre();
		return new Vector2f(centre.x, centre.y);
	}

	private void createStarSystem(Float2DoubleFunction func) {

		positions.clear();
		nodes.clear();
		Vector2f centre = getNewCentre();

		for (int i = 1; i <= ClientStarchart.getStarchart().size(); i++) {
			if (ClientStarchart.getPlanetData(i - 1).equals(PlanetData.NONE))
				continue;

			double angle = (Math.PI * 2) / ClientStarchart.getStarchart().size() * i;

			double r = func.apply(ClientStarchart.getPlanetData(i - 1).getAttribute(PlanetAttributeTypesInit.DISTANCE));

			float x = (float) (r * Math.cos(angle));
			float y = (float) (r * Math.sin(angle));

			positions.add(new Vector2f(centre.x - x, centre.y - y));
			nodes.add(new PlanetNodeElement(centre.x - x, centre.y - y, this, ClientStarchart.getPlanetData(i - 1)));
		}
	}

	@Override
	public Rectangle getContainerBounds() {
		return new Rectangle(0, 0, this.width, this.height);
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
		minecraft.getTextureManager().bind(SC_RS);
		UIHelper.blit(matrixStack, this.width / 2 - 32, this.height / 2 - 32, 0, 32, 64, 64);

		// Elements
		renderStarSystem(matrixStack, pMouseX, pMouseY, pPartialTicks);

		// Border
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

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
