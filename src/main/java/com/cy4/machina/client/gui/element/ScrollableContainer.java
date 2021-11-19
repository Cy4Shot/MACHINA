package com.cy4.machina.client.gui.element;

import com.cy4.machina.api.client.gui.Renderable;
import com.cy4.machina.client.util.Rectangle;
import com.cy4.machina.client.util.UIHelper;
import com.cy4.machina.util.MachinaRL;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class ScrollableContainer extends AbstractGui {

	public static final int SCROLL_WIDTH = 8;

	public static final ResourceLocation UI_RESOURCE = new MachinaRL("textures/gui/elements.png");
	protected Rectangle bounds;
	protected Renderable renderer;
	public ITextComponent title;
	protected int innerHeight;
	protected int yOffset;
	protected boolean scrolling;
	protected double scrollingStartY;
	protected int scrollingOffsetY;

	public ScrollableContainer(Renderable renderer, ITextComponent title) {
		this.renderer = renderer;
		this.title = title;
	}

	public int getyOffset() {
		return yOffset;
	}

	public float scrollPercentage() {
		Rectangle scrollBounds = getScrollBounds();
		return (float) yOffset / (innerHeight - scrollBounds.getHeight());
	}

	public void setInnerHeight(int innerHeight) {
		this.innerHeight = innerHeight;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Rectangle getRenderableBounds() {
		Rectangle renderableBounds = new Rectangle(bounds);
		int margin = 2;
		renderableBounds.x1 -= SCROLL_WIDTH + margin;
		return renderableBounds;
	}

	public Rectangle getScrollBounds() {
		Rectangle scrollBounds = new Rectangle(bounds);
		scrollBounds.x0 = scrollBounds.x1 - SCROLL_WIDTH;
		return scrollBounds;
	}

	public void mouseMoved(double mouseX, double mouseY) {
		if (scrolling) {
			double deltaY = mouseY - scrollingStartY;
			Rectangle renderableBounds = getRenderableBounds();
			Rectangle scrollBounds = getScrollBounds();
			double deltaOffset = deltaY * innerHeight / scrollBounds.getHeight();

			yOffset = MathHelper.clamp(scrollingOffsetY + (int) (deltaOffset * innerHeight / scrollBounds.getHeight()),
					0, innerHeight - renderableBounds.getHeight() + 2);
		}
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {
		Rectangle renderableBounds = getRenderableBounds();
		Rectangle scrollBounds = getScrollBounds();

		float viewportRatio = (float) renderableBounds.getHeight() / innerHeight;

		if (viewportRatio < 1 && scrollBounds.contains((int) mouseX, (int) mouseY)) {
			scrolling = true;
			scrollingStartY = mouseY;
			scrollingOffsetY = yOffset;
		}
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		scrolling = false;
	}

	public void mouseScrolled(double mouseX, double mouseY, double delta) {
		Rectangle renderableBounds = getRenderableBounds();
		float viewportRatio = (float) renderableBounds.getHeight() / innerHeight;

		if (viewportRatio < 1) {
			yOffset = MathHelper.clamp(yOffset + (int) (-delta * 5), 0, innerHeight - renderableBounds.getHeight() + 2);
		}
	}

	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		TextureManager textureManager = Minecraft.getInstance().getTextureManager();

		Rectangle renderableBounds = getRenderableBounds();
		Rectangle scrollBounds = getScrollBounds();

		textureManager.bind(UI_RESOURCE);
		UIHelper.renderContainerBorder(matrixStack, renderableBounds);

		UIHelper.drawStringWithBorder(matrixStack, title.getString(), bounds.x0, bounds.y0 - 12, 0xFF_cc00ff,
				0xFF_0e0e0e);

		UIHelper.renderOverflowHidden(matrixStack, ms -> fill(ms, renderableBounds.x0, renderableBounds.y0,
				renderableBounds.x1, renderableBounds.y1, 0xFF_8B8B8B), ms -> {
					ms.pushPose();
					ms.translate(renderableBounds.x0 + 1, renderableBounds.y0 - yOffset + 1, 0);
					renderer.render(matrixStack, mouseX, mouseY, partialTicks);
					ms.popPose();
				});

		textureManager.bind(UI_RESOURCE);
		matrixStack.pushPose();
		matrixStack.translate(scrollBounds.x0 + 2, scrollBounds.y0, 0);
		matrixStack.scale(1, scrollBounds.getHeight(), 1);
		blit(matrixStack, 0, 0, 1, 146, 6, 1);
		matrixStack.popPose();
		blit(matrixStack, scrollBounds.x0 + 2, scrollBounds.y0, 1, 145, 6, 1);
		blit(matrixStack, scrollBounds.x0 + 2, scrollBounds.y1 - 1, 1, 251, 6, 1);

		float scrollPercentage = scrollPercentage();
		float viewportRatio = (float) renderableBounds.getHeight() / innerHeight;
		int scrollHeight = (int) (renderableBounds.getHeight() * viewportRatio);

		if (viewportRatio <= 1) {
			int scrollU = scrolling ? 28 : scrollBounds.contains(mouseX, mouseY) ? 18 : 8;

			matrixStack.pushPose();
			matrixStack.translate(0, (scrollBounds.getHeight() - scrollHeight) * scrollPercentage, 0);
			blit(matrixStack, scrollBounds.x0 + 1, scrollBounds.y0, scrollU, 104, 8, scrollHeight);
			blit(matrixStack, scrollBounds.x0 + 1, scrollBounds.y0 - 2, scrollU, 101, 8, 2);
			blit(matrixStack, scrollBounds.x0 + 1, scrollBounds.y0 + scrollHeight, scrollU, 253, 8, 2);
			matrixStack.popPose();
		}
	}
}
