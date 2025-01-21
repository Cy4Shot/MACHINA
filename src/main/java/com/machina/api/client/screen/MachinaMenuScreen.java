package com.machina.api.client.screen;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.machina.Machina;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.cap.sided.ISideAdapter;
import com.machina.api.cap.sided.Side;
import com.machina.api.multiblock.ClientMultiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.StringUtils;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.VecUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

public abstract class MachinaMenuScreen<R extends MachinaBlockEntity, T extends MachinaContainerMenu<R>>
		extends AbstractContainerScreen<T> {

	private static final Minecraft mc = Minecraft.getInstance();

	protected static final ResourceLocation COMMON_UI = new MachinaRL("textures/gui/common_ui.png");
	protected static final ResourceLocation BG_OVERLAY = new MachinaRL("textures/gui/bg_overlay.png");

	protected final R entity;

	protected long aliveTicks = 0;
	private Float lsx, lsy = null;
	private float rotX, rotY;

	private Map<String, ClickArea> clickareas = new HashMap<>();
	private Map<String, Clickable> clickables = new HashMap<>();
	private Map<String, Hoverable> hoverables = new HashMap<>();
	private Map<String, Stateable> stateables = new HashMap<>();
	private Map<Direction, TextureAtlasSprite> sprites = new HashMap<>();

	public MachinaMenuScreen(T menu, Inventory inv, Component title) {
		super(menu, inv, title);

		this.entity = menu.be;

		this.imageWidth = 235;
		this.imageHeight = 100;

		BlockState state = this.menu.getDefaultState();
		RandomSource rand = RandomSource.create();
		BakedModel model = mc.getBlockRenderer().getBlockModel(state);
		for (Direction d : Direction.values()) {
			sprites.put(d, model.getQuads(state, d, rand, ModelData.EMPTY, null).get(0).getSprite());
		}
	}

	@Override
	protected void init() {
		this.clickareas.clear();
		this.clickables.clear();
		this.hoverables.clear();
		super.init();
	}

	public void render(GuiGraphics gui, int mx, int my, float pt) {
		this.renderBackground(gui);
		if (appearDraw(this.aliveTicks))
			super.render(gui, mx, my, pt);
		else
			this.renderBg(gui, pt, mx, my);
		this.renderTooltip(gui, mx, my);
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int x, int y) {
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		this.aliveTicks++;
	}

	protected int midWidth() {
		return (this.width - this.imageWidth) / 2;
	}

	protected int midHeight() {
		return (this.height - this.imageHeight) / 2;
	}

	@Override
	protected boolean hasClickedOutside(double mx, double my, int top, int left, int button) {
		for (ClickArea area : clickareas.values()) {
			if (area.enabled().get()) {
				if (mx > area.minX() && mx < area.maxX() && my > area.minY() && my < area.maxY())
					return false;
			}
		}
		return true;
	}

	protected static Component uistr(String key) {
		return Component.translatable("gui.machina." + key);
	}

	protected static String uistrs(String key) {
		return Component.translatable("gui.machina." + key).getString();
	}

	protected static void blitCommon(GuiGraphics gui, int x, int y, int u, int v, int w, int h) {
		gui.blit(COMMON_UI, x, y, u, v, w, h, 512, 512);
	}

	protected void blitOverlay(GuiGraphics gui, int x, int y, int w, int h) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1f, 1f, 1f, 0.1f);
		gui.blit(BG_OVERLAY, x, y, 0, 0, w, h, 512, 512);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	protected void drawString(GuiGraphics gui, Component text, int x, int y, int color) {
		gui.drawString(font, text, x, y, color);
	}

	protected void drawStringVertical(GuiGraphics gui, Component text, int x, int y, int color) {
		gui.pose().pushPose();
		gui.pose().translate(x, y, 0);
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.ZP, 90));
		gui.drawString(font, text, 0, 0, color);
		gui.pose().popPose();
	}

	protected void drawInventory(GuiGraphics gui, int mx, int my) {
		int i = midWidth();
		int j = midHeight();
		boolean hovered = this.hoveredSlot != null && this.hoveredSlot.container == mc.player.getInventory();

		int sx, sy;
		if (hovered) {
			sx = i + this.hoveredSlot.x + 2;
			sy = j + this.hoveredSlot.y + 3;
		} else {
			sx = Math.min(i + 196, Math.max(i + 23, mx - 5));
			sy = Math.min(j + 126, Math.max(j + 78, my - 5));
		}

		if (this.lsx == null || this.lsy == null) {
			this.lsx = (float) sx;
			this.lsy = (float) sy;
		} else {
			this.lsx += (sx - this.lsx) / 50f;
			this.lsy += (sy - this.lsy) / 50f;
		}

		int k1 = (int) Math.max(4 - this.aliveTicks, 0);
		int k2 = 6 - lsx.intValue() % 6;

		// Backdrop
		blitCommon(gui, i + 23, j + 78, 179, 0, 187, 92);
		blitCommon(gui, i + 27, j + 82, 0, 84 * k1, 179, 84);
		registerClickArea("inv", i + 23, j + 78, i + 210, j + 170, () -> true);

		// Active Slot
		if (hovered) {
			blitCommon(gui, i + this.hoveredSlot.x - 1, j + this.hoveredSlot.y - 1, 368, 2, 19, 19);
		}

		// Decorators
		blitCommon(gui, i + 18, lsy.intValue(), 366, 0, 2, 14);
		blitCommon(gui, lsx.intValue(), j + 173, 368, 0, 14, 2);
		blitCommon(gui, i + 27, j + 143, 179 + k2, 92, 179, 2);
		drawStringVertical(gui, Component.translatable("container.inventory"), i + 220, j + 78, 0x00FEFE);
	}

	protected void drawBackground(GuiGraphics gui) {
		int i = midWidth();
		int j = midHeight();

		blitCommon(gui, i, j - 73, 179, 94, 235, 151);
		drawStringVertical(gui, this.menu.getName(), i + 245, j - 71, 0x00FEFE);
		registerClickArea("bg", i, j - 73, i + 235, j + 78, () -> true);
	}

	public enum SpecialSlot {
		PLUS(475, 0),
		MINUS(485, 0),
		RIGHT(495, 0),
		DOWN(475, 10),
		UP(485, 10),
		LEFT(495, 10),
		BOLT(499, 23),
		CROSS(499, 33),
		COAL(499, 43),
		DUST(499, 53);

		protected int x;
		protected int y;

		SpecialSlot(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void draw(GuiGraphics gui, int x, int y, long aliveTicks) {
			if (appearDraw(aliveTicks))
				blitCommon(gui, x, y, this.x, this.y, 10, 10);
		}
	}

	protected void drawDownFacingSlot(GuiGraphics gui, int id, int mx, int my, int x, int y, SpecialSlot slot,
			String hover) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 19 && my > j && my < j + 21 ? 115 : 94;
		blitCommon(gui, i, j, 414, h, 19, 21);
		if (entity.getItem(id).isEmpty())
			slot.draw(gui, i + 4, j + 6, this.aliveTicks);

		blitCommon(gui, i - 6, j + 4, 387, 0, 3, 16);
		blitCommon(gui, i + 21, j + 4, 390, 0, 3, 16);

		if (!hover.isEmpty()) {
			registerHoverable("slot_" + x + "_" + y, i - 1, j + 1, i + 18, j + 20, () -> entity.getItem(id).isEmpty(),
					() -> uistr(hover));
		}
	}

	protected void drawUpFacingSlot(GuiGraphics gui, int id, int mx, int my, int x, int y, SpecialSlot slot,
			String hover) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 19 && my > j && my < j + 21 ? 115 : 94;
		blitCommon(gui, i, j, 433, h, 19, 21);
		if (entity.getItem(id).isEmpty())
			slot.draw(gui, i + 4, j + 4, this.aliveTicks);

		blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
		blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);

		if (!hover.isEmpty()) {
			registerHoverable("slot_" + x + "_" + y, i - 1, j + 1, i + 18, j + 20, () -> entity.getItem(id).isEmpty(),
					() -> uistr(hover));
		}
	}

	private void drawBar(GuiGraphics gui, int i, int j, int o, float p, boolean active, boolean under_deco, String text,
			String missing) {
		// Bar
		blitCommon(gui, i, j, 366, 21, 133, 18);
		blitCommon(gui, i + 1, j + 3, 366, 39 + o * 14, (int) (131 * p), 14);

		// Deco
		int dec_off = active ? 0 : 6;
		blitCommon(gui, i - 5, j + 2, 387 + dec_off, 0, 3, 16);
		blitCommon(gui, i + 135, j + 2, 390 + dec_off, 0, 3, 16);

		if (!active) {
			gui.drawCenteredString(font, uistr(missing), i + 66, j + 6, 0xFE0000);
		}

		if (under_deco) {
			dec_off = active ? 0 : 38;
			Component c = Component.literal(text);
			int w = font.width(c) / 2 + 2;
			gui.drawCenteredString(font, c, i + 66, j + 20, active ? 0x00FEFE : 0xFE0000);
			blitCommon(gui, i + 66 + w, j + 18, 418 + dec_off, 5, 19, 8);
			blitCommon(gui, i + 66 - w - 20, j + 18, 399 + dec_off, 5, 19, 8);
		}
	}

	protected void drawEnergyBar(GuiGraphics gui, int x, int y, boolean active, boolean under_deco, String missing) {
		int i = midWidth() + x - 66;
		int j = midHeight() + y - 9;
		registerHoverable("energy", i + 1, j + 1, i + 136, j + 18,
				() -> active ? Component.literal(StringUtils.formatPower(this.entity.getEnergy()) + " / "
						+ StringUtils.formatPower(this.entity.getMaxEnergy()) + " ("
						+ StringUtils.formatPercent(this.entity.getEnergyF()) + ")") : uistr(missing));
		drawBar(gui, i, j, 0, this.entity.getEnergyF(), active, under_deco,
				StringUtils.formatPower(this.entity.getEnergy()), missing);
	}

	private void drawFace(GuiGraphics gui, int x, int y, Direction dir, @Nullable ISideAdapter storage) {
		TextureAtlasSprite sprite = sprites.get(dir);
		int size = (int) (4.0F / sprite.uvShrinkRatio());
		gui.blit(sprite.atlasLocation(), x, y, sprite.getX(), sprite.getY(), 16, 16, size, size);

		if (storage != null) {
			Side side = storage.get(dir);
			blitCommon(gui, x + 4, y + 4, side.x(), side.y(), 8, 8);
		}
	}

	protected void drawItemSideConfig(GuiGraphics gui, int x, int y, int mx, int my, int slot, SpecialSlot special) {
		drawSideConfig(gui, x, y, mx, my, "item_" + slot, this.entity.getItemAdapter(slot), special);
	}

	protected void drawEnergySideConfig(GuiGraphics gui, int x, int y, int mx, int my) {
		drawSideConfig(gui, x, y, mx, my, "energy", this.entity.getEnergyAdapter(), SpecialSlot.BOLT);
	}

	protected void drawSideConfig(GuiGraphics gui, int x, int y, int mx, int my, String name,
			Supplier<ISideAdapter> adapter, SpecialSlot slot) {
		int i = midWidth() - 3 + x;
		int j = midHeight() - 73 + y;

		String key = "side_" + name;
		initState(key, false);
		registerClickable(key + "_off", i - 20, j, i, j + 20, key, false);
		registerClickable(key + "_on", i - 28, j + 5, i - 13, j + 20, key, true);

		// Face Hovers
		Supplier<Boolean> hover = () -> getState(key) && appearDraw(getElapsedState(key));
		Function<Direction, Runnable> click = d -> () -> adapter.get().cycle(d);
		Function<Direction, Supplier<Component>> text = d -> () -> Component
				.literal(uistrs("dir." + d.name().toLowerCase()) + ": "
						+ uistrs("side." + adapter.get().get(d).name().toLowerCase()));
		clickAndHover(key + "_up", i - 52, j + 8, i - 31, j + 29, hover, text.apply(Direction.UP),
				click.apply(Direction.UP));
		clickAndHover(key + "_north", i - 52, j + 27, i - 31, j + 48, hover, text.apply(Direction.NORTH),
				click.apply(Direction.NORTH));
		clickAndHover(key + "_down", i - 52, j + 46, i - 31, j + 67, hover, text.apply(Direction.DOWN),
				click.apply(Direction.DOWN));
		clickAndHover(key + "_west", i - 33, j + 27, i - 12, j + 48, hover, text.apply(Direction.WEST),
				click.apply(Direction.WEST));
		clickAndHover(key + "_east", i - 71, j + 27, i - 50, j + 48, hover, text.apply(Direction.EAST),
				click.apply(Direction.EAST));
		clickAndHover(key + "_south", i - 33, j + 46, i - 12, j + 67, hover, text.apply(Direction.SOUTH),
				click.apply(Direction.SOUTH));

		int anim = getAnimationState(key, 4);
		int elap = getElapsedState(key);
		boolean state = getState(key);

		blitCommon(gui, i - 75, j, anim * 75, 336, 75, 68);
		registerClickArea(key, i - 75, j, i, j + 68, () -> getState(key));

		if (appearDraw(elap)) {
			if (state) {

				// Close Button
				if (mx > i - 28 && mx < i - 13 && my > j + 5 && my < j + 20) {
					blitCommon(gui, i - 27, j + 6, 452, 108, 14, 14);
				} else {
					blitCommon(gui, i - 27, j + 6, 452, 94, 14, 14);
				}
				SpecialSlot.CROSS.draw(gui, i - 25, j + 8, elap);

				// Machine
				ISideAdapter storage = adapter.get();
				drawFace(gui, i - 51, j + 9, Direction.UP, storage);
				drawFace(gui, i - 51, j + 28, Direction.NORTH, storage);
				drawFace(gui, i - 51, j + 47, Direction.DOWN, storage);
				drawFace(gui, i - 32, j + 28, Direction.WEST, storage);
				drawFace(gui, i - 70, j + 28, Direction.EAST, storage);
				drawFace(gui, i - 32, j + 47, Direction.SOUTH, storage);

				// Deorators
				gui.drawString(font, uistr("config." + name), i - 75, j + 71, 0x00FEFE);
			} else {

				// Open Button
				if (mx > i - 20 && mx < i && my > j && my < j + 20) {
					blitCommon(gui, i - 18, j + 2, 414, 136, 17, 16);
				}
				slot.draw(gui, i - 15, j + 5, elap);
			}
		}
	}

	protected void drawOverlay(GuiGraphics gui) {
		int height = this.height + 4;
		int width = this.width;

		int k = (int) (this.aliveTicks / 3 % 4);
		int cw = width / 512;
		int ch = height / 512;
		int tw = cw * 512;
		int th = ch * 512;

		for (int i = 0; i < cw; i++) {
			for (int j = 0; j < ch; j++) {
				blitOverlay(gui, i * 512, j * 512 + k, 512, 512);
			}
		}

		for (int i = 0; i < cw; i++) {
			blitOverlay(gui, i * 512, th + k, 512, height - th);
		}

		for (int j = 0; j < ch; j++) {
			blitOverlay(gui, tw, j * 512 + k, width - tw, 512);
		}

		blitOverlay(gui, tw, th + k, width - tw, height - th);
	}

	public void drawMultiblock(GuiGraphics gui, ResourceLocation mbloc, int xPos, int yPos, int s, float pt) {
		int x = midWidth() + xPos;
		int y = midHeight() + yPos;
		ClientMultiblock mb = new ClientMultiblock(MultiblockLoader.INSTANCE.get(mbloc));
		Vec3i size = mb.mb.size;
		int sizeX = size.getX();
		int sizeY = size.getY();
		int sizeZ = size.getZ();
		float maxX = 90;
		float maxY = 90;
		float diag = (float) Math.sqrt(sizeX * sizeX + sizeZ * sizeZ);
		float scaleX = maxX / diag;
		float scaleY = maxY / sizeY;
		float scale = -Math.min(scaleX, scaleY) * s;

		gui.pose().pushPose();
		gui.pose().translate(x, y, 100);
		gui.pose().scale(scale, scale, scale);
		gui.pose().translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
		Matrix4f rotMat = new Matrix4f();
		rotMat.identity();
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.XP, rotX - 30F));
		rotMat.rotate(VecUtil.rotationDegrees(VecUtil.XP, 30F - rotX));

		float offX = (float) -sizeX / 2;
		float offZ = (float) -sizeZ / 2 + 1;
		gui.pose().translate(-offX, 0, -offZ);
		gui.pose().mulPose(VecUtil.rotationDegrees(VecUtil.YP, 45F - rotY));
		rotMat.rotate(VecUtil.rotationDegrees(VecUtil.YP, rotY - 45F));
		gui.pose().translate(offX, 0, offZ);

		renderElements(gui.pose(), mb, size, pt, pos -> false, rotX < 30F);

		gui.pose().popPose();
	}

	private static BufferSource mbBuffers = null;

	private static void renderElements(PoseStack ms, ClientMultiblock mb, Vec3i dest, float par,
			Predicate<BlockPos> transparency, boolean flip) {
		if (mbBuffers == null) {
			mbBuffers = initBuffers(mc.renderBuffers().bufferSource(), 0.2f);
		}

		BufferSource buffers = mc.renderBuffers().bufferSource();

		ms.pushPose();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		ms.translate(0, 0, -1);

		doWorldRenderPass(ms, mbBuffers, buffers, mb, dest, transparency, flip);
		mbBuffers.endBatch();
		buffers.endBatch();

		ms.popPose();
	}

	private static void doWorldRenderPass(PoseStack ms, @Nonnull BufferSource tpBuffers,
			@Nonnull BufferSource nmBuffers, ClientMultiblock mb, Vec3i dest, Predicate<BlockPos> transparency,
			boolean flip) {
		boolean last = false;
		for (int y = 0; y < dest.getY(); y++) {
			for (int x = 0; x < dest.getX(); x++) {
				for (int z = 0; z < dest.getZ(); z++) {
					BlockPos pos = new BlockPos(x, flip ? y : dest.getY() - y - 1, z);
					boolean tp = !transparency.test(pos);
					if (last != tp) {
						(last ? nmBuffers : tpBuffers).endBatch();
					}
					mb = mb.restrict(has -> tp ? !transparency.test(has) : transparency.test(has));
					BlockState bs = mb.getBlockState(pos);

					ms.pushPose();
					ms.translate(pos.getX(), pos.getY(), pos.getZ());
					for (RenderType layer : RenderType.chunkBufferLayers()) {
						VertexConsumer buffer = (tp ? nmBuffers : tpBuffers).getBuffer(layer);
						Vec3 vector3d = bs.getOffset(mb, pos);
						ms.translate(vector3d.x, vector3d.y, vector3d.z);
						BakedModel model = mc.getBlockRenderer().getBlockModel(bs);
						ModelData modelData = model.getModelData(mb, pos, bs, ModelData.EMPTY);
						mc.getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, bs, model, pos.getX(),
								pos.getY(), pos.getZ(), 255, OverlayTexture.NO_OVERLAY, modelData, layer);
					}
					ms.popPose();
					last = tp;
				}
			}
		}
	}

	private static BufferSource initBuffers(BufferSource original, float alpha) {
		Map<RenderType, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
		for (Map.Entry<RenderType, BufferBuilder> e : original.fixedBuffers.entrySet()) {
			remapped.put(MultiblockRenderType.remap(e.getKey(), alpha), e.getValue());
		}
		return new MultiblockBuffers(original.builder, remapped, alpha);
	}

	private static class MultiblockBuffers extends BufferSource {

		private final float alpha;

		protected MultiblockBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> layerBuffers, float alpha) {
			super(fallback, layerBuffers);
			this.alpha = alpha;
		}

		@Override
		public VertexConsumer getBuffer(RenderType type) {
			return super.getBuffer(MultiblockRenderType.remap(type, alpha));
		}
	}

	private static class MultiblockRenderType extends RenderType {
		private static Map<RenderType, RenderType> remappedTypes = new IdentityHashMap<>();

		private MultiblockRenderType(RenderType original, float alpha) {
			super(String.format("%s_%s_multiblock", original.toString(), Machina.MOD_ID), original.format(),
					original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
						original.setupRenderState();

						RenderSystem.disableDepthTest();
						RenderSystem.enableBlend();
						RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA,
								GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
						RenderSystem.setShaderColor(1, 1, 1, alpha);
					}, () -> {
						RenderSystem.setShaderColor(1, 1, 1, 1);
						RenderSystem.defaultBlendFunc();
						RenderSystem.disableBlend();
						RenderSystem.enableDepthTest();

						original.clearRenderState();
					});
		}

		@Override
		public boolean equals(@Nullable Object other) {
			return this == other;
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(this);
		}

		public static RenderType remap(RenderType in, float alpha) {
			if (in instanceof MultiblockRenderType) {
				return in;
			} else {
				return remappedTypes.computeIfAbsent(in, a -> new MultiblockRenderType(a, alpha));
			}
		}
	}

	@Override
	protected void renderTooltip(GuiGraphics gui, int mx, int my) {
		super.renderTooltip(gui, mx, my);

		for (Hoverable h : hoverables.values()) {
			if (h.active().get()) {
				if (mx > h.minX() && mx < h.maxX() && my > h.minY() && my < h.maxY()) {
					gui.renderTooltip(font, h.text().get(), mx, my);
					break;
				}
			}
		}
	}

	private record ClickArea(int minX, int minY, int maxX, int maxY, Supplier<Boolean> enabled) {
	}

	private record Hoverable(int minX, int minY, int maxX, int maxY, Supplier<Boolean> active,
			Supplier<Component> text) {
	}

	private record Clickable(int minX, int minY, int maxX, int maxY, Supplier<Boolean> active, Runnable action) {
	}

	private record Stateable(boolean state, long lastClick) {
	}

	private void registerClickArea(String key, int minX, int minY, int maxX, int maxY, Supplier<Boolean> enabled) {
		this.clickareas.putIfAbsent(key, new ClickArea(minX, minY, maxX, maxY, enabled));
	}

	private void registerHoverable(String key, int minX, int minY, int maxX, int maxY, Supplier<Boolean> active,
			Supplier<Component> text) {
		this.hoverables.putIfAbsent(key, new Hoverable(minX, minY, maxX, maxY, active, text));
	}

	private void registerHoverable(String key, int minX, int minY, int maxX, int maxY, Supplier<Component> text) {
		registerHoverable(key, minX, minY, maxX, maxY, () -> true, text);
	}

	private void registerClickable(String key, int minX, int minY, int maxX, int maxY, Supplier<Boolean> active,
			Runnable action) {
		this.clickables.putIfAbsent(key, new Clickable(minX, minY, maxX, maxY, active, action));
	}

	private void registerClickable(String key, int minX, int minY, int maxX, int maxY, String state, boolean val) {
		registerClickable(key, minX, minY, maxX, maxY, () -> getState(state) == val, () -> setState(state, !val));
	}

	private void clickAndHover(String key, int minX, int minY, int maxX, int maxY, Supplier<Boolean> active,
			Supplier<Component> text, Runnable action) {
		registerHoverable(key, minX, minY, maxX, maxY, active, text);
		registerClickable(key, minX, minY, maxX, maxY, active, action);
	}

	public void initState(String key, boolean initial) {
		this.stateables.putIfAbsent(key, new Stateable(initial, -100));
	}

	public void setState(String key, boolean state) {
		this.stateables.put(key, new Stateable(state, this.aliveTicks));
	}

	public boolean getState(String key) {
		Stateable s = this.stateables.get(key);
		return s != null && s.state;
	}

	public int getAnimationState(String key, int max) {
		Stateable s = this.stateables.get(key);
		if (s == null)
			return 0;
		int m = Math.min((int) Math.max(s.lastClick() - this.aliveTicks + max, 0), max);
		return s.state() ? m : max - m - 1;
	}

	public int getElapsedState(String key) {
		Stateable s = this.stateables.get(key);
		return s == null ? 0 : (int) (this.aliveTicks - s.lastClick());
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		// Rotate - Right Click
		if (pButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			this.rotX -= (float) pDragY / (float) height * 80f;
			this.rotY -= (float) pDragX / (float) width * 180f;
			this.rotX = MathUtil.clamp(this.rotX, 0f, 60f);
		}

		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseClicked(double x, double y, int button) {
		for (Clickable c : clickables.values()) {
			if (x > c.minX() && x < c.maxX() && y > c.minY() && y < c.maxY()) {
				if (c.active().get()) {
					c.action().run();
					return true;
				}
			}
		}

		return super.mouseClicked(x, y, button);
	}

	private static boolean appearDraw(long elap) {
		return elap > 9 || elap == 5 || elap == 7 || elap == 8;
	}
}
