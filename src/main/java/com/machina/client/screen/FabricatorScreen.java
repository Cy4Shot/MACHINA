package com.machina.client.screen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.machina.block.container.FabricatorContainer;
import com.machina.client.screen.base.NoJeiContainerScreen;
import com.machina.client.util.UIHelper;
import com.machina.registration.init.ItemInit;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;

public class FabricatorScreen extends NoJeiContainerScreen<FabricatorContainer> {

	public FabricatorScreen(FabricatorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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
		super.render(stack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(stack, pMouseX, pMouseY);
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
	}

	@Override
	protected void renderBg(MatrixStack stack, float pPartialTicks, int pX, int pY) {

		// Back
		int xSize = 237, ySize = 201;
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		UIHelper.bindBlprt();
		UIHelper.blit(stack, x, y, 0, 72, xSize, ySize - 17);
		UIHelper.bindScifi();
		UIHelper.blit(stack, x + 32, y + 187, 3, 200, 174, 30);

		// Slots
		for (int sy = 0; sy < 4; sy++) {
			for (int sx = 0; sx < 4; sx++) {
				UIHelper.blit(stack, x + 20 + sx * 20, y + 60 + sy * 20, 228, 184, 19, 19);
			}
		}
		UIHelper.blit(stack, x + 50, y + 34, 228, 184, 19, 19);
		UIHelper.blit(stack, x + 50, y + 142, 228, 184, 19, 19);

		UIHelper.bindTrmnl();
		UIHelper.blit(stack, x + 107, y + 42, 0, 234, 118, 18);
		UIHelper.blit(stack, x + 107, y + 68, 138, 184, 118, 72);

		// Deco
		UIHelper.bindPrgrs();
		if (true) { // Craft Not Ready
			UIHelper.blit(stack, x + 24, y + 140, 29, 230, 19, 13);
			UIHelper.blit(stack, x + 75, y + 140, 78, 230, 19, 13);
			UIHelper.blit(stack, x + 45, y + 143, 0, 183, 29, 17);
			UIHelper.blit(stack, x + 50, y + 142, 238, 16, 18, 18);
		} else {
			UIHelper.blit(stack, x + 24, y + 140, 29, 243, 19, 13);
			UIHelper.blit(stack, x + 75, y + 140, 78, 243, 19, 13);
			UIHelper.blit(stack, x + 45, y + 143, 0, 239, 29, 17);
		}
		UIHelper.blit(stack, x + 115, y + 22, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 59, y + 23, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 121, y + 23, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 45, y + 35, 0, 239, 29, 17);
		UIHelper.blit(stack, x + 27, y + 41, 128, 240, 16, 16);
		UIHelper.blit(stack, x + 75, y + 41, 144, 240, 16, 16);
		UIHelper.blit(stack, x + 162, y + 60, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 107, y + 61, 88, 0, 55, 4);
		UIHelper.blit(stack, x + 168, y + 61, 88, 0, 55, 4);
		UIHelper.blit(stack, x + 110, y + 140, 0, 200, 112, 11);

		if (true) { // Has Blueprint
			if (true) { // Has Materials
				UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("fabricator.ready"), x + 165,
						y + 46, 0xFF_00fefe, 0xFF_0e0e0e);

				List<IReorderingProcessor> p1 = LanguageMap.getInstance().getVisualOrder(
						StringUtils.findOptimalLines(StringUtils.translateScreenComp("fabricator.prompt1"), 100));

				List<IReorderingProcessor> p2 = LanguageMap.getInstance().getVisualOrder(
						StringUtils.findOptimalLines(StringUtils.translateScreenComp("fabricator.prompt2"), 150));

				float scale = 0.7f;
				float iscale = 1f / scale;
				RenderSystem.scalef(scale, scale, scale);
				for (int k1 = 0; k1 < p1.size(); ++k1) {
					UIHelper.drawCenteredStringWithBorder(stack, p1.get(k1), (x + 165) * iscale,
							(y + 76) * iscale + k1 * 10, 0xFF00fefe, 0xFF_0e0e0e);
				}
				for (int k2 = 0; k2 < p2.size(); ++k2) {
					UIHelper.drawCenteredStringWithBorder(stack, p2.get(k2), (x + 165) * iscale,
							(y + 120) * iscale + k2 * 10, 0xFF_ff0000, 0xFF_0e0e0e);
				}
				RenderSystem.scalef(iscale, iscale, iscale);
				UIHelper.bindTrmnl();
				if (pX > x + 157 && pX < x + 157 + 16 && pY > y + 96 && pY < y + 96 + 16) {
					UIHelper.blit(stack, x + 157, y + 96, 237, 73, 17, 17);
					UIHelper.renderLabel(stack,
							Collections.singletonList(StringUtils.translateScreenComp("fabricator.craft")), pX, pY,
							0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
				} else {
					UIHelper.blit(stack, x + 157, y + 96, 237, 56, 17, 17);
				}
			} else {
				UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("fabricator.missing"), x + 165,
						y + 46, 0xFF_ff0000, 0xFF_0e0e0e);

				// 16 items
				List<ItemStack> missing = Arrays.asList(new ItemStack(Items.DIAMOND, 2),
						new ItemStack(ItemInit.AMMONIUM_NITRATE.get(), 5), new ItemStack(Items.DIAMOND, 2),
						new ItemStack(ItemInit.ALUMINUM_INGOT.get(), 5), new ItemStack(Items.ACACIA_BUTTON, 2),
						new ItemStack(ItemInit.CALCIUM_SULPHATE.get(), 5), new ItemStack(Items.ACACIA_STAIRS, 2),
						new ItemStack(ItemInit.HEXAMINE.get(), 5), new ItemStack(Items.BARRIER, 2),
						new ItemStack(ItemInit.IRON_CATALYST.get(), 5), new ItemStack(Items.ACACIA_WOOD, 2),
						new ItemStack(ItemInit.HDPE.get(), 5), new ItemStack(Items.EMERALD, 2),
						new ItemStack(ItemInit.SCANNER.get(), 5), new ItemStack(Items.DARK_OAK_BUTTON, 2),
						new ItemStack(ItemInit.NITRONIUM_TETRAFLUOROBORATE.get(), 5));

				float scale = 0.7f;
				float iscale = 1f / scale;
				RenderSystem.scalef(scale, scale, scale);
				int i = 0;
				for (ItemStack item : missing) {
					if (i == 8) {
						UIHelper.drawStringWithBorder(stack,
								StringUtils.translateScreen("fabricator.extra").replace("#",
										String.valueOf(missing.size() - i)),
								(x + 110) * iscale, (y + 71) * iscale + i * 10, 0xff0000, 0xFF_0e0e0e);
						break;
					}
					UIHelper.drawStringWithBorder(stack,
							StringUtils.toEllipsis(StringUtils.prettyItemStack(item), 20, 0), (x + 110) * iscale,
							(y + 71) * iscale + i * 10, 0xff0000, 0xFF_0e0e0e);
					i++;
				}
				RenderSystem.scalef(iscale, iscale, iscale);
			}
		} else {
			UIHelper.renderTintedItem(stack, ItemInit.BLUEPRINT.get().getDefaultInstance(), x + 51, y + 35, 35, 35, 35,
					0.8f);
			UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("fabricator.no_bp"), x + 165,
					y + 46, 0xFF_ff0000, 0xFF_0e0e0e);
		}

		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("fabricator.title"), x + xSize / 2,
				y + 10, 0xFF_00fefe, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "MACHINA://FABRICATION_BENCH/", x + 8, y + 170, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseReleased(double pX, double pY, int pB) {
		return super.mouseReleased(pX, pY, pB);
	}

	@Override
	public int getXSize() {
		return 176;
	}
}
