package com.machina.client.model.celestial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

public abstract class CelestialModel implements IDynamicBakedModel {

	private List<BakedQuad> quads = new ArrayList<>();

	public CelestialModel() {
		setup();
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
			@NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
		if (side != null) {
			return Collections.emptyList();
		}

		return quads;
	}

	public CelestialModel offset(float x, float y, float z) {
		transform(v -> x + v, v -> y + v, v -> z + v);
		return this;
	}

	public CelestialModel scale(float x, float y, float z) {
		transform(v -> x * v, v -> y * v, v -> z * v);
		return this;
	}

	private void transform(Function<Float, Float> x, Function<Float, Float> y, Function<Float, Float> z) {
		List<BakedQuad> n = new ArrayList<>();
		for (BakedQuad q : quads) {
			int[] quadData = q.getVertices();
			for (int i = 0; i < 4; i++) {
				int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
				quadData[offset] = Float.floatToRawIntBits(x.apply(Float.intBitsToFloat(quadData[offset])));
				quadData[offset + 1] = Float.floatToRawIntBits(y.apply(Float.intBitsToFloat(quadData[offset + 1])));
				quadData[offset + 2] = Float.floatToRawIntBits(z.apply(Float.intBitsToFloat(quadData[offset + 2])));
			}
			n.add(new BakedQuad(quadData, q.getTintIndex(), q.getDirection(), q.getSprite(), q.isShade(),
					q.hasAmbientOcclusion()));
		}
		this.quads = n;
	}

	public void setupCube() {
		quads.clear();
		TextureAtlasSprite b = getTexture(tex());
		Transformation rotation = Transformation.identity();
		float l = -0.5f;
		float r = 0.5f;
		float p = 0.5f;

		quads.add(createQuad(v(r, p, r), v(r, p, l), v(l, p, l), v(l, p, r), rotation, b));
		quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), rotation, b));
		quads.add(createQuad(v(r, p, r), v(r, l, r), v(r, l, l), v(r, p, l), rotation, b));
		quads.add(createQuad(v(l, p, l), v(l, l, l), v(l, l, r), v(l, p, r), rotation, b));
		quads.add(createQuad(v(r, p, l), v(r, l, l), v(l, l, l), v(l, p, l), rotation, b));
		quads.add(createQuad(v(l, p, r), v(l, l, r), v(r, l, r), v(r, p, r), rotation, b));
	}

	public void setupSphere(int n) {
		quads.clear();
		TextureAtlasSprite back = getTexture(tex());
		Transformation rotation = Transformation.identity();
		for (int phi = 0; phi < n; phi++) {
			for (int theta = 0; theta < n; theta++) {
				float x0 = (float) Math.cos(2 * Math.PI * phi / n) * (float) Math.sin(Math.PI * theta / n);
				float y0 = (float) Math.sin(2 * Math.PI * phi / n) * (float) Math.sin(Math.PI * theta / n);
				float z0 = (float) Math.cos(Math.PI * theta / n);

				float x1 = (float) Math.cos(2 * Math.PI * (phi + 1) / n) * (float) Math.sin(Math.PI * theta / n);
				float y1 = (float) Math.sin(2 * Math.PI * (phi + 1) / n) * (float) Math.sin(Math.PI * theta / n);
				float z1 = (float) Math.cos(Math.PI * theta / n);

				float x2 = (float) Math.cos(2 * Math.PI * (phi + 1) / n) * (float) Math.sin(Math.PI * (theta + 1) / n);
				float y2 = (float) Math.sin(2 * Math.PI * (phi + 1) / n) * (float) Math.sin(Math.PI * (theta + 1) / n);
				float z2 = (float) Math.cos(Math.PI * (theta + 1) / n);

				float x3 = (float) Math.cos(2 * Math.PI * phi / n) * (float) Math.sin(Math.PI * (theta + 1) / n);
				float y3 = (float) Math.sin(2 * Math.PI * phi / n) * (float) Math.sin(Math.PI * (theta + 1) / n);
				float z3 = (float) Math.cos(Math.PI * (theta + 1) / n);

				Vector3f v0 = v(x0, y0, z0);
				Vector3f v1 = v(x1, y1, z1);
				Vector3f v2 = v(x2, y2, z2);
				Vector3f v3 = v(x3, y3, z3);

				float uvu0 = (float) phi / (n - 1);
				float uvu1 = (float) (phi + 1) / (n - 1);
				float uvv0 = (float) theta / (n - 1);
				float uvv1 = (float) (theta + 1) / (n - 1);

				quads.add(createQuad(v0, v1, v2, v3, rotation, back, v0, uvu0, uvu1, uvv0, uvv1));
			}
		}
	}

	public abstract void setup();

	public abstract ResourceLocation tex();

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
