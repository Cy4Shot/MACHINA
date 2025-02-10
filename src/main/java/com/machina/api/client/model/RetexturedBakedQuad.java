package com.machina.api.client.model;

import java.util.Arrays;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

// From CoFH Core
public class RetexturedBakedQuad extends BakedQuad {

	private final TextureAtlasSprite texture;

	public RetexturedBakedQuad(BakedQuad quad, Direction dir, TextureAtlasSprite textureIn) {

		super(Arrays.copyOf(quad.getVertices(), quad.getVertices().length), quad.getTintIndex(),
				FaceBakery.calculateFacing(quad.getVertices()), quad.getSprite(), quad.isShade());
		this.texture = textureIn;
		this.remapQuad();
	}

	private void remapQuad() {
		for (int i = 0; i < 4; ++i) {
			int j = DefaultVertexFormat.BLOCK.getIntegerSize() * i;
			int uvIndex = 4;
			this.vertices[j + uvIndex] = Float.floatToRawIntBits(this.texture
					.getU(getUnInterpolatedU(this.sprite, Float.intBitsToFloat(this.vertices[j + uvIndex]))));
			this.vertices[j + uvIndex + 1] = Float.floatToRawIntBits(this.texture
					.getV(getUnInterpolatedV(this.sprite, Float.intBitsToFloat(this.vertices[j + uvIndex + 1]))));
		}
	}

	@Override
	public @NotNull TextureAtlasSprite getSprite() {
		return texture;
	}

	private static float getUnInterpolatedU(TextureAtlasSprite sprite, float u) {

		float f = sprite.getU1() - sprite.getU0();
		return (u - sprite.getU0()) / f;
	}

	private static float getUnInterpolatedV(TextureAtlasSprite sprite, float v) {

		float f = sprite.getV1() - sprite.getV0();
		return (v - sprite.getV0()) / f;
	}

}