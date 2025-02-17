package com.machina.api.client.model;

import java.util.Arrays;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class RetexturedBakedQuad extends BakedQuad {

	public RetexturedBakedQuad(BakedQuad quad, Direction dir, TextureAtlasSprite tex) {

		super(Arrays.copyOf(quad.getVertices(), quad.getVertices().length), quad.getTintIndex(), dir, tex,
				quad.isShade());

		this.vertices[4] = Float.floatToRawIntBits(tex.getU0());
		this.vertices[5] = Float.floatToRawIntBits(tex.getV0());

		this.vertices[12] = Float.floatToRawIntBits(tex.getU1());
		this.vertices[13] = Float.floatToRawIntBits(tex.getV0());

		this.vertices[20] = Float.floatToRawIntBits(tex.getU1());
		this.vertices[21] = Float.floatToRawIntBits(tex.getV1());

		this.vertices[28] = Float.floatToRawIntBits(tex.getU0());
		this.vertices[29] = Float.floatToRawIntBits(tex.getV1());
	}

}