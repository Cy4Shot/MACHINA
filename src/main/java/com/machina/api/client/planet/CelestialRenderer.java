package com.machina.api.client.planet;

import com.machina.api.client.screen.MUI;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.math.MathUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class CelestialRenderer {

    private static final float UI_OVERLAY_MAX_THRESHOLD = 0.03f;
    private static final float UI_OVERLAY_MIN_THRESHOLD = 0.002f;
    private static final int SPHERE_SEGMENTS = 16;

    public static void drawStar(PoseStack matrices, CelestialRenderInfo starInfo, double time, float zoom, Consumer<CelestialDeferredUI> enqueue) {
        Vec3 pos = starInfo.getOrbitalCoords(time);

        matrices.pushPose();
        matrices.translate((float) pos.x, (float) pos.y, (float) pos.z);

        Vector4f sp = asScreenPos(matrices);

        if (zoom > UI_OVERLAY_MAX_THRESHOLD / starInfo.radius()) {
            drawSphere(matrices, starInfo.bg(), (float) starInfo.radius(), 0xFFFF0000);
        } else {
            enqueue.accept(CelestialRenderer.createUIOverlay(asUIPos(sp, starInfo.width(), starInfo.height())));
        }

        matrices.popPose();
    }

    public static void drawPlanet(PoseStack matrices, CelestialRenderInfo planetInfo, Planet planet, double time, float zoom, Consumer<CelestialDeferredUI> enqueue) {
        Vec3 pos = planetInfo.getOrbitalCoords(time);

        matrices.pushPose();
        matrices.translate((float) pos.x, (float) pos.y, (float) pos.z);

        Vector4f sp = asScreenPos(matrices);
        float sqDist = MathUtil.dot(sp.x, sp.y, sp.x, sp.y);

        if (zoom > UI_OVERLAY_MAX_THRESHOLD / planetInfo.radius()) {
            drawSphere(matrices, planetInfo.bg(), (float) planetInfo.radius(), getPlanetColor(planet));
        } else if (sqDist > UI_OVERLAY_MIN_THRESHOLD) {
            enqueue.accept(CelestialRenderer.createUIOverlay(asUIPos(sp, planetInfo.width(), planetInfo.height())));
        }

        matrices.popPose();
    }

    public static void drawOrbit(PoseStack matrices, Planet planet, int color) {
        if (planet.e() == 0 && planet.a() == 0) return; // Skip invalid orbits

        matrices.pushPose();

        // Generate orbit ellipse points
        float semiMajor = (float) planet.a();
        float eccentricity = (float) planet.e();
        double[] orbitPoints = MUI.keplerianOrbit(0, 0, semiMajor, eccentricity, 64);

        // Draw orbit lines
        drawLines(matrices, orbitPoints, color);

        matrices.popPose();
    }


    private static ResourceLocation getCelestialTexture(String name) {
        return new MachinaRL("textures/gui/starchart/" + name + ".png");
    }

    private static void drawSphere(PoseStack matrices, String texName, float radius, int color) {
        RenderSystem.setShaderTexture(0, getCelestialTexture(texName));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LESS);

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

    private static void addSphereVertex(BufferBuilder buffer, Matrix4f pose, float radius, float theta, float phi, float r, float g, float b, float a) {
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.cos(theta));
        float z = (float) (radius * Math.sin(theta) * Math.sin(phi));

        buffer.vertex(pose, x, y, z)
                .color(r, g, b, a)
                .endVertex();
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

    private static CelestialDeferredUI createUIOverlay(Vec2 pos) {
        return (GuiGraphics gui, int centerX, int centerY) -> {
            int screenX = (int) pos.x;
            int screenY = (int) pos.y;

            // Only draw if on screen
            if (screenX >= -10 && screenX <= gui.guiWidth() + 10 &&
                    screenY >= -10 && screenY <= gui.guiHeight() + 10) {
                gui.fill(screenX - 6, screenY - 6, screenX + 6, screenY + 6, 0x80000000);
                MUI.drawCenteredString(gui, Component.literal("X"), screenX, screenY - 4, 0xFFFFFF);
            }
        };
    }

    private static void drawLines(PoseStack matrices, double[] lines, int color) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        Matrix4f pose = matrices.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < lines.length; i += 2) {
            buffer.vertex(pose, (float) lines[i], 0, (float) lines[i + 1])
                    .color(r, g, b, a)
                    .endVertex();
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferUploader.drawWithShader(buffer.end());
    }

    private static int getPlanetColor(Planet planet) {
        // Color planets based on their classification or type
        char planClass = planet.plan_class();
        return switch (planClass) {
            case 'M' -> 0xFF4169E1; // Earth-like - blue
            case 'V' -> 0xFFFF6347; // Venus-like - orange-red
            case 'J' -> 0xFFDEB887; // Jovian - tan/brown
            case 'I' -> 0xFF87CEEB; // Ice world - light blue
            case 'R' -> 0xFF8B4513; // Rock world - brown
            case 'G' -> 0xFF9ACD32; // Greenhouse - yellow-green
            default -> 0xFFB0B0B0;  // Unknown - gray
        };
    }
}
