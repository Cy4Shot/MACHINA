package com.machina.client.screen;

import org.lwjgl.glfw.GLFW;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.multiblock.MultiblockMasterTileEntity;
import com.machina.client.util.UIHelper;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class UnformedMultiblockScreen<T extends BaseContainer<? extends MultiblockMasterTileEntity>> extends Screen {

	private T menu;
	private float rot = 0f;

	public UnformedMultiblockScreen(T menu, ITextComponent title) {
		super(title);

		this.menu = menu;
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float par) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);

		// Render
		UIHelper.bindLarge();

		// Back
		int xSize = 236, ySize = 210;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		UIHelper.blit(stack, x, y, 0, 0, xSize, ySize);

		UIHelper.renderMultiblock(stack, this.menu.te.mb, x + xSize / 2, y + ySize / 2, 1, par, -rot);

		// Data
		UIHelper.drawCenteredStringWithBorder(stack,
				StringUtils.translateScreenComp("multiblock.unformed1")
						.append(menu.te.getName().setStyle(Style.EMPTY.withColor(Color.fromRgb(0xFF_fcba03))))
						.append(StringUtils.translateScreenComp("multiblock.unformed2")),
				x + 117, y + 14, 0xFF_ff0000, 0xFF_0e0e0e);

		// Footer
		UIHelper.drawStringWithBorder(stack, "MACHINA://404_NOT_FOUND/", x + 6, y + 195, 0xFF_00fefe, 0xFF_0e0e0e);
	}
	
	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_2) {
			this.rot -= (float) pDragX / (float) width * 180f;
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}
	
	@Override
	public void onClose() {
		this.minecraft.player.closeContainer();
		super.onClose();
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
