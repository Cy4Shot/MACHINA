/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.client.gui;

public interface IGui {

	default int getLeft() {
		if (this instanceof BaseScreen<?>) { return ((BaseScreen<?>) this).getGuiLeft(); }
		return 0;
	}

	default int getTop() {
		if (this instanceof BaseScreen<?>) { return ((BaseScreen<?>) this).getGuiTop(); }
		return 0;
	}

	default int getWidth() {
		if (this instanceof BaseScreen<?>) { return ((BaseScreen<?>) this).getXSize(); }
		return 0;
	}

	default int getHeight() {
		if (this instanceof BaseScreen<?>) { return ((BaseScreen<?>) this).getYSize(); }
		return 0;
	}

}
