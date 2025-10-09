package com.machina.client.screen;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.machina.api.client.celestial.CelestialRenderInfo;
import com.machina.api.client.celestial.CelestialRenderer;
import com.machina.api.client.celestial.CelestialUIRenderInfo;
import com.machina.api.client.screen.MUI;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.math.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class StarchartScreen extends Screen {

    final SolarSystem system;

    float rotX = 0;
    float rotY = 90;
    float posX = 0;
    float posY = 0;
    float zoom;
    float orbitalSpeed = 0.01f;

    List<CelestialUIRenderInfo> queue;
    CelestialUIRenderInfo tracked;
    float targetX;
    float targetY;
    float targetZ;
    double accumulatedTime = 0;

    public StarchartScreen(SolarSystem s) {
        super(Component.empty());
        this.system = s;
    }

    @Override
    protected void init() {
        super.init();

        zoom = calculateZoom(system.maxAphelion(), height, 40);
    }

    public float calculateZoom(double targetAphelion, int target, int padding) {
        float max = (float) (targetAphelion * 2);

        return (float) MathUtil.binarySearch(0.001D, 100D, (target - padding) / 2f, zoom -> {
            float z = (float) zoom;
            PoseStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.pushPose();
            matrixStack.translate((double) width / 2, (double) height / 2, 300.0D);
            matrixStack.scale(1.0F, -1.0F, 1.0F);
            matrixStack.scale(32.0F, 32.0F, 32.0F);

            PoseStack matrices = new PoseStack();
            matrices.scale(1.0F, 1.0F, 0.1F);
            matrices.scale(z, z, z);
            matrices.mulPose(createRotQuat(90, 0));
            matrices.pushPose();

            Vector4f spos = new Vector4f(max, max, 0, 1);
            Matrix4f stm = new Matrix4f(matrices.last().pose());
            Matrix4f mvp = new Matrix4f(RenderSystem.getProjectionMatrix()).mul(matrixStack.last().pose());

            stm.transform(spos);
            mvp.transform(spos);

            Vector4f norm = spos.div(spos.w);
            float x = (1.0f + norm.x) * 0.5f * target;
            float y = (1.0f - norm.y) * 0.5f * target;

            matrices.popPose();
            matrixStack.popPose();

            return (double) Math.max(x, y);
        }, 0.01);
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

    @Override
    public void render(@NotNull GuiGraphics gui, int mX, int mY, float partial) {
        MUI.drawStars(gui, 0, 0, width, height);

        accumulatedTime += minecraft.getFrameTime() * orbitalSpeed;
        setupAndRenderCelestials(gui, width / 2, height / 2, createRotQuat(rotX, rotY), accumulatedTime);
    }

    protected void setupAndRenderCelestials(GuiGraphics gui, int x, int y, Quaternionf rot, double t) {
        
        // Move towards target position and zoom:
        if (this.tracked != null) {
            float dx = targetX - this.posX;
            float dy = targetY - this.posY;
            float dz = targetZ - this.zoom;
            this.posX += dx / 20;
            this.posY += dy / 20;
            this.zoom += dz / 20;
        }

        // Configure Render System
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LESS);
        RenderSystem.depthMask(true);
        RenderSystem.clearDepth(1.0);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        // Apply to model view matrix
        PoseStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushPose();
        matrixStack.translate(x, y, 300.0D);
        matrixStack.scale(1.0F, -1.0F, 1.0F);
        matrixStack.scale(32.0F, 32.0F, 32.0F);
        RenderSystem.applyModelViewMatrix();

        // Render
        MultiBufferSource.BufferSource vcp = null;
        if (minecraft != null) {
            vcp = minecraft.renderBuffers().bufferSource();
        }
        this.queue = renderCelestials(gui, rot, vcp, t);
        if (vcp != null) {
            vcp.endBatch();
        }

        // Reset
        matrixStack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(true);

        queue.forEach(r -> CelestialRenderer.drawUIOverlay(r, gui));
    }

    protected List<CelestialUIRenderInfo> renderCelestials(GuiGraphics gui, Quaternionf rot, MultiBufferSource c, double t) {
        List<CelestialUIRenderInfo> renderQueue = new ArrayList<>();
        PoseStack matrices = new PoseStack();
        matrices.scale(1.0F, 1.0F, 0.1F);
        matrices.scale(zoom, zoom, zoom);
        matrices.translate(posX / 32, -posY / 32, 0);
        matrices.mulPose(rot);

        // Render orbits first (behind celestial bodies)
        for (Planet p : system.planets()) {
            CelestialRenderer.drawOrbit(matrices, p, 0x40FFFFFF);
        }

        // Render star
        CelestialRenderInfo starInfo = CelestialRenderInfo.from(system.star(), gui);
        CelestialRenderer.drawStar(matrices, starInfo, t, zoom, renderQueue::add);

        // Render Planets
        for (Planet p : system.planets()) {
            CelestialRenderInfo planetInfo = CelestialRenderInfo.from(p, gui);
            CelestialRenderer.drawPlanet(matrices, planetInfo, p, t, posX, posY, zoom, renderQueue::add);
        }

        return renderQueue;
    }
    
    @Override
    public boolean mouseClicked(double mX, double mY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            Vec2 mPos = new Vec2((float) mX, (float) mY);
            for (CelestialUIRenderInfo info : this.queue) {
                if (info.pos().distanceToSqr(mPos) < 25) {
                    this.tracked = info;
                    float celestialScreenX = info.pos().x;
                    float celestialScreenY = info.pos().y;
                    float centerX = width / 2f;
                    float centerY = height / 2f;
                    float screenOffsetX = centerX - celestialScreenX;
                    float screenOffsetY = centerY - celestialScreenY;
                    int targetSize = Math.min(width, height);
                    double celestialRadius = info.celestial().radiusAU();
                    this.targetX = this.posX + (screenOffsetX / this.zoom);
                    this.targetY = this.posY + (screenOffsetY / this.zoom);
                    this.targetZ = calculateZoom(celestialRadius, targetSize, 100);
                    MUI.click();
                    return true;
                }
            }
        }
        return super.mouseClicked(mX, mY, button);
    }

    @Override
    public boolean mouseDragged(double mX, double mY, int button, double dX, double dY) {
        // Rotate - Right Click
        if (button == GLFW.GLFW_MOUSE_BUTTON_2) {

            float rotSpeed = 100;
            float maxYAng = 89.9f;

            this.rotX += (float) dX / (float) height * rotSpeed;
            this.rotY += (float) dY / (float) width * rotSpeed;

            this.rotY = Math.min(this.rotY, maxYAng);
            this.rotY = Math.max(this.rotY, -maxYAng);
        }

        // Pan - Middle Click or Left Click
        if (button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_3) {
            this.tracked = null;
            this.posX += (float) (dX / this.zoom);
            this.posY += (float) (dY / this.zoom);
        }

        return super.mouseDragged(mX, mY, button, dX, dY);
    }

    @Override
    public boolean mouseScrolled(double mX, double mY, double delta) {
        this.tracked = null;
        this.zoom *= (float) Math.pow(1.1, delta);
        return super.mouseScrolled(mX, mY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
