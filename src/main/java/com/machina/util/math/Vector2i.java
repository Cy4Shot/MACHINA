package com.machina.util.math;

public class Vector2i {
	public static final Vector2i ZERO = new Vector2i(0, 0);
	public static final Vector2i ONE = new Vector2i(1, 1);
	public static final Vector2i UNIT_X = new Vector2i(1, 0);
	public static final Vector2i NEG_UNIT_X = new Vector2i(-1, 0);
	public static final Vector2i UNIT_Y = new Vector2i(0, 1);
	public static final Vector2i NEG_UNIT_Y = new Vector2i(0, -1);
	public static final Vector2i MAX = new Vector2i(Integer.MAX_VALUE, Integer.MAX_VALUE);
	public static final Vector2i MIN = new Vector2i(Integer.MIN_VALUE, Integer.MIN_VALUE);
	public final int x;
	public final int y;

	public Vector2i(int pX, int pY) {
		this.x = pX;
		this.y = pY;
	}

	public boolean equals(Vector2i pOther) {
		return this.x == pOther.x && this.y == pOther.y;
	}
}
