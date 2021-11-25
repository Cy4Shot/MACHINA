/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.util;

public class MachinaColours {

	// TODO make these actually be colours
	public static final MachinaColours TITLE = new MachinaColours(0);
	public static final MachinaColours HEADING = new MachinaColours(0);
	public static final MachinaColours SUBHEADING = new MachinaColours(0);
	public static final MachinaColours SCREEN = new MachinaColours(0);

	private final int colourARGB;

	protected MachinaColours(int colourARGB) {
		this.colourARGB = colourARGB;
	}

	public int argb() {
		return colourARGB;
	}

}
