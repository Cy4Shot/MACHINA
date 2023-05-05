package com.machina.client.screen;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.machina.block.container.BlueprinterContainer;
import com.machina.blueprint.Blueprint;
import com.machina.blueprint.Blueprint.BlueprintCategory;
import com.machina.client.ClientResearch;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SEtchBlueprint;
import com.machina.registration.init.KeyBindingsInit;
import com.machina.util.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;

public class BlueprinterScreen extends NoJeiContainerScreen<BlueprinterContainer> {

	private int tab = -1;
	private Blueprint selected = null;

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
		return 0x44_FFFFFF;
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);

		// Darker background
		this.renderBackground(stack);
		this.renderBackground(stack);

		// Render
		if (tab >= 0 && tab < BlueprintCategory.values().length) {
			super.render(stack, pMouseX, pMouseY, pPartialTicks);
			this.renderTooltip(stack, pMouseX, pMouseY);
		} else {
			this.renderBg(stack, pPartialTicks, pMouseX, pMouseY);
		}
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
					if ((this.selected != null && this.selected.getId().equals(e.getKey().getId()))
							|| (pX > bx - 1 + bpx * 20 && pX < bx + 18 + bpx * 20 && pY > by - 1 + bpy * 20
									&& pY < by + 18 + bpy * 20)) {
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

			// Craft
			UIHelper.bindPrgrs();
			UIHelper.blit(stack, x + 137, y + 24, 163, 175, 93, 81);

			UIHelper.bindScifi();
			UIHelper.blit(stack, x + 148, y + 49, 228, 184, 19, 19);
			UIHelper.blit(stack, x + 195, y + 49, 228, 184, 19, 19);

			UIHelper.bindPrgrs();
			UIHelper.blit(stack, x + 143, y + 50, 0, 239, 29, 17);
			UIHelper.blit(stack, x + 190, y + 50, 0, 239, 29, 17);
			UIHelper.blit(stack, x + 178, y + 53, 121, 246, 6, 10);

			if (this.menu.te.getItem(0).isEmpty()) {
				UIHelper.bindBlprt();
				UIHelper.blit(stack, x + 172, y + 80, 237, 166, 17, 17);
				RenderSystem.scalef(0.8f, 0.8f, 0.8f);
				UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("blueprint.missing"),
						(x + 180) * 1.25f, (y + 36) * 1.25f, 0xFF_ff0000, 0xFF_0e0e0e);
				RenderSystem.scalef(1.25f, 1.25f, 1.25f);
			} else {
				if (this.selected == null) {
					UIHelper.bindBlprt();
					UIHelper.blit(stack, x + 172, y + 80, 237, 166, 17, 17);
					RenderSystem.scalef(0.8f, 0.8f, 0.8f);
					UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("blueprint.unselected"),
							(x + 180) * 1.25f, (y + 36) * 1.25f, 0xFF_ff0000, 0xFF_0e0e0e);
					RenderSystem.scalef(1.25f, 1.25f, 1.25f);
				} else {
					if (!this.menu.te.getItem(1).isEmpty()) {
						UIHelper.bindBlprt();
						UIHelper.blit(stack, x + 172, y + 80, 237, 166, 17, 17);
						RenderSystem.scalef(0.8f, 0.8f, 0.8f);
						UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("blueprint.full"),
								(x + 180) * 1.25f, (y + 36) * 1.25f, 0xFF_ff0000, 0xFF_0e0e0e);
						RenderSystem.scalef(1.25f, 1.25f, 1.25f);
					} else {
						UIHelper.bindTrmnl();
						if (pX > x + 172 && pX < x + 172 + 16 && pY > y + 80 && pY < y + 80 + 16) {
							UIHelper.blit(stack, x + 172, y + 80, 237, 73, 17, 17);
							UIHelper.renderLabel(stack,
									Arrays.asList(StringUtils.translateCompScreen("blueprint.etch")), pX, pY,
									0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
							UIHelper.bindTrmnl();
						} else {
							UIHelper.blit(stack, x + 172, y + 80, 237, 56, 17, 17);
						}
						RenderSystem.scalef(0.8f, 0.8f, 0.8f);
						UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("blueprint.ready"),
								(x + 180) * 1.25f, (y + 36) * 1.25f, 0xFF_00fefe, 0xFF_0e0e0e);
						RenderSystem.scalef(1.25f, 1.25f, 1.25f);
					}
				}
			}

			if (this.selected != null && this.menu.te.getItem(1).isEmpty()) {
				UIHelper.renderTintedItem(stack, this.selected.getItem(), x + 196, y + 50, 35, 35, 35, 0.7f);
			}

			float inv = 1 / 0.7f;
			List<IReorderingProcessor> desc = LanguageMap.getInstance().getVisualOrder(
					StringUtils.findOptimalLines(StringUtils.translateCompScreen("blueprint.crafttip"), 270));
			RenderSystem.scalef(0.7f, 0.7f, 0.7f);
			for (int k1 = 0; k1 < desc.size(); ++k1) {
				UIHelper.drawCenteredStringWithBorder(stack, desc.get(k1), (x + 118) * inv, (y + 120 + k1 * 9) * inv,
						0xFF_00fefe, 0xFF_0e0e0e);
			}
			RenderSystem.scalef(inv, inv, inv); // lol
		} else {
			float inv = 1 / 0.7f;
			List<IReorderingProcessor> desc = LanguageMap.getInstance().getVisualOrder(
					StringUtils.findOptimalLines(StringUtils.toComp(StringUtils.translateScreen("blueprint.tabtip1")
							+ KeyBindingsInit.STARCHART.getKey().getDisplayName().getString().toUpperCase()
							+ StringUtils.translateScreen("blueprint.tabtip2")), 270));
			RenderSystem.scalef(0.7f, 0.7f, 0.7f);
			for (int k1 = 0; k1 < desc.size(); ++k1) {
				UIHelper.drawCenteredStringWithBorder(stack, desc.get(k1), (x + 118) * inv, (y + 74 + k1 * 9) * inv,
						0xFF_00fefe, 0xFF_0e0e0e);
			}
			RenderSystem.scalef(inv, inv, inv); // lol
		}

		UIHelper.drawStringWithBorder(stack, "MACHINA://BLUEPRINTER_TABLE/", x + 8, y + 170, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pB) {
		int tabs = BlueprintCategory.values().length;
		if (pB == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			int xSize = 237, ySize = 201;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			if (pX > x + 215 && pX < x + 215 + 17 && pY > y + 4 && pY < y + 4 + 17) {
				this.onClose();
				UIHelper.click();
				return true;
			}

			for (int i = 0; i < tabs; i++) {
				if (tab != i && ClientResearch.getResearch().categoryUnlocked(BlueprintCategory.values()[i])) {
					if (pX > x + i * 22 - 1 && pX < x + i * 22 + 20 && pY > y - 19 && pY < y) {
						tab = i;
						UIHelper.click();
						return true;
					}
				}
			}

			if (tab >= 0 && tab < tabs) {
				if (!this.menu.te.getItem(0).isEmpty() && this.selected != null && pX > x + 172 && pX < x + 172 + 16
						&& pY > y + 80 && pY < y + 80 + 16 && this.menu.te.getItem(1).isEmpty()) {
					MachinaNetwork.CHANNEL
							.sendToServer(new C2SEtchBlueprint(this.menu.te.getBlockPos(), this.selected.getId()));
					UIHelper.click();
					return true;
				}
				int bpx = 0;
				int bpy = 0;
				int bx = x + 8;
				int by = y + 32;
				LinkedHashMap<Blueprint, Boolean> bps = ClientResearch.getResearch()
						.getCategory(BlueprintCategory.values()[tab]);
				for (Map.Entry<Blueprint, Boolean> e : bps.entrySet()) {
					if (e.getValue()) {
						if (pX > bx - 1 + bpx * 20 && pX < bx + 18 + bpx * 20 && pY > by - 1 + bpy * 20
								&& pY < by + 18 + bpy * 20) {
							this.selected = e.getKey();
							UIHelper.click();
							return true;
						}
					}
					bpx++;
					if (bpx >= 6) {
						bpx = 0;
						bpy++;
					}
				}

			}
		}

		if (tab >= 0 && tab < tabs)
			return super.mouseReleased(pX, pY, pB);
		else
			return true;
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (tab >= 0 && tab < BlueprintCategory.values().length)
			return super.mouseClicked(pMouseX, pMouseY, pButton);
		else
			return true;
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
