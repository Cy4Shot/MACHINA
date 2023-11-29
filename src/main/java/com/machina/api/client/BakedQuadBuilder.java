package com.machina.api.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class BakedQuadBuilder {
	private static final int SIZE = DefaultVertexFormat.BLOCK.getElements().size();

	private final float[][][] unpackedData = new float[4][SIZE][4];
	private int tint = -1;
	private Direction orientation;
	private TextureAtlasSprite texture;
	private boolean applyDiffuseLighting = true;

	private int vertices = 0;
	private int elements = 0;
	private boolean full = false;
	private boolean contractUVs = false;

	public BakedQuadBuilder() {
	}

	public BakedQuadBuilder(TextureAtlasSprite texture) {
		this.texture = texture;
	}

	public void setContractUVs(boolean value) {
		this.contractUVs = value;
	}

	public VertexFormat getVertexFormat() {
		return DefaultVertexFormat.BLOCK;
	}

	public void setQuadTint(int tint) {
		this.tint = tint;
	}

	public void setQuadOrientation(Direction orientation) {
		this.orientation = orientation;
	}

	public void setTexture(TextureAtlasSprite texture) {
		this.texture = texture;
	}

	public void setApplyDiffuseLighting(boolean diffuse) {
		this.applyDiffuseLighting = diffuse;
	}

	public void put(int element, float... data) {
		for (int i = 0; i < 4; i++) {
			if (i < data.length) {
				unpackedData[vertices][element][i] = data[i];
			} else {
				unpackedData[vertices][element][i] = 0;
			}
		}
		elements++;
		if (elements == SIZE) {
			vertices++;
			elements = 0;
		}
		if (vertices == 4) {
			full = true;
		}
	}

	private final float eps = 1f / 0x100;

	public BakedQuad build() {
		if (!full) {
			throw new IllegalStateException("not enough data");
		}
		if (texture == null) {
			throw new IllegalStateException("texture not set");
		}
		if (contractUVs) {
			float tX = texture.getX() / (texture.getU1() - texture.getU0());
			float tY = texture.getY() / (texture.getV1() - texture.getV0());
			float tS = tX > tY ? tX : tY;
			float ep = 1f / (tS * 0x100);
			int uve = 0;
			ImmutableList<VertexFormatElement> elements = DefaultVertexFormat.BLOCK.getElements();
			while (uve < elements.size()) {
				VertexFormatElement e = elements.get(uve);
				if (e.getUsage() == VertexFormatElement.Usage.UV && e.getIndex() == 0) {
					break;
				}
				uve++;
			}
			if (uve == elements.size()) {
				throw new IllegalStateException("Can't contract UVs: format doesn't contain UVs");
			}
			float[] uvc = new float[4];
			for (int v = 0; v < 4; v++) {
				for (int i = 0; i < 4; i++) {
					uvc[i] += unpackedData[v][uve][i] / 4;
				}
			}
			for (int v = 0; v < 4; v++) {
				for (int i = 0; i < 4; i++) {
					float uo = unpackedData[v][uve][i];
					float un = uo * (1 - eps) + uvc[i] * eps;
					float ud = uo - un;
					float aud = ud;
					if (aud < 0)
						aud = -aud;
					if (aud < ep) {
						float udc = uo - uvc[i];
						if (udc < 0)
							udc = -udc;
						if (udc < 2 * ep) {
							un = (uo + uvc[i]) / 2;
						} else {
							un = uo + (ud < 0 ? ep : -ep);
						}
					}
					unpackedData[v][uve][i] = un;
				}
			}
		}
		int[] packed = new int[DefaultVertexFormat.BLOCK.getIntegerSize() * 4];
		for (int v = 0; v < 4; v++) {
			for (int e = 0; e < SIZE; e++) {
				pack(unpackedData[v][e], packed, DefaultVertexFormat.BLOCK, v, e);
			}
		}
		return new BakedQuad(packed, tint, orientation, texture, applyDiffuseLighting);
	}

	public static void pack(float[] from, int[] to, VertexFormat formatTo, int v, int e) {
		VertexFormatElement element = formatTo.getElements().get(e);
		int vertexStart = v * formatTo.getVertexSize() + formatTo.getOffset(e);
		int count = element.getElementCount();
		VertexFormatElement.Type type = element.getType();
		int size = type.getSize();
		int mask = (256 << (8 * (size - 1))) - 1;
		for (int i = 0; i < 4; i++) {
			if (i < count) {
				int pos = vertexStart + size * i;
				int index = pos >> 2;
				int offset = pos & 3;
				int bits = 0;
				float f = i < from.length ? from[i] : 0;
				if (type == VertexFormatElement.Type.FLOAT) {
					bits = Float.floatToRawIntBits(f);
				} else if (type == VertexFormatElement.Type.UBYTE || type == VertexFormatElement.Type.USHORT
						|| type == VertexFormatElement.Type.UINT) {
					bits = Math.round(f * mask);
				} else {
					bits = Math.round(f * (mask >> 1));
				}
				to[index] &= ~(mask << (offset * 8));
				to[index] |= (((bits & mask) << (offset * 8)));
			}
		}
	}
}
