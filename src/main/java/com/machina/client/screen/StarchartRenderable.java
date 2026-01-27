package com.machina.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.machina.api.client.celestial.CelestialRenderInfo;
import com.machina.api.client.celestial.CelestialRenderer;
import com.machina.api.client.celestial.CelestialUIRenderInfo;
import com.machina.api.client.screen.MUI;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.math.VecUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class StarchartRenderable {

    private final static Minecraft mc = Minecraft.getInstance();
    private final static float NEAR_PLANE = 0.005f;
    private final static float FAR_PLANE = 500000000f;

    final SolarSystem system;
    final boolean paused;

    private final float maxZoom;

    private List<Consumer<Integer>> select;
    
    private float rotX = 0;
    private float rotY = 90;
    private float posX = 0;
    private float posY = 0;
    private float orbitalSpeed = 0.01f;
    private float zoom;
    private List<CelestialUIRenderInfo> queue;
    private CelestialUIRenderInfo tracked;

    private Vec3 trackedOrbitalPos;
    private float targetZoom;
    private float smoothing = 0.05f;

    private double realTime = 0;
    private double accumulatedTime = 0;

    public StarchartRenderable(SolarSystem s, boolean paused) {
        this.system = s;
        this.paused = paused;

        this.maxZoom = calculateZoom(system.maxAphelion());
        this.zoom = maxZoom;
        
        this.select = new ArrayList<>();
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

    public void render(@NotNull GuiGraphics gui, int x, int y, int xOff, int yOff, int width, int height) {
        MUI.drawStars(gui, x, y, width, height);
        
        // TODO: Move the entire inner display by this many pixels!

        if (!this.paused && this.tracked == null) {
            accumulatedTime += mc.getFrameTime() * orbitalSpeed;
        }
        realTime += mc.getFrameTime();
        updateCameraTracking();
        setupAndRenderCelestials(gui, width, height, createRotQuat(rotX, rotY), accumulatedTime, realTime);
    }

    protected void updateCameraTracking() {
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

    protected void setupAndRenderCelestials(GuiGraphics gui, int w, int h, Quaternionf rot, double t,
            double rt) {

        // Configure Render System
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LESS);
        RenderSystem.depthMask(true);
        RenderSystem.clearDepth(1.0);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        Matrix4f oldProj = RenderSystem.getProjectionMatrix();
        float halfWidth = 1 / zoom;
        float halfHeight = halfWidth * ((float) h / w);
        Matrix4f newProj = new Matrix4f().frustum(-halfWidth, halfWidth, -halfHeight, halfHeight, NEAR_PLANE,
                FAR_PLANE);
        RenderSystem.setProjectionMatrix(newProj, RenderSystem.getVertexSorting());

        // Apply to model view matrix
        PoseStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushPose();
        matrixStack.mulPose(rot);
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
        matrixStack.popPose();
        RenderSystem.setProjectionMatrix(oldProj, RenderSystem.getVertexSorting());
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(true);

        queue.forEach(r -> CelestialRenderer.drawUIOverlay(r, gui));
    }

    protected List<CelestialUIRenderInfo> renderCelestials(GuiGraphics gui, Quaternionf rot, MultiBufferSource c, double t, double rt) {
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
            CelestialRenderer.drawPlanet(matrices, planetInfo, p, t, rt, posX, posY, zoom, renderQueue::add);
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

}
