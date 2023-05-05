package com.machina.planet;

import java.util.Random;

import com.machina.util.MachinaRL;

import net.minecraft.util.ResourceLocation;

public enum OreType {
	SALT("NaCl", false, 1f),
	FLUORITE("CaF2", false, 1f),
	BORON("B", true, 0.5f),
	SULPHUR("S", false, 0.75f),
	BISMUTH("Bi", true, 0.75f),
	LEAD("Pb", true, 0.5f),
	SILVER("Ag", true, 0.3f),
	PALLADIUM("Pd", true, 0.5f),
	MACHINARIUM("Ma", true, 0.05f);

	boolean ingot;
	float rarityMult;
	String chem;

	OreType(String chem, boolean ingot, float r) {
		this.chem = chem;
		this.ingot = ingot;
		this.rarityMult = r;
	}

	public String chem() {
		return this.chem;
	}

	public boolean ingot() {
		return this.ingot;
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