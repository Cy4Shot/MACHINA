package com.machina.client.screen;

import java.util.LinkedHashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.machina.block.container.BlueprinterContainer;
import com.machina.blueprint.Blueprint;
import com.machina.blueprint.Blueprint.BlueprintCategory;
import com.machina.client.ClientResearch;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class BlueprinterScreen extends NoJeiContainerScreen<BlueprinterContainer> {

	private int tab = -1;

	public BlueprinterScreen(BlueprinterContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - 176) / 2;
	}

	@Override
	public int getSlotColor(int index) {
		if (index < this.menu.te.getContainerSize())
			return 0x00_FFFFFF;
		else
			return super.getSlotColor(index);
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);

		// Render
		super.render(stack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(stack, pMouseX, pMouseY);
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
	}

	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {
		UIHelper.bindScifi();

		// Back
		int xSize = 237, ySize = 201;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		UIHelper.blit(stack, x + 32, y + 187, 3, 200, 174, 30);
		UIHelper.bindBlprt();
		UIHelper.blit(stack, x, y, 0, 72, xSize, ySize - 17);
		if (pX > x + 215 && pX < x + 215 + 17 && pY > y + 4 && pY < y + 4 + 17) {
			UIHelper.blit(stack, x + 215, y + 4, 239, 239, 17, 17);
		} else {
			UIHelper.blit(stack, x + 215, y + 4, 239, 222, 17, 17);
		}

		// Tabs
		int tabs = BlueprintCategory.values().length;
		for (int i = 0; i < tabs; i++) {
			boolean s = tab == i;
			UIHelper.blit(stack, x + i * 22, y - 18, 236, s ? 20 : 0, 20, 20);
			if (ClientResearch.getResearch().categoryUnlocked(BlueprintCategory.values()[i])) {
				boolean h = pX > x + i * 22 - 1 && pX < x + i * 22 + 20 && pY > y - 19 && pY < y;
				UIHelper.blit(stack, x + i * 22 + 2, y - 16, i * 16, s ? 0 : (h ? 32 : 16), 16, 16);
			} else {
				UIHelper.blit(stack, x + i * 22 + 2, y - 16, 240, 192, 16, 16);
			}
		}

		// Res
		if (tab >= 0 && tab < tabs) {
			int bpx = 0;
			int bpy = 0;
			int bx = x + 8;
			int by = y + 32;
			LinkedHashMap<Blueprint, Boolean> bps = ClientResearch.getResearch()
					.getCategory(BlueprintCategory.values()[tab]);
			for (Map.Entry<Blueprint, Boolean> e : bps.entrySet()) {
				if (e.getValue()) {
					UIHelper.blit(stack, bx + bpx * 20, by + bpy * 20, 237, 72, 19, 19);
					if (pX > bx - 1 + bpx * 20 && pX < bx + 18 + bpx * 20 && pY > by - 1 + bpy * 20
							&& pY < by + 18 + bpy * 20) {
						UIHelper.blit(stack, bx + bpx * 20, by + bpy * 20, 237, 110, 18, 18);
					}
					UIHelper.renderItem(e.getKey().getItem(), bx + 1 + bpx * 20, by + 1 + bpy * 20);
					UIHelper.bindBlprt();
				} else {
					UIHelper.blit(stack, bx + bpx * 20, by + bpy * 20, 237, 91, 19, 19);
					UIHelper.blit(stack, bx + 1 + bpx * 20, by + 1 + bpy * 20, 240, 192, 16, 16);
				}
				bpx++;
				if (bpx >= 6) {
					bpx = 0;
					bpy++;
				}
			}
			UIHelper.bindPrgrs();
			UIHelper.blit(stack, bx + 56, by - 12, 88, 4, 6, 6);
			UIHelper.blit(stack, bx, by - 11, 88, 0, 56, 4);
			UIHelper.blit(stack, bx + 62, by - 11, 88, 0, 56, 4);
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("blueprint.tab" + tab), bx + 60,
					by - 24, 0xFF_00fefe, 0xFF_0e0e0e);
		}

		// Text
		UIHelper.drawStringWithBorder(stack, "MACHINA://BLUEPRINTER_TABLE/", x + 8, y + 170, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pB) {
		if (pB == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			int xSize = 237, ySize = 201;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			if (pX > x + 215 && pX < x + 215 + 17 && pY > y + 4 && pY < y + 4 + 17) {
				this.onClose();
				UIHelper.click();
				return true;
			}

			int tabs = BlueprintCategory.values().length;
			for (int i = 0; i < tabs; i++) {
				if (tab != i && ClientResearch.getResearch().categoryUnlocked(BlueprintCategory.values()[i])) {
					if (pX > x + i * 22 - 1 && pX < x + i * 22 + 20 && pY > y - 19 && pY < y) {
						tab = i;
						UIHelper.click();
						return true;
					}
				}
			}
		}

		return super.mouseReleased(pX, pY, pB);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
