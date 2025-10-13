package com.machina.api.client.celestial;

import java.util.function.Consumer;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector4d;
import org.lwjgl.opengl.GL11;

import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.math.MathUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class CelestialRenderer {

    private static record GizmosFade(float minZoom, float maxZoom, float fadeFactor) {
        private float getAlpha(float zoom) {
            float alpha;
            float peakStart = minZoom * fadeFactor;
            float peakEnd = maxZoom / fadeFactor;
            if (zoom <= minZoom) {
                alpha = 0f;
            } else if (zoom < peakStart) {
                float t = (zoom - minZoom) / (peakStart - minZoom);
                alpha = MathUtil.clamp(t, 0f, 1f);
                alpha = (float) Math.pow(alpha, 0.25);
            } else if (zoom <= peakEnd) {
                alpha = 1f;
            } else if (zoom < maxZoom) {
                float t = (maxZoom - zoom) / (maxZoom - peakEnd);
                alpha = MathUtil.clamp(t, 0f, 1f);
                alpha = (float) Math.pow(alpha, 0.25);
            } else {
                alpha = 0f;
            }
            return alpha;
        };
    }

    private static final GizmosFade UI_OVERLAY_FADE = new GizmosFade(2500f, 1e8f, 5f);
    private static final GizmosFade ORBIT_FADE = new GizmosFade(1f, 1e10f, 50f);

    private static final int SPHERE_SEGMENTS_L0 = 32;
    private static final int SPHERE_SEGMENTS_L1 = 8;
    private static final int SPHERE_SEGMENTS_L2 = 4;
    private static final int SPHERE_THRESHOLD_L1 = (int) 5e6;
    private static final int SPHERE_THRESHOLD_L2 = (int) 1e6;

    public static void drawUIOverlay(CelestialUIRenderInfo renderinfo, GuiGraphics gui) {
        if (renderinfo.markerAlpha() < 0.01f) {
            return;
        }

        int screenX = (int) renderinfo.screenPos().x;
        int screenY = (int) renderinfo.screenPos().y;
        if (screenX >= -10 && screenX <= gui.guiWidth() + 10 && screenY >= -10 && screenY <= gui.guiHeight() + 10) {
            RenderSystem.setShaderColor(1f, 1f, 1f, renderinfo.markerAlpha());
            gui.fill(screenX - 1, screenY - 1, screenX + 1, screenY + 1, 0xFFFFFFFF);
        }
    }

    public static void drawStar(PoseStack matrices, CelestialRenderInfo info, double time, double rt, float zoom,
            Consumer<CelestialUIRenderInfo> enqueue) {
        Vec3 pos = info.getOrbitalCoords(time);

        matrices.pushPose();
        matrices.translate((float) pos.x, (float) pos.y, (float) pos.z);

        drawSphere(matrices, info.celestial().texture_bg(), (float) info.radius(), 0xFFFFFFFF, zoom, rt, 0.005f);

        float flareThreshold = (float) (UI_OVERLAY_FADE.minZoom());
        float logZoom = (float) Math.log(zoom);
        float logThreshold = (float) Math.log(flareThreshold);
        float t = MathUtil.clamp((logZoom - logThreshold) / 4f, 0f, 1f);
        float flareIntensity = t * t * (3f - 2f * t);
        LensFlareRenderer.drawLensFlare(info.width(), info.height(), asScreenPos(matrices, info),
                flareIntensity * 0.77f);
        matrices.popPose();
    }

    public static void drawPlanet(PoseStack matrices, CelestialRenderInfo info, Planet planet, double time, double rt,
            float px, float py, float zoom, Consumer<CelestialUIRenderInfo> enqueue) {
        Vec3 pos = info.getOrbitalCoords(time);

        matrices.pushPose();
        matrices.translate((float) pos.x, (float) pos.y, (float) pos.z);

        if (zoom > UI_OVERLAY_FADE.maxZoom()) {
            drawSphere(matrices, info.celestial().texture_bg(), (float) info.radius(), 0xFFFFFFFF, zoom, rt, 0);
        }
        enqueue.accept(
                CelestialUIRenderInfo.from(info, asScreenPos(matrices, info), pos, UI_OVERLAY_FADE.getAlpha(zoom)));
        matrices.popPose();
    }

    public static void drawOrbit(PoseStack matrices, Planet planet, int color, float zoom, double time) {
        if (planet.e() == 0 && planet.a() == 0)
            return;

        matrices.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        int trailSamples = 32;
        float ba = ORBIT_FADE.getAlpha(zoom);
        float br = ((color >> 16) & 0xFF) / 255.0f;
        float bg = ((color >> 8) & 0xFF) / 255.0f;
        float bb = (color & 0xFF) / 255.0f;

        Matrix4f pose = matrices.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        double n = 2 * Math.PI / planet.orb_period();
        double M0 = n * time + planet.where_in_orbit();
        double deltaM = ba * -Math.PI / trailSamples;
        for (int s = 0; s < trailSamples; s++) {
            double M1 = M0 + deltaM * s;
            double M2 = M0 + deltaM * (s + 1);
            double theta1 = planet.trueAnomalyFromMean(M1, planet.e());
            double theta2 = planet.trueAnomalyFromMean(M2, planet.e());
            double x1 = planet.a() * (Math.cos(theta1) - planet.e());
            double z1 = planet.a() * Math.sqrt(1 - planet.e() * planet.e()) * Math.sin(theta1);
            double x2 = planet.a() * (Math.cos(theta2) - planet.e());
            double z2 = planet.a() * Math.sqrt(1 - planet.e() * planet.e()) * Math.sin(theta2);
            float alpha1 = (float) Math.pow(1f - (float) s / (trailSamples - 1), 1.5f) * ba;
            float alpha2 = (float) Math.pow(1f - (float) (s + 1) / (trailSamples - 1), 1.5f) * ba;

            buffer.vertex(pose, (float) x1, 0f, (float) z1).color(br, bg, bb, alpha1).endVertex();
            buffer.vertex(pose, (float) x2, 0f, (float) z2).color(br, bg, bb, alpha2).endVertex();
        }

        BufferUploader.drawWithShader(buffer.end());
        matrices.popPose();

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    private static void drawBillboard(PoseStack matrices, ResourceLocation texture, float size, int color) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        matrices.pushPose();
        Matrix4f pose = matrices.last().pose();
        Matrix4f billboardPose = new Matrix4f(pose);
        billboardPose.m00(1);
        billboardPose.m01(0);
        billboardPose.m02(0);
        billboardPose.m10(0);
        billboardPose.m11(1);
        billboardPose.m12(0);
        billboardPose.m20(0);
        billboardPose.m21(0);
        billboardPose.m22(1);
        billboardPose.scale(size);

        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(billboardPose, -0.5f, -0.5f, 0).uv(0, 1).color(r, g, b, a).endVertex();
        buffer.vertex(billboardPose, 0.5f, -0.5f, 0).uv(1, 1).color(r, g, b, a).endVertex();
        buffer.vertex(billboardPose, 0.5f, 0.5f, 0).uv(1, 0).color(r, g, b, a).endVertex();
        buffer.vertex(billboardPose, -0.5f, 0.5f, 0).uv(0, 0).color(r, g, b, a).endVertex();
        BufferUploader.drawWithShader(buffer.end());
        matrices.popPose();

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    private static ResourceLocation getCelestialTexture(String name) {
        return new MachinaRL("textures/celestial/" + name + ".png");
    }

    private static void drawSphere(PoseStack matrices, String texName, float radius, int color, float zoom, double time,
            float deformStrength) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LESS);
        RenderSystem.depthMask(true);
        RenderSystem.setShaderTexture(0, getCelestialTexture(texName));
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        Matrix4f pose = matrices.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR_TEX);

        // LOD
        int segments;
        if (zoom < SPHERE_THRESHOLD_L2) {
            segments = SPHERE_SEGMENTS_L2;
        } else if (zoom < SPHERE_THRESHOLD_L1) {
            segments = SPHERE_SEGMENTS_L1;
        } else {
            segments = SPHERE_SEGMENTS_L0;
        }

        // Col
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = 1.0f;

        for (int i = 0; i < segments; i++) {
            for (int j = 0; j < segments; j++) {
                float theta1 = (float) (i * Math.PI / segments);
                float theta2 = (float) ((i + 1) * Math.PI / segments);
                float phi1 = (float) (j * 2 * Math.PI / segments);
                float phi2 = (float) ((j + 1) * 2 * Math.PI / segments);

                float u1 = (float) j / segments;
                float u2 = (float) (j + 1) / segments;
                float v1 = (float) i / segments;
                float v2 = (float) (i + 1) / segments;

                // First triangle
                addSphereVertex(buffer, pose, radius, theta1, phi1, u1, v1, r, g, b, a, time, deformStrength);
                addSphereVertex(buffer, pose, radius, theta2, phi1, u1, v2, r, g, b, a, time, deformStrength);
                addSphereVertex(buffer, pose, radius, theta1, phi2, u2, v1, r, g, b, a, time, deformStrength);

                // Second triangle
                addSphereVertex(buffer, pose, radius, theta2, phi1, u1, v2, r, g, b, a, time, deformStrength);
                addSphereVertex(buffer, pose, radius, theta2, phi2, u2, v2, r, g, b, a, time, deformStrength);
                addSphereVertex(buffer, pose, radius, theta1, phi2, u2, v1, r, g, b, a, time, deformStrength);
            }
        }

        BufferUploader.drawWithShader(buffer.end());
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void addSphereVertex(BufferBuilder buffer, Matrix4f pose, float radius, float theta, float phi,
            float u, float v, float r, float g, float b, float a, double time, float deformStrength) {
        float deform = (float) Math.sin(theta * 3 + phi * 2 + time / 50D) * deformStrength;
        float dynamicRadius = radius * (1.0f + deform);

        float x = (float) (dynamicRadius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (dynamicRadius * Math.cos(theta));
        float z = (float) (dynamicRadius * Math.sin(theta) * Math.sin(phi));

        buffer.vertex(pose, x, y, z).color(r, g, b, a).uv(u, v).endVertex();
    }

    private static Vector2d asScreenPos(PoseStack stack, CelestialRenderInfo info) {
        Vector4d spos = new Vector4d(0, 0, 0, 1);
        Matrix4d stm = new Matrix4d(stack.last().pose());
        Matrix4d mvp = new Matrix4d(RenderSystem.getProjectionMatrix())
                .mul(new Matrix4d(RenderSystem.getModelViewMatrix()));

        stm.transform(spos);
        mvp.transform(spos);
        Vector4d ndc = spos.div(spos.w);

        double screenX = (ndc.x * 0.5 + 0.5) * info.width();
        double screenY = (1.0 - (ndc.y * 0.5 + 0.5)) * info.height();
        return new Vector2d(screenX, screenY);
    }
}
