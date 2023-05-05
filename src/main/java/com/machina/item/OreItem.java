package com.machina.item;

import com.machina.planet.OreType;

public class OreItem extends ChemicalItem {

	OreType type;

	public OreItem(Properties pProperties, OreType type) {
		super(pProperties, type.chem());
		this.type = type;
	}

	public OreType type() {
		return this.type;
	}
}