package com.machina.client.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.google.common.util.concurrent.Runnables;
import com.machina.api.client.celestial.CelestialRenderInfo;
import com.machina.api.client.celestial.CelestialRenderer;
import com.machina.api.client.celestial.CelestialUIRenderInfo;
import com.machina.api.client.screen.MUI;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.api.util.StringUtils;
import com.machina.api.util.math.VecUtil;
import com.machina.weather.system.ServerWeatherSystem;
import com.machina.weather.system.ServerWeatherSystem.RoughTemperatureRange;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;

public class StarchartRenderable {

	private record Hoverable(int minX, int minY, int maxX, int maxY, Supplier<List<Component>> text) {
	}

	private final static Minecraft mc = Minecraft.getInstance();
	private final static float NEAR_PLANE = 0.005f;
	private final static float FAR_PLANE = 500000000f;

	final SolarSystem system;
	final boolean paused;

	private final float maxZoom;

	private List<Consumer<Integer>> select;
	private final Map<String, Hoverable> hoverables = new HashMap<>();

	private float rotX = 0;
	private float rotY = 90;
	private float posX = 0;
	private float posY = 0;
	private float orbitalSpeed = 0.01f;
	private float zoom;
	private List<CelestialUIRenderInfo> queue;
	public CelestialUIRenderInfo tracked;

	private Vec3 trackedOrbitalPos;
	private float targetZoom;
	private float smoothing = 0.05f;
	private float smoothedYOff = 0;
	private float targetYOff = 0;

	private double realTime = 0;
	private double accumulatedTime = 0;

	public StarchartRenderable(SolarSystem s, boolean paused) {
		this.system = s;
		this.paused = paused;

		this.maxZoom = calculateZoom(system.maxAphelion());
		this.zoom = maxZoom;

		this.select = new ArrayList<>();
		this.resize();
	}

	public void resize() {
		this.hoverables.clear();
	}

	public float calculateZoom(double targetAphelion) {
		double a = 1.34D;
		double b = 0.032D;
		double c = 20.25D;
		double logx = Math.log(targetAphelion);
		double logx2 = Math.pow(logx, 2);
		return (float) Math.pow(2, -b * logx2 + -a * logx + c);
	}

	public Quaternionf createRotQuat(float x, float y) {
		float hy = Mth.DEG_TO_RAD * y * 0.5F;
		float hp = Mth.DEG_TO_RAD * x * 0.5F;
		float shy = Mth.sin(hy);
		float chy = Mth.cos(hy);
		float shp = Mth.sin(hp);
		float chp = Mth.cos(hp);
		float qw = chy * chp;
		float qx = shy * chp;
		float qy = chy * shp;
		float qz = shy * shp;
		return new Quaternionf(qx, qy, qz, qw);
	}

	public void addSelectListener(Consumer<Integer> selected) {
		this.select.add(selected);
	}

	private void registerHoverable(String key, int minX, int minY, int maxX, int maxY, Supplier<List<Component>> text) {
		this.hoverables.putIfAbsent(key, new Hoverable(minX, minY, maxX, maxY, text));
	}

	public void render(@NotNull GuiGraphics gui, int x, int y, int xOff, int yOff, int width, int height) {
		render(gui, x, y, xOff, yOff, width, height, Runnables.doNothing(), Runnables.doNothing());
	}

	public void render(@NotNull GuiGraphics gui, int x, int y, int xOff, int yOff, int width, int height,
			Runnable infoUnderlay, Runnable infoOverlay) {
		MUI.drawStars(gui, x, y, width, height);
		infoUnderlay.run();

		float frameTime = mc.getTimer().getGameTimeDeltaPartialTick(true);
		if (!this.paused && this.tracked == null) {
			accumulatedTime += frameTime * orbitalSpeed;
		}
		realTime += frameTime;
		updateCameraTracking();
		setupAndRenderCelestials(gui, width, height, xOff, yOff, createRotQuat(rotX, rotY), accumulatedTime, realTime);
		infoOverlay.run();
	}

