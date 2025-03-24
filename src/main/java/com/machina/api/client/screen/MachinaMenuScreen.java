package com.machina.api.client.screen;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.machina.Machina;
import com.machina.api.block.entity.ContainerBlockEntity;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.block.menu.MachinaAnyMenu;
import com.machina.api.cap.sided.ISideAdapter;
import com.machina.api.cap.sided.Side;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.multiblock.ClientMultiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.util.StringUtils;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.VecUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.FluidStack;

public abstract class MachinaMenuScreen<T extends MachinaAnyMenu> extends AbstractContainerScreen<T> {

	private static final Minecraft mc = Minecraft.getInstance();

	private final ContainerBlockEntity entity;

	protected long aliveTicks = 0;
	private Float lsx, lsy = null;
	private float rotX, rotY;

	private final Map<String, ClickArea> clickareas = new HashMap<>();
	private final Map<String, Clickable> clickables = new HashMap<>();
	private final Map<String, Hoverable> hoverables = new HashMap<>();
	private final Map<String, Stateable> stateables = new HashMap<>();
	private final Map<Direction, TextureAtlasSprite> sprites = new HashMap<>();

	public MachinaMenuScreen(T menu, Inventory inv, Component title) {
		super(menu, inv, title);

		this.imageWidth = 235;
		this.imageHeight = 100;

		RandomSource rand = RandomSource.create();

		this.entity = menu.getBlockEntity();

		BlockState state = menu.getDefaultState();

		if (state != null) {
			BakedModel model = mc.getBlockRenderer().getBlockModel(state);
			for (Direction d : Direction.values()) {
				sprites.put(d, model.getQuads(state, d, rand, ModelData.EMPTY, null).get(0).getSprite());
			}
		}
	}

	@Override
	protected void init() {
		this.clickareas.clear();
		this.clickables.clear();
		this.hoverables.clear();
		super.init();
	}

	public void render(@NotNull GuiGraphics gui, int mx, int my, float pt) {
		this.renderBackground(gui);
		if (appearDraw(this.aliveTicks))
			super.render(gui, mx, my, pt);
		else
			this.renderBg(gui, pt, mx, my);
		this.renderTooltip(gui, mx, my);
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics gui, int x, int y) {
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

	protected void drawInventory(GuiGraphics gui, int mx, int my) {
		int i = midWidth();
		int j = midHeight();
		boolean hovered = false;
		if (mc.player != null) {
			hovered = this.hoveredSlot != null && this.hoveredSlot.container == mc.player.getInventory();
		}

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
		MUI.blitCommon(gui, i + 23, j + 78, 179, 0, 187, 92);
		MUI.blitCommon(gui, i + 27, j + 82, 0, 84 * k1, 179, 84);
		registerClickArea("inv", i + 23, j + 78, i + 210, j + 170, () -> true);

		// Active Slot
		if (hovered) {
			MUI.blitCommon(gui, i + this.hoveredSlot.x - 1, j + this.hoveredSlot.y - 1, 368, 2, 19, 19);
		}

		// Decorators
		MUI.blitCommon(gui, i + 18, lsy.intValue(), 366, 0, 2, 14);
		MUI.blitCommon(gui, lsx.intValue(), j + 173, 368, 0, 14, 2);
		MUI.blitCommon(gui, i + 27, j + 143, 179 + k2, 92, 179, 2);
		MUI.drawStringVertical(gui, Component.translatable("container.inventory"), i + 220, j + 78);
	}

	protected void drawBackground(GuiGraphics gui) {
		int i = midWidth();
		int j = midHeight();

		MUI.blitCommon(gui, i, j - 73, 179, 94, 235, 151);
		MUI.drawStringVertical(gui, this.menu.getName(), i + 245, j - 71);
		registerClickArea("bg", i, j - 73, i + 235, j + 78, () -> true);
	}

	protected void drawMiniBackground(GuiGraphics gui) {
		int i = midWidth();
		int j = midHeight();

		MUI.blitCommon(gui, i + 53, j - 7, 179, 245, 129, 85);
		MUI.drawString(gui, this.menu.getName(), i + 55, j - 17);
		registerClickArea("bg", i + 53, j - 7, i + 129 + 53, j + 78, () -> true);
	}

	protected void drawDownFacingSlot(GuiGraphics gui, int id, int mx, int my, int x, int y, MuiSlot slot,
			String hover) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 19 && my > j && my < j + 21 ? 115 : 94;
		MUI.blitCommon(gui, i, j, 414, h, 19, 21);
		if (id != -1 && entity.getItem(id).isEmpty())
			slot.draw(gui, i + 4, j + 6, this.aliveTicks);

		MUI.blitCommon(gui, i - 6, j + 4, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 4, 390, 0, 3, 16);

		if (!hover.isEmpty()) {
			registerHoverable("slot_" + x + "_" + y, i - 1, j + 1, i + 18, j + 20,
					() -> id != -1 && entity.getItem(id).isEmpty(), () -> MUI.uistr(hover));
		}
	}

