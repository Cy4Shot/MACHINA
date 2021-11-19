package com.cy4.machina.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.cy4.machina.api.client.gui.IBoundedGui;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.client.gui.element.PlanetNodeElement;
import com.cy4.machina.client.gui.element.ScrollableContainer;
import com.cy4.machina.client.util.Rectangle;
import com.cy4.machina.client.util.UIHelper;
import com.cy4.machina.starchart.PlanetData;
import com.cy4.machina.starchart.Starchart;
import com.cy4.machina.util.MachinaRL;
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

	Starchart sc;
	List<Vector2f> positions = new ArrayList<>();
	List<PlanetNodeElement> nodes = new ArrayList<>();
	private ScrollableContainer planetDescriptions;

	public PlanetNodeElement selected = null;

	public StarchartScreen(Starchart sc) {
		super(new TranslationTextComponent("machina.screen.starchart.title"));
		this.sc = sc;

		this.planetDescriptions = new ScrollableContainer(this::renderDescription,
				new StringTextComponent("Planet Statistics"));
	}

	@Override
	protected void init() {
		super.init();

		createStarSystem(40D, 10D);

		for (PlanetNodeElement ne : this.nodes)
			this.addWidget(ne);
	}

	public Vector2f getNewCentre() {
		Vector2f centre = getCentre();
		return new Vector2f(centre.x - 70, centre.y);
	}

	private void createStarSystem(double variance, double min) {

		this.positions.clear();
		this.nodes.clear();
		Vector2f centre = getNewCentre();

		for (int i = 1; i <= sc.planets.size(); i++) {
			double angle = (Math.PI * 2) / sc.planets.size() * i;
			
			double r = sc.planets.get(i - 1).dist * variance + min;

			float x = (float) (r * Math.cos(angle));
			float y = (float) (r * Math.sin(angle));

			this.positions.add(new Vector2f(centre.x - x, centre.y - y));
			this.nodes.add(new PlanetNodeElement(centre.x - x, centre.y - y, this, sc.planets.get(i - 1)));
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
		this.nodes.forEach(node -> node.render(matrixStack, mX, mY, pTicks));
	}

	private void renderDescription(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		planetDescriptions.setInnerHeight(UIHelper.renderWrappedText(matrixStack,
				getDescriptionForPlanet(),
				planetDescriptions.getRenderableBounds().getWidth(), 10) * 10 + 20);
		RenderSystem.enableDepthTest();
	}
	
	private IFormattableTextComponent getDescriptionForPlanet() {
		if (selected == null) {
			return new StringTextComponent("Terra Prime").setStyle(Style.EMPTY.withColor(Color.fromRgb(0xFF_2fc256)));
		}
		PlanetData planet = selected.getData();
		int color = planet.color;
		
		planetDescriptions.title = new StringTextComponent("Information - \"" + planet.name + "\"");
		IFormattableTextComponent text = new StringTextComponent("Traits:\n").setStyle(Style.EMPTY.withColor(Color.fromRgb(color)));
		for (PlanetTrait t : planet.traits) {
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
		
		return text;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (getContainerBounds().contains((int) mouseX, (int) mouseY))
			planetDescriptions.mouseScrolled(mouseX, mouseY, delta);
		return super.mouseScrolled(mouseX, mouseY, delta);
	}

	@Override
	public void mouseMoved(double pMouseX, double pMouseY) {
		if (getContainerBounds().contains((int) pMouseX, (int) pMouseY))
			this.planetDescriptions.mouseMoved(pMouseX, pMouseY);
		super.mouseMoved(pMouseX, pMouseY);
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (getContainerBounds().contains((int) pMouseX, (int) pMouseY))
			this.planetDescriptions.mouseClicked(pMouseX, pMouseY, pButton);
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		if (getContainerBounds().contains((int) pMouseX, (int) pMouseY))
			this.planetDescriptions.mouseReleased(pMouseX, pMouseY, pButton);
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
