package com.machina.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.machina.api.util.MachinaRL;
import com.mojang.math.Transformation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

public class StarModel implements IDynamicBakedModel {

	private final ResourceLocation bg = new MachinaRL("gui/starchart/star_bg");

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
			@NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
		if (side != null) {
			return Collections.emptyList();
		}

		TextureAtlasSprite back = getTexture(bg);
		List<BakedQuad> quads = new ArrayList<>();
		Transformation rotation = Transformation.identity();
//		float l = 0;
//		float r = 1;
//		float p = 1;
//
//		
//		quads.add(createQuad(v(r, p, r), v(r, p, l), v(l, p, l), v(l, p, r), rotation, b));
//		quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), rotation, b));
//		quads.add(createQuad(v(r, p, r), v(r, l, r), v(r, l, l), v(r, p, l), rotation, b));
//		quads.add(createQuad(v(l, p, l), v(l, l, l), v(l, l, r), v(l, p, r), rotation, b));
//		quads.add(createQuad(v(r, p, l), v(r, l, l), v(l, l, l), v(l, p, l), rotation, b));
//		quads.add(createQuad(v(l, p, r), v(l, l, r), v(r, l, r), v(r, p, r), rotation, b));

		int numSides = 30;
		float radius = 1.0f;

		for (int phi = 0; phi < numSides; phi++) {
			for (int theta = 0; theta < numSides; theta++) {
				float x0 = (float) Math.cos(2 * Math.PI * phi / numSides)
						* (float) Math.sin(Math.PI * theta / numSides);
				float y0 = (float) Math.sin(2 * Math.PI * phi / numSides)
						* (float) Math.sin(Math.PI * theta / numSides);
				float z0 = (float) Math.cos(Math.PI * theta / numSides);

				float x1 = (float) Math.cos(2 * Math.PI * (phi + 1) / numSides)
						* (float) Math.sin(Math.PI * theta / numSides);
				float y1 = (float) Math.sin(2 * Math.PI * (phi + 1) / numSides)
						* (float) Math.sin(Math.PI * theta / numSides);
				float z1 = (float) Math.cos(Math.PI * theta / numSides);

				float x2 = (float) Math.cos(2 * Math.PI * (phi + 1) / numSides)
						* (float) Math.sin(Math.PI * (theta + 1) / numSides);
				float y2 = (float) Math.sin(2 * Math.PI * (phi + 1) / numSides)
						* (float) Math.sin(Math.PI * (theta + 1) / numSides);
				float z2 = (float) Math.cos(Math.PI * (theta + 1) / numSides);

				float x3 = (float) Math.cos(2 * Math.PI * phi / numSides)
						* (float) Math.sin(Math.PI * (theta + 1) / numSides);
				float y3 = (float) Math.sin(2 * Math.PI * phi / numSides)
						* (float) Math.sin(Math.PI * (theta + 1) / numSides);
				float z3 = (float) Math.cos(Math.PI * (theta + 1) / numSides);

				Vector3f v0 = v(x0 * radius, y0 * radius, z0 * radius);
				Vector3f v1 = v(x1 * radius, y1 * radius, z1 * radius);
				Vector3f v2 = v(x2 * radius, y2 * radius, z2 * radius);
				Vector3f v3 = v(x3 * radius, y3 * radius, z3 * radius);

				float uvu0 = (float) phi / (numSides - 1);
                float uvu1 = (float) (phi + 1) / (numSides - 1);
                float uvv0 = (float) theta / (numSides - 1);
                float uvv1 = (float) (theta + 1) / (numSides - 1);

				quads.add(createQuad(v0, v1, v2, v3, rotation, back, v(x0, y0, z0), uvu0, uvu1, uvv0, uvv1));
			}
		}

		return quads;
	}

	Vector3f calculateSpherePoint(float radius, float theta, float phi) {
		float x = radius * (float) Math.sin(phi) * (float) Math.cos(theta);
		float y = radius * (float) Math.sin(phi) * (float) Math.sin(theta);
		float z = radius * (float) Math.cos(phi);

		return new Vector3f(x, y, z);
	}

	Vector3f calculateQuadNormal(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4) {
		Vector3f edge1 = v2.sub(v1);
		Vector3f edge2 = v3.sub(v2);
		return edge1.cross(edge2).normalize();
	}

	private static void putVertex(QuadBakingVertexConsumer builder, Vector3f normal, Vector4f vector, float u, float v,
			TextureAtlasSprite sprite) {
		builder.vertex(vector.x(), vector.y(), vector.z()).color(1.0f, 1.0f, 1.0f, 1.0f)
				.uv(sprite.getU(u), sprite.getV(v)).uv2(0, 0).normal(normal.x(), normal.y(), normal.z()).endVertex();
	}

	public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation,
			TextureAtlasSprite sprite) {
		Vector3f normal = new Vector3f(v3);
		normal.sub(v2);
		Vector3f temp = new Vector3f(v1);
		temp.sub(v2);
		normal.cross(temp);
		normal.normalize();

		return createQuad(v1, v2, v3, v4, rotation, sprite, normal);
	}

	public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation,
			TextureAtlasSprite sprite, Vector3f normal) {
		return createQuad(v1, v2, v3, v4, rotation, sprite, normal, 0, 1, 0, 1);
	}

	public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation,
			TextureAtlasSprite sprite, Vector3f normal, float uvu0, float uvu1, float uvv0, float uvv1) {
		int tw = sprite.getX();
		int th = sprite.getY();

		rotation = rotation.blockCenterToCorner();
		rotation.transformNormal(normal);

		Vector4f vv1 = new Vector4f(v1.x, v1.y, v1.z, 0);
		rotation.transformPosition(vv1);
		Vector4f vv2 = new Vector4f(v2.x, v2.y, v2.z, 0);
		rotation.transformPosition(vv2);
		Vector4f vv3 = new Vector4f(v3.x, v3.y, v3.z, 0);
		rotation.transformPosition(vv3);
		Vector4f vv4 = new Vector4f(v4.x, v4.y, v4.z, 0);
		rotation.transformPosition(vv4);

		BakedQuad[] quad = new BakedQuad[1];
		QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
		builder.setSprite(sprite);
		builder.setDirection(Direction.getNearest(normal.x(), normal.y(), normal.z()));
		putVertex(builder, normal, vv1, uvu0 * tw, uvv0 * th, sprite);
		putVertex(builder, normal, vv2, uvu1 * tw, uvv0 * th, sprite);
		putVertex(builder, normal, vv3, uvu1 * tw, uvv1 * th, sprite);
		putVertex(builder, normal, vv4, uvu0 * tw, uvv1 * th, sprite);
		return quad[0];
	}

	private static Vector3f v(float x, float y, float z) {
		return new Vector3f(x, y, z);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return null;
	}

	@Override
	public ItemOverrides getOverrides() {
		return null;
	}

	private static TextureAtlasSprite getTexture(ResourceLocation resLoc) {
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resLoc);
	}
}
