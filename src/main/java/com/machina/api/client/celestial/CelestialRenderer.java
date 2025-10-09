package com.machina.api.client.celestial;

import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class CelestialRenderer {

    private static final float UI_GLOW_MAX_THRESHOLD = 0.1f;
    private static final float UI_OVERLAY_MAX_THRESHOLD = 0.005f;
    private static final float UI_OVERLAY_MIN_THRESHOLD = 0.002f;
    private static final int SPHERE_SEGMENTS = 16;

    public static void drawUIOverlay(CelestialUIRenderInfo renderinfo, GuiGraphics gui) {
        int screenX = (int) renderinfo.pos().x;
        int screenY = (int) renderinfo.pos().y;

        // Only draw if on screen
        if (screenX >= -10 && screenX <= gui.guiWidth() + 10 && screenY >= -10 && screenY <= gui.guiHeight() + 10) {
//            gui.fill(screenX - 1, screenY - 1, screenX + 1, screenY + 1, 0xFFFFFFFF);
        }
    }

    public static void drawStar(PoseStack matrices, CelestialRenderInfo info, double time, float zoom,
            Consumer<CelestialUIRenderInfo> enqueue) {
        Vec3 pos = info.getOrbitalCoords(time);

        matrices.pushPose();
        matrices.translate((float) pos.x, (float) pos.y, (float) pos.z);

        drawSphere(matrices, info.celestial().texture_bg(), (float) info.radius(), 0xFFFFFFFF);
        float threshold = (float) (UI_GLOW_MAX_THRESHOLD / info.radius());
        if (zoom < threshold) {
            float glowAlpha = MathUtil.clamp((threshold - zoom) / threshold, 0f, 1f);
            glowAlpha = (float) Math.pow(glowAlpha, 0.25);
            int color = 0xFFFFFF | ((int) (glowAlpha * 255) << 24);
            drawBillboard(matrices, getCelestialTexture("glow"),
                    (float) info.radius() * (float) Math.sqrt(zoom) * 100f, color);
        }

        matrices.popPose();
    }

    public static void drawPlanet(PoseStack matrices, CelestialRenderInfo info, Planet planet, double time, float px,
            float py, float zoom, Consumer<CelestialUIRenderInfo> enqueue) {
        Vec3 pos = info.getOrbitalCoords(time);

        matrices.pushPose();
        matrices.translate((float) pos.x, (float) pos.y, (float) pos.z);

        Vector4f sp = asScreenPos(matrices);
        float sqDist = MathUtil.sqDist(sp.x, sp.y, px, py);

        if (zoom > UI_OVERLAY_MAX_THRESHOLD / info.radius()) {
            int col = getPlanetColor(planet);
            drawSphere(matrices, info.celestial().texture_bg(), (float) info.radius(), col);
            float threshold = (float) (UI_GLOW_MAX_THRESHOLD / info.radius());
            if (zoom < threshold) {
                float glowAlpha = MathUtil.clamp((threshold - zoom) / threshold, 0f, 1f);
                glowAlpha = (float) Math.pow(glowAlpha, 0.25);
                int color = 0xFFFFFF | ((int) (glowAlpha * 255) << 24);
                drawBillboard(matrices, getCelestialTexture("glow"),
                        (float) info.radius() * (float) Math.sqrt(zoom) * 100f, color);
            }
        } else if (sqDist > UI_OVERLAY_MIN_THRESHOLD) {
            enqueue.accept(CelestialUIRenderInfo.from(info, asUIPos(sp, info.width(), info.height())));
        }

        matrices.popPose();
    }

    public static void drawOrbit(PoseStack matrices, Planet planet, int color, double time) {
        if (planet.e() == 0 && planet.a() == 0)
            return;

        matrices.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        
        int trailSamples = 32;
        float br = ((color >> 16) & 0xFF) / 255.0f;
        float bg = ((color >> 8) & 0xFF) / 255.0f;
        float bb = (color & 0xFF) / 255.0f;

        Matrix4f pose = matrices.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        
        double n = 2 * Math.PI / planet.orb_period();
        double M0 = n * time + planet.where_in_orbit();
        double deltaM = -Math.PI / trailSamples;
        for (int s = 0; s < trailSamples; s++) {
            double M1 = M0 + deltaM * s;
            double M2 = M0 + deltaM * (s + 1);
            double theta1 = planet.trueAnomalyFromMean(M1, planet.e());
            double theta2 = planet.trueAnomalyFromMean(M2, planet.e());
            double x1 = planet.a() * (Math.cos(theta1) - planet.e());
            double z1 = planet.a() * Math.sqrt(1 - planet.e() * planet.e()) * Math.sin(theta1);
            double x2 = planet.a() * (Math.cos(theta2) - planet.e());
            double z2 = planet.a() * Math.sqrt(1 - planet.e() * planet.e()) * Math.sin(theta2);

            float t = (float) s / (trailSamples - 1);
            float alpha = (float) Math.pow(1.0f - t, 1.5f);

            buffer.vertex(pose, (float) x1, 0f, (float) z1).color(br, bg, bb, alpha).endVertex();
            buffer.vertex(pose, (float) x2, 0f, (float) z2).color(br, bg, bb, alpha * 0.8f).endVertex();
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
        return new MachinaRL("textures/gui/starchart/" + name + ".png");
    }

    private static void drawSphere(PoseStack matrices, String texName, float radius, int color) {
        RenderSystem.setShaderTexture(0, getCelestialTexture(texName));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LESS);
        RenderSystem.depthMask(true);

        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = 1.0f; // Force full alpha

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        Matrix4f pose = matrices.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        // Generate sphere vertices
        for (int i = 0; i < SPHERE_SEGMENTS; i++) {
            for (int j = 0; j < SPHERE_SEGMENTS; j++) {
                // Calculate sphere coordinates
                float theta1 = (float) (i * Math.PI / SPHERE_SEGMENTS);
                float theta2 = (float) ((i + 1) * Math.PI / SPHERE_SEGMENTS);
                float phi1 = (float) (j * 2 * Math.PI / SPHERE_SEGMENTS);
                float phi2 = (float) ((j + 1) * 2 * Math.PI / SPHERE_SEGMENTS);

                // First triangle
                addSphereVertex(buffer, pose, radius, theta1, phi1, r, g, b, a);
                addSphereVertex(buffer, pose, radius, theta2, phi1, r, g, b, a);
                addSphereVertex(buffer, pose, radius, theta1, phi2, r, g, b, a);

                // Second triangle
                addSphereVertex(buffer, pose, radius, theta2, phi1, r, g, b, a);
                addSphereVertex(buffer, pose, radius, theta2, phi2, r, g, b, a);
                addSphereVertex(buffer, pose, radius, theta1, phi2, r, g, b, a);
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferUploader.drawWithShader(buffer.end());

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void addSphereVertex(BufferBuilder buffer, Matrix4f pose, float radius, float theta, float phi,
            float r, float g, float b, float a) {
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.cos(theta));
        float z = (float) (radius * Math.sin(theta) * Math.sin(phi));

        buffer.vertex(pose, x, y, z).color(r, g, b, a).endVertex();
    }

    private static Vector4f asScreenPos(PoseStack stack) {
        Vector4f spos = new Vector4f(0, 0, 0, 1);
        Matrix4f stm = new Matrix4f(stack.last().pose());
        Matrix4f mvp = new Matrix4f(RenderSystem.getProjectionMatrix()).mul(RenderSystem.getModelViewMatrix());

        stm.transform(spos);
        mvp.transform(spos);
        return spos.div(spos.w);
    }

    private static Vec2 asUIPos(Vector4f screenPos, int w, int h) {
        float x = (1.0f + screenPos.x) * 0.5f * w;
        float y = (1.0f - screenPos.y) * 0.5f * h;
        return new Vec2(x, y);
    }

    private static int getPlanetColor(Planet planet) {
        return switch (planet.plan_class()) {
        case 'M' -> 0xFF4169E1;
        case 'V' -> 0xFFFF6347;
        case 'J' -> 0xFFDEB887;
        case 'I' -> 0xFF87CEEB;
        case 'R' -> 0xFF8B4513;
        case 'G' -> 0xFF9ACD32;
        default -> 0xFFB0B0B0;
        };
    }
}
