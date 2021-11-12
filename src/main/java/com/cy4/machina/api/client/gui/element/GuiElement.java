package com.cy4.machina.api.client.gui.element;

import javax.annotation.Nonnull;

import com.cy4.machina.api.client.gui.IFontRenderer;
import com.cy4.machina.util.MachinaRL;
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
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
//		int i = this.getYImage(this.isHovered());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		this.blit(pMatrixStack, this.renderX, this.renderY, this.x, this.y, this.width / 2, this.height);
		this.blit(pMatrixStack, this.renderX, this.renderY, this.x, this.y, this.width / 2, this.height);
		this.renderBg(pMatrixStack, minecraft, pMouseX, pMouseY);
		int j = getFGColor();
		drawCenteredString(pMatrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2,
				this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
	}

	protected static ResourceLocation texturePath(String name) {
		return new MachinaRL("textures/gui/element/" + name);
	}

}
