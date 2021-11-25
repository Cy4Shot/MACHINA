/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.block.properties;

import net.minecraft.util.IStringSerializable;

public enum ActivationState implements IStringSerializable {
	UN_POWERED("un_powered"),
	NOT_ACTIVE("not_active"),
	WAITING("waiting"),
	ACTIVE("active");

	private final String name;

	ActivationState(String pName) {
		name = pName;
	}

	@Override
	public String getSerializedName() { return name; }
}