	protected void updateCameraTracking() {
		targetYOff = (tracked != null) ? -0.07f : 0f;
		smoothedYOff = Mth.lerp(smoothing, smoothedYOff, targetYOff);

		if (tracked != null && trackedOrbitalPos != null) {
			float targetPosX = -(float) trackedOrbitalPos.x;
			float targetPosY = -(float) trackedOrbitalPos.z;

			posX = Mth.lerp(smoothing, posX, targetPosX);
			posY = Mth.lerp(smoothing, posY, targetPosY);

			// Snapping logic
			if ((posX - targetPosX) * (posX - targetPosX) + (posY - targetPosY) * (posY - targetPosY) < 0.01f) {
				posX = targetPosX;
				posY = targetPosY;
				float zoomSmoothing = smoothing; // Slower smoothing for zoom
				float t = 1f - (float) Math.pow(1f - zoomSmoothing, 3.0);
				zoom = Mth.lerp(t, zoom, targetZoom);
			}
			if (Math.abs(zoom - targetZoom) < 0.001f) {
				zoom = targetZoom;

			}
		}
	}

	protected void setupAndRenderCelestials(GuiGraphics gui, int w, int h, int xOff, int yOff, Quaternionf rot,
			double t, double rt) {

		// Configure Render System
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();
		RenderSystem.depthFunc(GL11.GL_LESS);
		RenderSystem.depthMask(true);
		RenderSystem.clearDepth(1.0);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		Matrix4f oldProj = RenderSystem.getProjectionMatrix();
		float panelYOff = yOff + smoothedYOff * gui.guiWidth();
		float halfWidth = 1 / zoom;
		float halfHeight = halfWidth * ((float) gui.guiHeight() / gui.guiWidth()); // full screen aspect ratio
		float shiftX = ((float) xOff / gui.guiWidth()) * 2f * halfWidth;
		float shiftY = ((float) panelYOff / gui.guiHeight()) * 2f * halfHeight;

		Matrix4f newProj = new Matrix4f().frustum(-halfWidth + shiftX, halfWidth + shiftX, -halfHeight - shiftY,
				halfHeight - shiftY, // Y is flipped in screen space
				NEAR_PLANE, FAR_PLANE);
		RenderSystem.setProjectionMatrix(newProj, RenderSystem.getVertexSorting());

		// Apply to model view matrix
		Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.pushMatrix();
		matrixStack.rotate(rot);
		matrixStack.translate(posX, 0, posY);
		RenderSystem.applyModelViewMatrix();

		// Render
		MultiBufferSource.BufferSource vcp = null;
		if (mc != null) {
			vcp = mc.renderBuffers().bufferSource();
		}
		this.queue = renderCelestials(gui, rot, vcp, t, rt);
		if (vcp != null) {
			vcp.endBatch();
		}

		// Reset
		matrixStack.popMatrix();
		RenderSystem.setProjectionMatrix(oldProj, RenderSystem.getVertexSorting());
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(true);

		queue.forEach(r -> CelestialRenderer.drawUIOverlay(r, gui));
	}

	protected List<CelestialUIRenderInfo> renderCelestials(GuiGraphics gui, Quaternionf rot, MultiBufferSource c,
			double t, double rt) {
		List<CelestialUIRenderInfo> renderQueue = new ArrayList<>();
		PoseStack matrices = new PoseStack();

		// Render orbits first (behind celestial bodies)
		for (Planet p : system.planets()) {
			CelestialRenderer.drawOrbit(matrices, p, 0x4000FEFE, zoom, t);
		}

		// Render star
		CelestialRenderInfo starInfo = CelestialRenderInfo.from(-1, system.star(), gui);
		CelestialRenderer.drawStar(matrices, starInfo, t, rt, zoom, renderQueue::add);

		// Render Planets
		for (int i = 0; i < system.planets().size(); i++) {
			Planet p = system.planets().get(i);
			CelestialRenderInfo planetInfo = CelestialRenderInfo.from(i, p, gui);
			CelestialRenderer.drawPlanet(matrices, planetInfo, p, t, rt, posX, posY, zoom, rot, info -> {
				if (tracked != null && info.id() == tracked.id()) {
					this.tracked = info;
				}
				renderQueue.add(info);
			});
		}

		return renderQueue;
	}

