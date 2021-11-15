package com.cy4.machina.block.properties;

import net.minecraft.util.IStringSerializable;

public enum ActivationState implements IStringSerializable {
	NOT_ACTIVE("not_active"),
	WAITING("waiting"),
	ACTIVE("active");
	
	private final String name;
	
	private ActivationState(String pName) {
		this.name = pName;
	}
	
	public String getSerializedName() {
		return this.name;
	}
}
