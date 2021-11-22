package com.cy4.machina.block.properties;

import net.minecraft.util.IStringSerializable;

public enum ActivationState implements IStringSerializable {

	NOT_ACTIVE("not_active"), WAITING("waiting"), ACTIVE("active");

	private final String name;

	ActivationState(String pName) {
		name = pName;
	}

	@Override
	public String getSerializedName() { return name; }
}
