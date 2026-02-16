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

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(-1, -1, 0).setUv(0, 0);
        buffer.addVertex(1, -1, 0).setUv(1, 0);
        buffer.addVertex(1, 1, 0).setUv(1, 1);
        buffer.addVertex(-1, 1, 0).setUv(0, 1);
        BufferUploader.drawWithShader(buffer.build());

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    }
}
