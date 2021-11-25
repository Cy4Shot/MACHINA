package com.cy4.machina.api.client.gui;

import com.cy4.machina.client.util.Rectangle;

import net.minecraft.util.math.vector.Vector2f;

public interface IBoundedGui {

	Rectangle getContainerBounds();

	default Vector2f getCentre() {
		Rectangle bounds = getContainerBounds();
		return new Vector2f(Math.abs(bounds.x0 - bounds.x1) - bounds.x0 / 2,
				Math.abs(bounds.y0 - bounds.y1) - bounds.y0 / 2);
	}

}
