package com.machina.api.cap.sided;

import net.minecraft.core.Direction;

public interface ISideAdapter {
	Side get(Direction d);

	void cycle(Direction d);
}
