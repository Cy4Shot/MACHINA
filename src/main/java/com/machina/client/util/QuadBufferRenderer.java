package com.machina.client.util;

import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.machina.util.math.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuadBufferRenderer {
	public static final float[][] CUBE_VERTICES = new float[][] {
			// NORTH
			{ -1f, 1f, -1f }, { -1f, -1f, -1f }, { 1f, -1f, -1f }, { 1f, 1f, -1f },

			// EAST
			{ 1f, 1f, -1f }, { 1f, -1f, -1f }, { 1f, -1f, 1f }, { 1f, 1f, 1f },

			// WEST
			{ -1f, -1f, 1f }, { -1f, -1f, -1f }, { -1f, 1f, -1f }, { -1f, 1f, 1f },

			// TOP
			{ 1f, 1f, -1f }, { 1f, 1f, 1f }, { -1f, 1f, 1f }, { -1f, 1f, -1f },

			// SOUTH
			{ 1f, 1f, 1f }, { 1f, -1f, 1f }, { -1f, -1f, 1f }, { -1f, 1f, 1f },

			// BOTTOM
			{ -1f, -1f, 1f }, { 1f, -1f, 1f }, { 1f, -1f, -1f }, { -1f, -1f, -1f },

	};

	public static VertexBuffer create(VertexBuffer b, Consumer<BufferBuilder> r) {
		BufferBuilder bb = Tessellator.getInstance().getBuilder();
		b = new VertexBuffer(DefaultVertexFormats.POSITION_TEX);
		r.accept(bb);
		bb.end();
		b.upload(bb);
		return b;
	}

	public static void cube(BufferBuilder buffer, float radius) {
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		for (int i = 0; i < CUBE_VERTICES.length; i++) {
			buffer.vertex(CUBE_VERTICES[i][0] * radius, CUBE_VERTICES[i][1] * radius, CUBE_VERTICES[i][2] * radius)
					.uv(Math.floorDiv(i, 2) % 2 == 0 ? 0 : 1, Math.floorDiv(i + 1, 2) % 2 == 0 ? 0 : 1).endVertex();
		}
	}

	public static void cylinder(BufferBuilder buffer, int segments, double height, double radius) {
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		for (int i = 0; i < segments; i++) {
			double a1 = i * MathUtil.TWO_PI / segments;
			double a2 = (i + 1) * MathUtil.TWO_PI / segments;
			double px1 = Math.sin(a1) * radius;
			double pz1 = Math.cos(a1) * radius;
			double px2 = Math.sin(a2) * radius;
			double pz2 = Math.cos(a2) * radius;

			float u0 = (float) i / (float) segments;
			float u1 = (float) (i + 1) / (float) segments;

			buffer.vertex(px1, -height, pz1).uv(u0, 0).endVertex();
			buffer.vertex(px1, height, pz1).uv(u0, 1).endVertex();
			buffer.vertex(px2, height, pz2).uv(u1, 1).endVertex();
			buffer.vertex(px2, -height, pz2).uv(u1, 0).endVertex();
		}
	}

	public static void render(MatrixStack s, VertexBuffer vb, VertexFormat f, float r, float g, float b, float a) {
		RenderSystem.color4f(r, g, b, a);
		vb.bind();
		f.setupBufferState(0L);
		vb.draw(s.last().pose(), GL11.GL_QUADS);
		VertexBuffer.unbind();
		f.clearBufferState();
	}
}
