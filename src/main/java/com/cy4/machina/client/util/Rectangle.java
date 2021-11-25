/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.client.util;

import net.minecraft.util.math.vector.Vector2f;

public class Rectangle {

	public int x0, y0;
	public int x1, y1;

	public Rectangle() {
	}

	public Rectangle(Rectangle other) {
		x0 = other.x0;
		y0 = other.y0;
		x1 = other.x1;
		y1 = other.y1;
	}

	public int getWidth() { return x1 - x0; }

	public int getHeight() { return y1 - y0; }

	public void setWidth(int width) { x1 = x0 + width; }

	public void setHeight(int height) { y1 = y0 + height; }

	public boolean contains(int x, int y) {
		return x0 <= x && x <= x1 && y0 <= y && y <= y1;
	}

	public Vector2f midpoint() {
		return new Vector2f((x1 + x0) / 2f, (y1 + y0) / 2f);
	}
}
