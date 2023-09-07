package com.machina.api.util.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.core.Vec3i;

public class VecUtil {
	
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
