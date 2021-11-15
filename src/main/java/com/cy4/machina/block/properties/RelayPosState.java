package com.cy4.machina.block.properties;

import net.minecraft.util.IStringSerializable;

public enum RelayPosState implements IStringSerializable {
	N_A("n_a"), 
	NORTH("north"), 
	NORTHEAST("northeast"), 
	EAST("east"), 
	SOUTHEAST("southeast"), 
	SOUTH("south"), 
	SOUTHWEST("southwest"), 
	WEST("west"), 
	NORTHWEST("northwest"),
	CENTER("center");

	
	private final String name;

	private RelayPosState(String pName) {
	   this.name = pName;
	}

	public String getSerializedName() {
	   return this.name;
	}
}
