package com.machina.api.client.celestial;

import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;

import com.machina.api.client.shader.ShaderHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;

public class LensFlareRenderer {
    public static void drawLensFlare(int width, int height, Vector2d screenPos, float intensity) {
        float aspect = (float) width / height;

        ShaderInstance flare = ShaderHandler.FLARE.instance();
        flare.apply();
        flare.safeGetUniform("lightPos").set((float) screenPos.x / width, (float) screenPos.y / height);
        flare.safeGetUniform("intensity").set(intensity);
        flare.safeGetUniform("aspect").set(aspect);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.setShader(() -> flare);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(-1, -1, 0).uv(0, 0).endVertex();
        buffer.vertex(1, -1, 0).uv(1, 0).endVertex();
        buffer.vertex(1, 1, 0).uv(1, 1).endVertex();
        buffer.vertex(-1, 1, 0).uv(0, 1).endVertex();
        BufferUploader.drawWithShader(buffer.end());

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    }
}