	protected void drawUpFacingSlot(GuiGraphics gui, int id, int mx, int my, int x, int y, MuiSlot slot,
			String hover) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 19 && my > j && my < j + 21 ? 115 : 94;
		MUI.blitCommon(gui, i, j, 433, h, 19, 21);
		if (id != -1 && entity.getItem(id).isEmpty())
			slot.draw(gui, i + 4, j + 4, this.aliveTicks);

		MUI.blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);

		if (!hover.isEmpty()) {
			registerHoverable("slot_" + x + "_" + y, i - 1, j + 1, i + 18, j + 20,
					() -> id != -1 && entity.getItem(id).isEmpty(), () -> MUI.uistr(hover));
		}
	}

	protected void drawNoFacingSlot(GuiGraphics gui, int id, int mx, int my, int x, int y, MuiSlot slot,
			String hover) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
		MUI.blitCommon(gui, i, j, 466, h, 19, 19);
		if (id != -1 && entity.getItem(id).isEmpty())
			slot.draw(gui, i + 4, j + 4, this.aliveTicks);

		MUI.blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);

		if (!hover.isEmpty()) {
			registerHoverable("slot_" + x + "_" + y, i, j, i + 18, j + 18,
					() -> id != -1 && entity.getItem(id).isEmpty(), () -> MUI.uistr(hover));
		}
	}

	protected void drawGhostSlot(GuiGraphics gui, Supplier<Boolean> empty, int mx, int my, int x, int y,
			MuiSlot slot, String hover, BiConsumer<Integer, Integer> render) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
		MUI.blitCommon(gui, i, j, 466, h, 19, 19);
		if (empty.get())
			slot.draw(gui, i + 4, j + 4, this.aliveTicks);

		MUI.blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);

		render.accept(i, j);

		if (!hover.isEmpty()) {
			registerHoverable("ghost_" + x + "_" + y, i, j, i + 18, j + 18, empty, () -> MUI.uistr(hover));
		}
	}

	protected void drawToggle(GuiGraphics gui, int mx, int my, int x, int y, boolean initial, MuiSlot slot1,
			MuiSlot slot2, Consumer<Boolean> onClick, Supplier<Component> onHover) {
		String key = "toggle_" + x + "_" + y;
		initState(key, initial);

		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
		MUI.blitCommon(gui, i, j, 466, h, 19, 19);
		MuiSlot slot = getState(key) ? slot1 : slot2;
		slot.draw(gui, i + 4, j + 4, this.aliveTicks);

		MUI.blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);

		registerClickable(key, i, j, i + 18, j + 18, key, onClick);
		registerHoverable(key, i, j, i + 18, j + 18, onHover);
	}

	protected void drawToggleIO(GuiGraphics gui, int mx, int my, int x, int y, Side side, Supplier<Component> onHover,
			Runnable onClick) {
		String key = "toggle_" + x + "_" + y;

		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;

		MUI.blitCommon(gui, i, j, 466, h, 19, 19);
		MUI.blitCommon(gui, i + 5, j + 5, side.x(), side.y(), 8, 8);

		MUI.blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);

		registerClickable(key, i, j, i + 18, j + 18, key, s -> onClick.run());
		registerHoverable(key, i, j, i + 18, j + 18, onHover);
	}

	@SuppressWarnings("hiding")
	private <T extends Number> void drawBar(GuiGraphics gui, int x, int y, boolean active, String missing,
			Function<T, String> formatter, Supplier<MutableComponent> name, Supplier<T> value, Supplier<T> max,
			Supplier<Float> f, TriConsumer<Integer, Integer, Float> drawer) {
		int i = midWidth() + x + 117 - 66;
		int j = midHeight() + y - 9;
		registerHoverable("bar_" + x + "_" + y, i + 1, j + 1, i + 136, j + 18,
				() -> active
						? name.get()
								.append(Component.literal(formatter.apply(value.get()) + " / "
										+ formatter.apply(max.get()) + " (" + StringUtils.formatPercent(f.get()) + ")"))
						: MUI.uistr(missing));
		MUI.drawBar(gui, i, j, f.get(), active, formatter.apply(value.get()), missing, drawer);
	}

	protected void drawEnergyBar(GuiGraphics gui, int x, int y, boolean active, String missing) {
		if (entity instanceof MachinaBlockEntity) {
			MachinaBlockEntity mbe = (MachinaBlockEntity) entity;
			drawBar(gui, x, y, active, missing, StringUtils::formatPower, Component::empty, mbe::getEnergy,
					mbe::getMaxEnergy, mbe::getEnergyF, (i, j, p) -> {
						MUI.blitCommon(gui, i + 1, j + 3, 366, 39, (int) (131 * p), 14);
					});
		}
	}

	protected void drawFluidBar(GuiGraphics gui, int x, int y, int tank) {
		if (entity instanceof MachinaBlockEntity) {
			MachinaBlockEntity mbe = (MachinaBlockEntity) entity;
			drawBar(gui, x, y, true, "", StringUtils::formatFluid, Component::empty, () -> mbe.getFluidMB(tank),
					() -> mbe.getTankCapacity(tank), () -> mbe.getFluidF(tank), (i, j, p) -> {
						float prop = mbe.getFluidF(tank);
						renderFluid(gui, mbe.getFluid(tank), i + 1, j + 17, (int) (131 * prop), 14, 0);
					});
		}
	}

	private void drawFace(GuiGraphics gui, int x, int y, Direction dir, @Nullable ISideAdapter storage) {
		TextureAtlasSprite sprite = sprites.get(dir);
		int size = (int) (4.0F / sprite.uvShrinkRatio());
		gui.blit(sprite.atlasLocation(), x, y, sprite.getX(), sprite.getY(), 16, 16, size, size);

		if (storage != null) {
			Side side = storage.get(dir);
			MUI.blitCommon(gui, x + 4, y + 4, side.x(), side.y(), 8, 8);
		}
	}

	protected void drawItemSideConfig(GuiGraphics gui, int x, int y, int mx, int my, int slot, MuiSlot special) {
		if (entity instanceof MachinaBlockEntity) {
			MachinaBlockEntity mbe = (MachinaBlockEntity) entity;
			drawSideConfig(gui, x, y, mx, my, "item_" + slot, mbe.getItemAdapter(slot), special);
		}
	}

	protected void drawEnergySideConfig(GuiGraphics gui, int mx, int my) {
		if (entity instanceof MachinaBlockEntity) {
			MachinaBlockEntity mbe = (MachinaBlockEntity) entity;
			drawSideConfig(gui, 0, 0, mx, my, "energy", mbe.getEnergyAdapter(), MuiSlot.ENERGY);
		}
	}

	protected void drawSideConfig(GuiGraphics gui, int x, int y, int mx, int my, String name,
			Supplier<ISideAdapter> adapter, MuiSlot slot) {
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
				.literal(MUI.uistrs("dir." + d.name().toLowerCase()) + ": "
						+ MUI.uistrs("side." + adapter.get().get(d).name().toLowerCase()));
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

		MUI.blitCommon(gui, i - 75, j, anim * 75, 336, 75, 68);
		registerClickArea(key, i - 75, j, i, j + 68, () -> getState(key));

		if (appearDraw(elap)) {
			if (state) {

				// Close Button
				if (mx > i - 28 && mx < i - 13 && my > j + 5 && my < j + 20) {
					MUI.blitCommon(gui, i - 27, j + 6, 452, 108, 14, 14);
				} else {
					MUI.blitCommon(gui, i - 27, j + 6, 452, 94, 14, 14);
				}
				MuiSlot.CROSS.draw(gui, i - 25, j + 8, elap);

				// Machine
				ISideAdapter storage = adapter.get();
				drawFace(gui, i - 51, j + 9, Direction.UP, storage);
				drawFace(gui, i - 51, j + 28, Direction.NORTH, storage);
				drawFace(gui, i - 51, j + 47, Direction.DOWN, storage);
				drawFace(gui, i - 32, j + 28, Direction.WEST, storage);
				drawFace(gui, i - 70, j + 28, Direction.EAST, storage);
				drawFace(gui, i - 32, j + 47, Direction.SOUTH, storage);

				// Deorators
				MUI.drawString(gui, MUI.uistr("config." + name), i - 75, j + 71);
			} else {

				// Open Button
				if (mx > i - 20 && mx < i && my > j && my < j + 20) {
					MUI.blitCommon(gui, i - 18, j + 2, 414, 136, 17, 16);
				}
				slot.draw(gui, i - 15, j + 5, elap);
			}
		}
	}

	protected void drawOverlay(GuiGraphics gui) {
		MUI.drawOverlay(gui, this.width, this.height, this.aliveTicks);
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
			mbBuffers = initBuffers(mc.renderBuffers().bufferSource());
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
					mb = mb.restrict(has -> tp != transparency.test(has));
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

	private static BufferSource initBuffers(BufferSource original) {
		Map<RenderType, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
		for (Map.Entry<RenderType, BufferBuilder> e : original.fixedBuffers.entrySet()) {
			remapped.put(MultiblockRenderType.remap(e.getKey(), (float) 0.2), e.getValue());
		}
		return new MultiblockBuffers(original.builder, remapped);
	}

	private static class MultiblockBuffers extends BufferSource {

		private final float alpha;

		protected MultiblockBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> layerBuffers) {
			super(fallback, layerBuffers);
			this.alpha = (float) 0.2;
		}

		@Override
		public @NotNull VertexConsumer getBuffer(@NotNull RenderType type) {
			return super.getBuffer(MultiblockRenderType.remap(type, alpha));
		}
	}

	private static class MultiblockRenderType extends RenderType {
		private static final Map<RenderType, RenderType> remappedTypes = new IdentityHashMap<>();

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

	public static void renderFluid(GuiGraphics gui, FluidStack fluid, int x, int y, int sx, int sy, int blit) {
		if (!fluid.isEmpty()) {
			TextureAtlasSprite icon = getFluidTexture(fluid);
			if (icon != null) {
				color(IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid));
				drawTiledSprite(gui, x, y, 0, sx, sy, icon, 16, 16, 0, TilingDirection.DOWN_RIGHT);
				resetColor();
			}
		}
	}

	// Mekanism
	public enum TilingDirection {
		DOWN_RIGHT(true, true),
		DOWN_LEFT(true, false),
		UP_RIGHT(false, true),
		UP_LEFT(false, false);

		private final boolean down;
		private final boolean right;

		TilingDirection(boolean down, boolean right) {
			this.down = down;
			this.right = right;
		}
	}

	// https://github.com/mekanism/Mekanism/blob/160d59e8d4b11aec446fc4d7d84b9f01dba5da68/src/main/java/mekanism/client/gui/GuiUtils.java
	public static void drawTiledSprite(GuiGraphics gui, int xPosition, int yPosition, int yOffset, int desiredWidth,
			int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel,
			TilingDirection tilingDirection) {
		drawTiledSprite(gui, xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth,
				textureHeight, zLevel, tilingDirection, true);
	}

	// https://github.com/mekanism/Mekanism/blob/160d59e8d4b11aec446fc4d7d84b9f01dba5da68/src/main/java/mekanism/client/gui/GuiUtils.java
	public static void drawTiledSprite(GuiGraphics guiGraphics, int xPosition, int yPosition, int yOffset,
			int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight,
			int zLevel, TilingDirection tilingDirection, boolean blend) {
		if (desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0) {
			return;
		}
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, sprite.atlasLocation());
		int xTileCount = desiredWidth / textureWidth;
		int xRemainder = desiredWidth - (xTileCount * textureWidth);
		int yTileCount = desiredHeight / textureHeight;
		int yRemainder = desiredHeight - (yTileCount * textureHeight);
		int yStart = yPosition + yOffset;
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		float uDif = uMax - uMin;
		float vDif = vMax - vMin;
		if (blend) {
			RenderSystem.enableBlend();
		}
		BufferBuilder vertexBuffer = Tesselator.getInstance().getBuilder();
		vertexBuffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			int width = (xTile == xTileCount) ? xRemainder : textureWidth;
			if (width == 0) {
				break;
			}
			int x = xPosition + (xTile * textureWidth);
			int maskRight = textureWidth - width;
			int shiftedX = x + textureWidth - maskRight;
			float uLocalDif = uDif * maskRight / textureWidth;
			float uLocalMin;
			float uLocalMax;
			if (tilingDirection.right) {
				uLocalMin = uMin;
				uLocalMax = uMax - uLocalDif;
			} else {
				uLocalMin = uMin + uLocalDif;
				uLocalMax = uMax;
			}
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int height = (yTile == yTileCount) ? yRemainder : textureHeight;
				if (height == 0) {
					break;
				}
				int y = yStart - ((yTile + 1) * textureHeight);
				int maskTop = textureHeight - height;
				float vLocalDif = vDif * maskTop / textureHeight;
				float vLocalMin;
				float vLocalMax;
				if (tilingDirection.down) {
					vLocalMin = vMin;
					vLocalMax = vMax - vLocalDif;
				} else {
					vLocalMin = vMin + vLocalDif;
					vLocalMax = vMax;
				}
				vertexBuffer.vertex(matrix4f, x, y + textureHeight, zLevel).uv(uLocalMin, vLocalMax).endVertex();
				vertexBuffer.vertex(matrix4f, shiftedX, y + textureHeight, zLevel).uv(uLocalMax, vLocalMax).endVertex();
				vertexBuffer.vertex(matrix4f, shiftedX, y + maskTop, zLevel).uv(uLocalMax, vLocalMin).endVertex();
				vertexBuffer.vertex(matrix4f, x, y + maskTop, zLevel).uv(uLocalMin, vLocalMin).endVertex();
			}
		}
		BufferUploader.drawWithShader(vertexBuffer.end());
		if (blend) {
			RenderSystem.disableBlend();
		}
	}

	public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack stack) {
		return getSprite(IClientFluidTypeExtensions.of(stack.getFluid()).getStillTexture());
	}

	public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
		return mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(spriteLocation);
	}

	public static void color(int color) {
		float r = getRed(color);
		float g = getGreen(color);
		float b = getBlue(color);
		float a = getAlpha(color);
		RenderSystem.setShaderColor(r, g, b, a);
	}

	public static void resetColor() {
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	public static float getRed(int color) {
		return FastColor.ARGB32.red(color) / 255.0F;
	}

	public static float getGreen(int color) {
		return FastColor.ARGB32.green(color) / 255.0F;
	}

	public static float getBlue(int color) {
		return FastColor.ARGB32.blue(color) / 255.0F;
	}

	public static float getAlpha(int color) {
		return FastColor.ARGB32.alpha(color) / 255.0F;
	}

	@Override
	protected void renderTooltip(@NotNull GuiGraphics gui, int mx, int my) {
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

	private void registerClickable(String key, int minX, int minY, int maxX, int maxY, String state,
			Consumer<Boolean> setter) {
		registerClickable(key, minX, minY, maxX, maxY, () -> true, () -> {
			boolean val = getState(state);
			setState(state, !val);
			setter.accept(!val);
		});
	}

	private void clickAndHover(String key, int minX, int minY, int maxX, int maxY, Supplier<Boolean> active,
			Supplier<Component> text, Runnable action) {
		registerHoverable(key, minX, minY, maxX, maxY, active, text);
		registerClickable(key, minX, minY, maxX, maxY, active, action);
	}

	protected void clickAndHoverItem(int minX, int minY, int maxX, int maxY, Supplier<Boolean> active,
			Supplier<Component> text, Consumer<ItemStack> action) {
		clickAndHover("click_and_hover" + minX + "_" + minY, minX, minY, maxX, maxY, active, text,
				() -> action.accept(this.menu.getCarried()));
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

	@SuppressWarnings("unchecked")
	protected <X extends MachinaBlockEntity> X entity() {
		return (X) entity;
	}
}
