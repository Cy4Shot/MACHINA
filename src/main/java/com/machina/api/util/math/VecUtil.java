package com.machina.api.util.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.core.Vec3i;

public class VecUtil {

	public static final Vector3f XN = new Vector3f(-1.0F, 0.0F, 0.0F);
	public static final Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
	public static final Vector3f YN = new Vector3f(0.0F, -1.0F, 0.0F);
	public static final Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
	public static final Vector3f ZN = new Vector3f(0.0F, 0.0F, -1.0F);
	public static final Vector3f ZP = new Vector3f(0.0F, 0.0F, 1.0F);

	public static Quaternionf rotationDegrees(Vector3f vec, float degrees) {
		degrees *= ((float) Math.PI / 180F);

		float f = MathUtil.sin(degrees / 2.0F);
		float x = vec.x() * f;
		float y = vec.y() * f;
		float z = vec.z() * f;
		float w = MathUtil.cos(degrees / 2.0F);
		return new Quaternionf(x, y, z, w);
	}

	public static Matrix4f createScaleMatrix(float x, float y, float z) {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.set(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);
		return matrix4f;
	}

	public static Matrix4f orthographic(float w, float h, float n, float f) {
		Matrix4f matrix4f = new Matrix4f();
		float d = f - n;
		matrix4f.set(2f / w, 0, 0, -1, 0, 2f / h, 0, 1, 0, 0, -2f / d, -(f + n) / d, 0, 0, 0, 1f);
		return matrix4f;
	}

	public static Matrix4f projection(float fov, float aspect, float near, float far) {
		Matrix4f matrix4f = new Matrix4f();
		float ood = 1 / (near - far);
		float m11 = (float) (1 / Math.tan(0.5f * fov));
		matrix4f.set(m11 / aspect, 0, 0, 0, 0, m11, 0, 0, 0, 0, far * ood, 1, 0, 0, (-far * near) * ood, 0);
		return matrix4f;
	}

	public static final Comparator<Vec3i> X_COMP = new Comparator<Vec3i>() {
		@Override
		public int compare(Vec3i o1, Vec3i o2) {
			return Integer.compare(o1.getX(), o2.getX());
		}
	};

	public static final Comparator<Vec3i> Y_COMP = new Comparator<Vec3i>() {
		@Override
		public int compare(Vec3i o1, Vec3i o2) {
			return Integer.compare(o1.getY(), o2.getY());
		}
	};

	public static final Comparator<Vec3i> Z_COMP = new Comparator<Vec3i>() {
		@Override
		public int compare(Vec3i o1, Vec3i o2) {
			return Integer.compare(o1.getZ(), o2.getZ());
		}
	};

	public static int minX(Collection<? extends Vec3i> coll) {
		return Collections.min(coll, X_COMP).getX();
	}

	public static int minY(Collection<? extends Vec3i> coll) {
		return Collections.min(coll, Y_COMP).getY();
	}

	public static int minZ(Collection<? extends Vec3i> coll) {
		return Collections.min(coll, Z_COMP).getZ();
	}

	public static int maxX(Collection<? extends Vec3i> coll) {
		return Collections.max(coll, X_COMP).getX();
	}

	public static int maxY(Collection<? extends Vec3i> coll) {
		return Collections.max(coll, Y_COMP).getY();
	}

	public static int maxZ(Collection<? extends Vec3i> coll) {
		return Collections.max(coll, Z_COMP).getZ();
	}

	public static Vec3i minAll(Collection<? extends Vec3i> coll) {
		return new Vec3i(minX(coll), minY(coll), minZ(coll));
	}

	public static Vec3i maxAll(Collection<? extends Vec3i> coll) {
		return new Vec3i(maxX(coll), maxY(coll), maxZ(coll));
	}

	public static int max(Vec3i vec) {
		return Arrays.stream(new int[] { vec.getX(), vec.getY(), vec.getZ() }).max().getAsInt();
	}
}
