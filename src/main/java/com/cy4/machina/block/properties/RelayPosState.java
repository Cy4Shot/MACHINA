/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.block.properties;

import net.minecraft.util.IStringSerializable;

public enum RelayPosState implements IStringSerializable {

	N_A("n_a"), NORTH("north"), NORTHEAST("northeast"), EAST("east"), SOUTHEAST("southeast"), SOUTH("south"),
	SOUTHWEST("southwest"), WEST("west"), NORTHWEST("northwest"), CENTER("center");

	private final String name;

	RelayPosState(String pName) {
		name = pName;
	}

	@Override
	public String getSerializedName() { return name; }
}