	public boolean mouseClicked(double mX, double mY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
			Vector2d mPos = new Vector2d(mX, mY);

			// Find the closest celestial object to the click
			CelestialUIRenderInfo closest = null;
			double closestDist = Double.MAX_VALUE;

			for (CelestialUIRenderInfo info : this.queue) {
				double dist = info.screenPos().distance(mPos);
				if (dist < 25 && dist < closestDist) {
					closest = info;
					closestDist = dist;
				}
			}

			if (closest != null) {
				this.hoverables.clear();
				this.tracked = closest;
				this.trackedOrbitalPos = closest.worldPos();
				this.targetZoom = calculateZoom(closest.celestial().radiusAU());

				final int id = closest.id();
				this.select.forEach(selected -> selected.accept(id));

				MUI.click();
				return true;
			}
		}
		return false;
	}

	public boolean mouseDragged(int button, double dX, double dY, int w, int h) {
		// Rotate - Right Click
		if (button == GLFW.GLFW_MOUSE_BUTTON_2) {

			float rotSpeed = 400;
			float maxYAng = 89.9f;

			this.rotX += (float) dX / (float) h * rotSpeed;
			this.rotY += (float) dY / (float) w * rotSpeed;

			this.rotY = Math.min(this.rotY, maxYAng);
			this.rotY = Math.max(this.rotY, -maxYAng);
		}

		// Pan - Middle Click or Left Click
		if (button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_3) {
			this.tracked = null;
			this.hoverables.clear();

			Quaternionf rot = createRotQuat(rotX, rotY);
			Vector3f right = new Vector3f(VecUtil.XP);
			Vector3f up = new Vector3f(VecUtil.YN);
			rot.transformInverse(right);
			rot.transformInverse(up);

			Vector3f rightXZ = new Vector3f(right.x, 0, right.z);
			Vector3f upXZ = new Vector3f(up.x, 0, up.z);

			float rightLenSq = rightXZ.lengthSquared();
			float upLenSq = upXZ.lengthSquared();
			if (rightLenSq > 0.000001f) {
				rightXZ.div(rightLenSq);
			} else {
				rightXZ.set(0, 0, 0);
			}
			if (upLenSq > 0.000001f) {
				upXZ.div(upLenSq);
			} else {
				upXZ.set(0, 0, 0);
			}

			float panSpeed = 0.5f * maxZoom / zoom;
			this.posX += (rightXZ.x * (float) dX + upXZ.x * (float) dY) * panSpeed;
			this.posY += (rightXZ.z * (float) dX + upXZ.z * (float) dY) * panSpeed;
		}

		return false;
	}

	public boolean mouseScrolled(double delta) {
		this.zoom *= (float) Math.pow(1.1, delta);
		this.targetZoom = zoom;
		return false;
	}

	public void renderTooltip(@NotNull GuiGraphics gui, int mx, int my) {
		for (Hoverable h : hoverables.values()) {
			if (mx > h.minX() && mx < h.maxX() && my > h.minY() && my < h.maxY()) {
				gui.renderTooltip(mc.font, h.text().get(), Optional.empty(), mx, my);
				break;
			}
		}
	}

	public void renderInfoBoxes(GuiGraphics gui, int i, int j) {
		MUI.drawWithScale(gui, 0.5f, t -> {

			// Draw Help
			MUI.blitCommon(gui, t.apply(i + 4f).intValue(), t.apply(j + 4f).intValue(), 448, 160, 16, 16);
			MUI.blitCommon(gui, t.apply(i + 4f).intValue(), t.apply(j + 14f).intValue(), 464, 160, 16, 16);
			MUI.blitCommon(gui, t.apply(i + 4f).intValue(), t.apply(j + 24f).intValue(), 496, 160, 16, 16);

			MUI.drawString(gui, MUI.uistr("rocket.starmap.pan"), t.apply(i + 14f).intValue(),
					t.apply(j + 6f).intValue());
			MUI.drawString(gui, MUI.uistr("rocket.starmap.rotate"), t.apply(i + 14f).intValue(),
					t.apply(j + 16f).intValue());
			MUI.drawString(gui, MUI.uistr("rocket.starmap.zoom"), t.apply(i + 14f).intValue(),
					t.apply(j + 26f).intValue());

			if (tracked != null) {
				Planet planet = (Planet) tracked.celestial();
				RoughTemperatureRange temperatureRange = ServerWeatherSystem.calculateRoughTemperatureRange(planet);
				Component temperatureText = Component.literal(StringUtils.formatTemp(temperatureRange.min()))
						.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))
						.append(Component.literal(" - ").withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1)))
						.append(Component.literal(StringUtils.formatTemp(temperatureRange.max()))
								.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2)));

				// Draw Tracking Box
				MUI.blitRocket(gui, t.apply(i + 56f).intValue(), t.apply(j + 5f).intValue(), 253, 26, 227, 114);

				// Draw Planet Title
				MUI.drawCenteredString(gui, planet.getName(), t.apply(i + 56f).intValue() + 112,
						t.apply(j + 5f).intValue() + 10);
				MUI.blitCommon(gui, t.apply(i + 56f).intValue() + 55, t.apply(j + 5f).intValue() + 22, 308, 245, 115,
						6);

				// Draw Planet Info
				Component c = Component.literal(": ");
				MUI.drawString(gui,
						MUI.uistr("rocket.starmap.planet_type").append(c)
								.append(planet.type().nameComp()
										.withStyle(Style.EMPTY.withBold(true).withColor(planet.type().color()))),
						t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 32);
				MUI.drawString(gui,
						MUI.uistr("rocket.starmap.day_length").append(c)
								.append(Component.literal(StringUtils.formatHours((float) planet.day()))
										.withStyle(Style.EMPTY.withBold(true))),
						t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 42);
				MUI.drawString(gui,
						MUI.uistr("rocket.starmap.temperature").append(c)
								.append(temperatureText),
						t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 52);
				if (planet.gas_giant()) {
					MUI.drawString(gui,
							MUI.uistr("rocket.starmap.gas_giant").append(c).append(StringUtils.formatBool(true)),
							t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 62);
				} else {
					MUI.drawString(gui,
							MUI.uistr("rocket.starmap.gravity").append(c)
									.append(Component.literal(StringUtils.formatGravity((float) planet.surf_grav()))
											.withStyle(Style.EMPTY.withBold(true))),
							t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 62);
				}
				MUI.drawString(gui,
						MUI.uistr("rocket.starmap.breathable_atmosphere").append(c)
								.append(StringUtils.formatBool(planet.breathable())),
						t.apply(i + 56f).intValue() + 4, t.apply(j + 5f).intValue() + 72);

				int k = 0;
				for (PlanetTrait trait : planet.traits()) {
					int traitX = t.apply(i + 56f).intValue() + 4;
					int traitY = t.apply(j + 5f).intValue() + 82 + 10 * k;
					Component traitComp = trait.comp().withStyle(Style.EMPTY.withBold(true).withColor(trait.color()));
					MUI.drawString(gui, traitComp, traitX, traitY);
					int rtx = (int) (traitX * 0.5f);
					int rty = (int) (traitY * 0.5f);
					registerHoverable("trait_" + k, rtx - 1, rty - 1, rtx + mc.font.width(traitComp) / 2 + 1,
							rty + mc.font.lineHeight / 2 + 1, () -> List.of(traitComp, trait.explanationComp()));
					k++;
				}
			}
		});
	}

}
