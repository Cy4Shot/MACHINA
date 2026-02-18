package com.machina.api.client.model.mesh;

import java.util.function.Function;

import javax.annotation.Nullable;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.mojang.math.Transformation;

import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.model.IModelBuilder;

public interface IMesh {

	float RESCALE_22_5 = 1.0F / (float) Math.cos((float) Math.PI / 8F) - 1.0F;
	float RESCALE_45 = 1.0F / (float) Math.cos((float) Math.PI / 4F) - 1.0F;

	default BakedQuad bakeQuad(Vector3f from, Vector3f to, MeshFace face, TextureAtlasSprite tex, Direction dir,
			@Nullable BlockElementRotation rot, boolean shade) {
		BlockFaceUV blockfaceuv = face.uv;

		float[] afloat = new float[blockfaceuv.uvs.length];
		System.arraycopy(blockfaceuv.uvs, 0, afloat, 0, afloat.length);
		float f = tex.uvShrinkRatio();
		float f1 = (blockfaceuv.uvs[0] + blockfaceuv.uvs[0] + blockfaceuv.uvs[2] + blockfaceuv.uvs[2]) / 4.0F;
		float f2 = (blockfaceuv.uvs[1] + blockfaceuv.uvs[1] + blockfaceuv.uvs[3] + blockfaceuv.uvs[3]) / 4.0F;
		blockfaceuv.uvs[0] = Mth.lerp(f, blockfaceuv.uvs[0], f1);
		blockfaceuv.uvs[2] = Mth.lerp(f, blockfaceuv.uvs[2], f1);
		blockfaceuv.uvs[1] = Mth.lerp(f, blockfaceuv.uvs[1], f2);
		blockfaceuv.uvs[3] = Mth.lerp(f, blockfaceuv.uvs[3], f2);
		int[] aint = makeVertices(blockfaceuv, tex, dir, setupShape(from, to), Transformation.identity(), rot, shade);
		Direction direction = FaceBakery.calculateFacing(aint);
		System.arraycopy(afloat, 0, blockfaceuv.uvs, 0, afloat.length);
		if (rot == null) {
			recalculateWinding(aint, direction);
		}

		ClientHooks.fillNormal(aint, direction);
		return new BakedQuad(aint, -1, direction, tex, shade, true);
	}

	default float[] setupShape(Vector3f from, Vector3f to) {
		float[] afloat = new float[Direction.values().length];
		afloat[FaceInfo.Constants.MIN_X] = from.x() / 16.0F;
		afloat[FaceInfo.Constants.MIN_Y] = from.y() / 16.0F;
		afloat[FaceInfo.Constants.MIN_Z] = from.z() / 16.0F;
		afloat[FaceInfo.Constants.MAX_X] = to.x() / 16.0F;
		afloat[FaceInfo.Constants.MAX_Y] = to.y() / 16.0F;
		afloat[FaceInfo.Constants.MAX_Z] = to.z() / 16.0F;
		return afloat;
	}

	default int[] makeVertices(BlockFaceUV uv, TextureAtlasSprite tex, Direction d, float[] light, Transformation t,
			@Nullable BlockElementRotation r, boolean s) {
		int[] aint = new int[32];
		for (int i = 0; i < 4; ++i) {
			this.bakeVertex(aint, i, d, uv, light, tex, t, r, s);
		}
		return aint;
	}

	default void bakeVertex(int[] p_111621_, int p_111622_, Direction p_111623_, BlockFaceUV p_111624_,
			float[] p_111625_, TextureAtlasSprite p_111626_, Transformation p_111627_,
			@Nullable BlockElementRotation p_111628_, boolean p_111629_) {
		FaceInfo.VertexInfo faceinfo$vertexinfo = FaceInfo.fromFacing(p_111623_).getVertexInfo(p_111622_);
		Vector3f vector3f = new Vector3f(p_111625_[faceinfo$vertexinfo.xFace], p_111625_[faceinfo$vertexinfo.yFace],
				p_111625_[faceinfo$vertexinfo.zFace]);
		this.applyElementRotation(vector3f, p_111628_);
		this.applyModelRotation(vector3f, p_111627_);
		this.fillVertex(p_111621_, p_111622_, vector3f, p_111626_, p_111624_);
	}

	default void fillVertex(int[] p_111615_, int p_111616_, Vector3f p_254291_, TextureAtlasSprite p_111618_,
			BlockFaceUV p_111619_) {
		int i = p_111616_ * 8;
		p_111615_[i] = Float.floatToRawIntBits(p_254291_.x());
		p_111615_[i + 1] = Float.floatToRawIntBits(p_254291_.y());
		p_111615_[i + 2] = Float.floatToRawIntBits(p_254291_.z());
		p_111615_[i + 3] = -1;
		p_111615_[i + 4] = Float.floatToRawIntBits(
				p_111618_.getU(p_111619_.getU(p_111616_) * .999F + p_111619_.getU((p_111616_ + 2) % 4) * .001F));
		p_111615_[i + 4 + 1] = Float.floatToRawIntBits(
				p_111618_.getV(p_111619_.getV(p_111616_) * .999F + p_111619_.getV((p_111616_ + 2) % 4) * .001F));
	}

