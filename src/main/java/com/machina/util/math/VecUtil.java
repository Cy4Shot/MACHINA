package com.machina.util.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;

public class VecUtil {

	public static final Vector3f F3_0 = new Vector3f(0, 0, 0);

	public static Vector3f vec3f(float f) {
		return new Vector3f(f, f, f);
	}

	public static Vector3f norm(Vector3f in) {
		float f = MathHelper.fastInvSqrt(in.x() * in.x() + in.y() * in.y() + in.z() * in.z());
		return new Vector3f(in.x() * f, in.y() * f, in.z() * f);
	}

	public static Vector3f randNorm(Random rand) {
		return norm(new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
	}

	public static int max(Vector3i vec) {
		return Arrays.stream(new int[] { vec.getX(), vec.getY(), vec.getZ() }).max().getAsInt();
	}

	public static final Comparator<Vector3i> X_COMP = new Comparator<Vector3i>() {
		@Override
		public int compare(Vector3i o1, Vector3i o2) {
			return Integer.compare(o1.getX(), o2.getX());
		}
	};
	
	public static final Comparator<Vector3i> Y_COMP = new Comparator<Vector3i>() {
		@Override
		public int compare(Vector3i o1, Vector3i o2) {
			return Integer.compare(o1.getY(), o2.getY());
		}
	};
	
	public static final Comparator<Vector3i> Z_COMP = new Comparator<Vector3i>() {
		@Override
		public int compare(Vector3i o1, Vector3i o2) {
			return Integer.compare(o1.getZ(), o2.getZ());
		}
	};
	
	public static int minX(Collection<? extends Vector3i> coll) {
		return Collections.min(coll, X_COMP).getX();
	}
	
	public static int minY(Collection<? extends Vector3i> coll) {
		return Collections.min(coll, Y_COMP).getY();
	}
	
	public static int minZ(Collection<? extends Vector3i> coll) {
		return Collections.min(coll, Z_COMP).getZ();
	}
	
	public static int maxX(Collection<? extends Vector3i> coll) {
		return Collections.max(coll, X_COMP).getX();
	}
	
	public static int maxY(Collection<? extends Vector3i> coll) {
		return Collections.max(coll, Y_COMP).getY();
	}
	
	public static int maxZ(Collection<? extends Vector3i> coll) {
		return Collections.max(coll, Z_COMP).getZ();
	}
	
	public static Vector3i minAll(Collection<? extends Vector3i> coll) {
		return new Vector3i(minX(coll), minY(coll), minZ(coll));
	}
	
	public static Vector3i maxAll(Collection<? extends Vector3i> coll) {
		return new Vector3i(maxX(coll), maxY(coll), maxZ(coll));
	}
}
