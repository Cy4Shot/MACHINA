package com.machina.block.ore;

import java.util.Random;

import com.machina.util.text.MachinaRL;

import net.minecraft.util.ResourceLocation;

public enum OreType {
	SALT("NaCl", 1f),
	FLOURITE("CaF2", 1f),
	BORON("B", 0.5f),
	SULPHUR("S", 0.75f),
	BISMUTH("Bi", 0.75f),
	LEAD("Pb", 0.5f),
	SILVER("Ag", 0.3f),
	PALLADIUM("Pd", 0.5f),
	MACHINARIUM("Ma", 0.05f);

	float rarityMult;
	String chem;

	OreType(String chem, float r) {
		this.chem = chem;
		this.rarityMult = r;
	}

	public String chem() {
		return this.chem;
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