	default void applyElementRotation(Vector3f p_254412_, @Nullable BlockElementRotation p_254150_) {
		if (p_254150_ != null) {
			Vector3f vector3f;
			Vector3f vector3f1 = switch (p_254150_.axis()) {
			case X -> {
				vector3f = new Vector3f(1.0F, 0.0F, 0.0F);
				yield new Vector3f(0.0F, 1.0F, 1.0F);
			}
			case Y -> {
				vector3f = new Vector3f(0.0F, 1.0F, 0.0F);
				yield new Vector3f(1.0F, 0.0F, 1.0F);
			}
			case Z -> {
				vector3f = new Vector3f(0.0F, 0.0F, 1.0F);
				yield new Vector3f(1.0F, 1.0F, 0.0F);
			}
			default -> throw new IllegalArgumentException("There are only 3 axes");
			};

			Quaternionf quaternionf = (new Quaternionf()).rotationAxis(p_254150_.angle() * ((float) Math.PI / 180F),
					vector3f);
			if (p_254150_.rescale()) {
				if (Math.abs(p_254150_.angle()) == 22.5F) {
					vector3f1.mul(RESCALE_22_5);
				} else {
					vector3f1.mul(RESCALE_45);
				}

				vector3f1.add(1.0F, 1.0F, 1.0F);
			} else {
				vector3f1.set(1.0F, 1.0F, 1.0F);
			}

			this.rotateVertexBy(p_254412_, new Vector3f(p_254150_.origin()), (new Matrix4f()).rotation(quaternionf),
					vector3f1);
		}
	}

	default void applyModelRotation(Vector3f p_254561_, Transformation p_253793_) {
		if (p_253793_ != Transformation.identity()) {
			this.rotateVertexBy(p_254561_, new Vector3f(0.5F, 0.5F, 0.5F), p_253793_.getMatrix(),
					new Vector3f(1.0F, 1.0F, 1.0F));
		}
	}

	default void rotateVertexBy(Vector3f p_253804_, Vector3f p_253835_, Matrix4f p_253730_, Vector3f p_254056_) {
		Vector4f vector4f = p_253730_.transform(new Vector4f(p_253804_.x() - p_253835_.x(),
				p_253804_.y() - p_253835_.y(), p_253804_.z() - p_253835_.z(), 1.0F));
		vector4f.mul(new Vector4f(p_254056_, 1.0F));
		p_253804_.set(vector4f.x() + p_253835_.x(), vector4f.y() + p_253835_.y(), vector4f.z() + p_253835_.z());
	}

	default void recalculateWinding(int[] p_111631_, Direction p_111632_) {
		int[] aint = new int[p_111631_.length];
		System.arraycopy(p_111631_, 0, aint, 0, p_111631_.length);
		float[] afloat = new float[Direction.values().length];
		afloat[FaceInfo.Constants.MIN_X] = 999.0F;
		afloat[FaceInfo.Constants.MIN_Y] = 999.0F;
		afloat[FaceInfo.Constants.MIN_Z] = 999.0F;
		afloat[FaceInfo.Constants.MAX_X] = -999.0F;
		afloat[FaceInfo.Constants.MAX_Y] = -999.0F;
		afloat[FaceInfo.Constants.MAX_Z] = -999.0F;

		for (int i = 0; i < 4; ++i) {
			int j = 8 * i;
			float f = Float.intBitsToFloat(aint[j]);
			float f1 = Float.intBitsToFloat(aint[j + 1]);
			float f2 = Float.intBitsToFloat(aint[j + 2]);
			if (f < afloat[FaceInfo.Constants.MIN_X]) {
				afloat[FaceInfo.Constants.MIN_X] = f;
			}

			if (f1 < afloat[FaceInfo.Constants.MIN_Y]) {
				afloat[FaceInfo.Constants.MIN_Y] = f1;
			}

			if (f2 < afloat[FaceInfo.Constants.MIN_Z]) {
				afloat[FaceInfo.Constants.MIN_Z] = f2;
			}

			if (f > afloat[FaceInfo.Constants.MAX_X]) {
				afloat[FaceInfo.Constants.MAX_X] = f;
			}

			if (f1 > afloat[FaceInfo.Constants.MAX_Y]) {
				afloat[FaceInfo.Constants.MAX_Y] = f1;
			}

			if (f2 > afloat[FaceInfo.Constants.MAX_Z]) {
				afloat[FaceInfo.Constants.MAX_Z] = f2;
			}
		}

		FaceInfo faceinfo = FaceInfo.fromFacing(p_111632_);

		for (int i1 = 0; i1 < 4; ++i1) {
			int j1 = 8 * i1;
			FaceInfo.VertexInfo faceinfo$vertexinfo = faceinfo.getVertexInfo(i1);
			float f8 = afloat[faceinfo$vertexinfo.xFace];
			float f3 = afloat[faceinfo$vertexinfo.yFace];
			float f4 = afloat[faceinfo$vertexinfo.zFace];
			p_111631_[j1] = Float.floatToRawIntBits(f8);
			p_111631_[j1 + 1] = Float.floatToRawIntBits(f3);
			p_111631_[j1 + 2] = Float.floatToRawIntBits(f4);

			for (int k = 0; k < 4; ++k) {
				int l = 8 * k;
				float f5 = Float.intBitsToFloat(aint[l]);
				float f6 = Float.intBitsToFloat(aint[l + 1]);
				float f7 = Float.intBitsToFloat(aint[l + 2]);
				if (Mth.equal(f8, f5) && Mth.equal(f3, f6) && Mth.equal(f4, f7)) {
					p_111631_[j1 + 4] = aint[l + 4];
					p_111631_[j1 + 4 + 1] = aint[l + 4 + 1];
				}
			}
		}

	}

	void addQuads(IModelBuilder<?> modelBuilder, Function<Material, TextureAtlasSprite> spriteGetter,
			BlockElementRotation rotation);
}
