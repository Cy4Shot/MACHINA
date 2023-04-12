package com.machina.block.ore;

import com.machina.util.text.MachinaRL;

import net.minecraft.util.ResourceLocation;

public enum OreType {
	SALT(1f),
	FLOURITE(1f),
	BORON(0.5f),
	SULPHUR(0.75f),
	BISMUTH(0.75f),
	LEAD(0.5f),
	SILVER(0.3f),
	PALLADIUM(0.5f),
	MACHINARIUM(0.05f);

	float rarityMult;

	OreType(float r) {
		this.rarityMult = r;
	}

	public ResourceLocation getTexturePath() {
		return new MachinaRL("ore/" + this.name().toLowerCase());
	}
}