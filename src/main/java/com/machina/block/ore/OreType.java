package com.machina.block.ore;

import java.util.Random;

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

	public static OreType randWeighted(Random rand) {
		double tot = 0D;
		for (OreType i : values()) {
			tot += i.rarityMult;
		}

		int idx = 0;
		for (double r = Math.random() * tot; idx < values().length - 1; ++idx) {
			r -= values()[idx].rarityMult;
			if (r <= 0D)
				break;
		}
		return values()[idx];
	}
}