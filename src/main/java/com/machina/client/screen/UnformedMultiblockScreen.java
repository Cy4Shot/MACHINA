package com.machina.client.screen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.multiblock.MultiblockMasterTileEntity;
import com.machina.client.util.UIHelper;
import com.machina.multiblock.ClientMultiblock;
import com.machina.multiblock.Multiblock;
import com.machina.util.math.ArrayUtil;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class UnformedMultiblockScreen<T extends BaseContainer<? extends MultiblockMasterTileEntity>> extends Screen {

	private T menu;
	private float rotX = 0f;
	private float rotY = 0f;
	private ClientMultiblock mb;
	private Optional<Integer> layer = Optional.empty();
	private List<Pair<Long, List<BlockState>>> blocks;
	private int textWidth;
	private boolean infoWindow = false;

	public UnformedMultiblockScreen(T menu, ITextComponent title) {
		super(title);

		this.menu = menu;
	}

	@Override
	protected void init() {
		super.init();
		Multiblock m = menu.te.mb;
		this.blocks = ArrayUtil.flatten(m.structure).map(o -> (String) o)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
				.map(e -> new Pair<>(e.getValue() - (e.getKey().equals(m.controller_replacable) ? 1 : 0),
						m.map.get(e.getKey())))
				.collect(Collectors.toList());
		this.blocks.add(new Pair<>(1L, Collections.singletonList(m.controller)));
		this.blocks = this.blocks.stream()
				.map(f -> new Pair<>(this.blocks.stream().filter(e -> e.getSecond().equals(f.getSecond()))
						.map(a -> a.getFirst()).reduce(0L, Long::sum), f.getSecond()))
				.filter(ArrayUtil.distinctByKey(e -> e.getFirst())).collect(Collectors.toList());
		this.mb = new ClientMultiblock(m);
		this.textWidth = this.blocks.stream().mapToInt(e -> UIHelper.getWidth(e.getFirst().toString() + "x")).max()
				.getAsInt();
	}

	@Override
	public void render(MatrixStack stack, int pX, int pY, float par) {
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

		// Multiblock
		UIHelper.renderMultiblock(stack, this.mb, x + xSize / 2, y + ySize / 2 + 5, 1, par, -rotY, -rotX,
				layer.isPresent() ? pos -> pos.getY() != layer.get() - 1 : pos -> false);

		// Controls
		UIHelper.bindStcht();
		UIHelper.betterBlit(stack, x + 8, y + 170, 16, 112, 16, 16, 128);
		UIHelper.drawStringWithBorder(stack, StringUtils.translateScreen("multiblock.rot"), x + 29, y + 174, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.bindScifi();
		UIHelper.blit(stack, x + 196, y + 168, 228, 184, 19, 19);

		if (pX > x + 217 && pX < x + 217 + 7 && pY > y + 172 && pY < y + 172 + 12) {
			UIHelper.blit(stack, x + 217, y + 172, 208, 196, 7, 12);
		} else {
			UIHelper.blit(stack, x + 217, y + 172, 208, 184, 7, 12);
		}
		if (pX > x + 186 && pX < x + 186 + 8 && pY > y + 172 && pY < y + 172 + 12) {
			UIHelper.blit(stack, x + 186, y + 172, 216, 196, 8, 12);
		} else {
			UIHelper.blit(stack, x + 186, y + 172, 216, 184, 8, 12);
		}

		UIHelper.drawCenteredStringWithBorder(stack, layer.isPresent() ? layer.get().toString() : "A", x + 205, y + 173,
				0xFF_00fefe, 0xFF_0e0e0e);

		// Title Deco
		UIHelper.bindPrgrs();
		UIHelper.blit(stack, x + 115, y + 22, 88, 4, 6, 6);
		UIHelper.blit(stack, x + 59, y + 23, 88, 0, 56, 4);
		UIHelper.blit(stack, x + 121, y + 23, 88, 0, 56, 4);

		// Block List
		if (this.infoWindow) {
			UIHelper.bindScifi();
			UIHelper.blit(stack, x + xSize + 4, y, 152, 103, 87, 81);

			for (int i = 0; i < this.blocks.size(); i++) {
				Pair<Long, List<BlockState>> entry = this.blocks.get(i);
				String text = entry.getFirst().toString() + "x";
				UIHelper.drawStringWithBorder(stack, text, x + 255 + textWidth - UIHelper.getWidth(text),
						y + 12 + i * 16, 0xFF_00fefe, 0xFF_0e0e0e);
				for (int j = 0; j < entry.getSecond().size(); j++) {
					int xp = x + textWidth + 257 + j * 16;
					int yp = y + 8 + i * 16;
					ItemStack st = new ItemStack(entry.getSecond().get(j).getBlock());
					UIHelper.renderItem(st, xp, yp);
					if (pX > xp && pX < xp + 16 && pY > yp && pY < yp + 16) {
						UIHelper.renderTintedItem(stack, st, xp, yp, 255, 255, 255, 0.2f);
						UIHelper.renderLabel(stack, Arrays.asList(st.getHoverName()), pX, pY, 0xFF_232323, 0xFF_00fefe,
								0xFF_1bcccc);
					}
				}
			}

			UIHelper.bindTrmnl();
			if (pX > x + 214 && pX < x + 214 + 17 && pY > y + 5 && pY < y + 5 + 17) {
				UIHelper.blit(stack, x + 214, y + 5, 237, 17, 17, 17);
				UIHelper.renderLabel(stack, Arrays.asList(StringUtils.translateScreenComp("multiblock.closeinfo")), pX,
						pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			} else {
				UIHelper.blit(stack, x + 214, y + 5, 237, 0, 17, 17);
			}
		} else {
			UIHelper.bindTrmnl();
			if (pX > x + 214 && pX < x + 214 + 17 && pY > y + 5 && pY < y + 5 + 17) {
				UIHelper.blit(stack, x + 214, y + 5, 121, 219, 17, 17);
				UIHelper.renderLabel(stack, Arrays.asList(StringUtils.translateScreenComp("multiblock.openinfo")), pX,
						pY, 0xFF_232323, 0xFF_00fefe, 0xFF_1bcccc);
			} else {
				UIHelper.blit(stack, x + 214, y + 5, 121, 202, 17, 17);
			}
		}

		// Text
		UIHelper.drawCenteredStringWithBorder(stack, menu.te.getName(), x + xSize / 2, y + 10, 0xFF_00fefe,
				0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack,
				StringUtils.translateScreenComp("multiblock.unformed1")
						.append(menu.te.getName().setStyle(Style.EMPTY.withColor(Color.fromRgb(0xFF_fcba03))))
						.append(StringUtils.translateScreenComp("multiblock.unformed2")),
				x + 117, y + 34, 0xFF_ff0000, 0xFF_0e0e0e);

		// Footer
		UIHelper.drawStringWithBorder(stack, "MACHINA://404_NOT_FOUND/", x + 6, y + 195, 0xFF_00fefe, 0xFF_0e0e0e);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_2) {
			this.rotX -= (float) pDragY / (float) height * 80f;
			this.rotY -= (float) pDragX / (float) width * 180f;

			this.rotX = MathHelper.clamp(this.rotX, 0f, 60f);
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseClicked(double pX, double pY, int pButton) {
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_1) {
			int xSize = 236, ySize = 210;
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;

			if (pX > x + 217 && pX < x + 217 + 7 && pY > y + 172 && pY < y + 172 + 12) {
				if (layer.isPresent()) {
					if (layer.get() < mb.mb.size.getY()) {
						layer = Optional.of(layer.get() + 1);
					} else {
						layer = Optional.empty();
					}
				} else {
					layer = Optional.of(1);
				}
				UIHelper.click();
			}
			if (pX > x + 186 && pX < x + 186 + 8 && pY > y + 172 && pY < y + 172 + 12) {
				if (layer.isPresent()) {
					if (layer.get() > 1) {
						layer = Optional.of(layer.get() - 1);
					} else {
						layer = Optional.empty();
					}
				} else {
					layer = Optional.of(mb.mb.size.getY());
				}
				UIHelper.click();
			}
			if (pX > x + 214 && pX < x + 214 + 17 && pY > y + 5 && pY < y + 5 + 17) {
				this.infoWindow = !infoWindow;
				UIHelper.click();
			}
		}
		return super.mouseClicked(pX, pY, pButton);
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
