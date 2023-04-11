package com.machina.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.machina.util.math.VecUtil;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

public class OreModel implements IDynamicBakedModel {

	private final ResourceLocation pt;
	private final ResourceLocation bg;
	private final ResourceLocation fg;

	public OreModel(ResourceLocation pt, ResourceLocation bg, ResourceLocation fg) {
		this.pt = pt;
		this.bg = bg;
		this.fg = fg;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
	}

	private void putVertex(BakedQuadBuilder builder, Vector3d normal, double x, double y, double z, float u, float v,
			TextureAtlasSprite sprite, float r, float g, float b) {

		ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
		for (int j = 0; j < elements.size(); j++) {
			VertexFormatElement e = elements.get(j);
			switch (e.getUsage()) {
			case POSITION:
				builder.put(j, (float) x, (float) y, (float) z, 1.0f);
				break;
			case COLOR:
				builder.put(j, r, g, b, 1.0f);
				break;
			case UV:
				switch (e.getIndex()) {
				case 0:
					float iu = sprite.getU(u);
					float iv = sprite.getV(v);
					builder.put(j, iu, iv);
					break;
				case 2:
					builder.put(j, (short) 0, (short) 0);
					break;
				default:
					builder.put(j);
					break;
				}
				break;
			case NORMAL:
				builder.put(j, (float) normal.x, (float) normal.y, (float) normal.z);
				break;
			default:
				builder.put(j);
				break;
			}
		}
	}

	private BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite, int tintindex) {
		Vector3d normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
		int tw = sprite.getWidth();
		int th = sprite.getHeight();

		BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
		builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
		putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v2.x, v2.y, v2.z, 0, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v3.x, v3.y, v3.z, tw, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v4.x, v4.y, v4.z, tw, 0, sprite, 1.0f, 1.0f, 1.0f);
		builder.setQuadTint(tintindex);
		return builder.build();
	}

	private static Vector3d v(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand,
			@Nonnull IModelData extraData) {

		if (side != null) {
			return Collections.emptyList();
		}

		TextureAtlasSprite b = getTexture(bg);
		TextureAtlasSprite f = getTexture(fg);
		List<BakedQuad> quads = new ArrayList<>();
		float s = 1f;
		double l = 1 - s;
		double r = s;
		quads.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), b, 0));
		quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), b, 0));
		quads.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), b, 0));
		quads.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), b, 0));
		quads.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), b, 0));
		quads.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), b, 0));

		l -= 0.01f;
		r += 0.01f;
		quads.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), f, -1));
		quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), f, -1));
		quads.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), f, -1));
		quads.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), f, -1));
		quads.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), f, -1));
		quads.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), f, -1));

		return quads;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return getTexture(pt);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.EMPTY;
	}

	@Override
	public ItemCameraTransforms getTransforms() {
		return getAllTransforms();
	}

	private static ItemCameraTransforms getAllTransforms() {
		ItemTransformVec3f tpLeft = getTransform(TransformType.THIRD_PERSON_LEFT_HAND);
		ItemTransformVec3f tpRight = getTransform(TransformType.THIRD_PERSON_RIGHT_HAND);
		ItemTransformVec3f fpLeft = getTransform(TransformType.FIRST_PERSON_LEFT_HAND);
		ItemTransformVec3f fpRight = getTransform(TransformType.FIRST_PERSON_RIGHT_HAND);
		ItemTransformVec3f head = getTransform(TransformType.HEAD);
		ItemTransformVec3f gui = getTransform(TransformType.GUI);
		ItemTransformVec3f ground = getTransform(TransformType.GROUND);
		ItemTransformVec3f fixed = getTransform(TransformType.FIXED);
		return new ItemCameraTransforms(tpLeft, tpRight, fpLeft, fpRight, head, gui, ground, fixed);
	}

	// Default block rotations.
	private static ItemTransformVec3f getTransform(TransformType type) {
		switch (type) {
		case GUI:
			return new ItemTransformVec3f(new Vector3f(30, 45, 0), VecUtil.F3_0, VecUtil.vec3f(0.625f));
		case GROUND:
			return new ItemTransformVec3f(VecUtil.F3_0, new Vector3f(0, 0.06f, 0), VecUtil.vec3f(0.25f));
		case FIXED:
			return new ItemTransformVec3f(VecUtil.F3_0, VecUtil.F3_0, VecUtil.vec3f(0.5f));
		case THIRD_PERSON_RIGHT_HAND:
			return new ItemTransformVec3f(new Vector3f(75, 45, 0), new Vector3f(0, 0.15f, 0), VecUtil.vec3f(0.375f));
		case THIRD_PERSON_LEFT_HAND:
			return new ItemTransformVec3f(new Vector3f(75, 225, 0), new Vector3f(0, 0.15f, 0), VecUtil.vec3f(0.375f));
		case FIRST_PERSON_RIGHT_HAND:
			return new ItemTransformVec3f(new Vector3f(0, 45, 0), VecUtil.F3_0, VecUtil.vec3f(0.4f));
		case FIRST_PERSON_LEFT_HAND:
			return new ItemTransformVec3f(new Vector3f(0, 225, 0), VecUtil.F3_0, VecUtil.vec3f(0.4f));
		default:
			return ItemTransformVec3f.NO_TRANSFORM;
		}
	}

	private static TextureAtlasSprite getTexture(ResourceLocation resLoc) {
		return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(resLoc);
	}
}
