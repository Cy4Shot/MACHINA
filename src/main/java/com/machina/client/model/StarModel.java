package com.machina.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.google.common.collect.ImmutableList;
import com.machina.api.client.BakedQuadBuilder;
import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.vertex.VertexFormatElement;

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

public class StarModel implements IDynamicBakedModel {

	private final ResourceLocation bg = new MachinaRL("gui/starchart/star_bg");
	private final ResourceLocation fg = new MachinaRL("gui/starchart/star_fg");

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
			@NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
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

	private BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite,
			int tintindex) {
		Vector3d normal = v3.sub(v2).cross(v1.sub(v2)).normalize();
		int tw = sprite.getX();
		int th = sprite.getY();

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
