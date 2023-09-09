package com.machina.api.starchart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.machina.api.util.math.MathUtil;
import com.mojang.datafixers.util.Pair;

import net.minecraftforge.fluids.FluidStack;

public class AtmosphericComposition {

	public static final AtmosphericComposition NONE = new AtmosphericComposition();

	public LinkedHashMap<FluidStack, Float> atmosphere = new LinkedHashMap<>();

	@SafeVarargs
	public AtmosphericComposition(Pair<FluidStack, Float>... atm) {
		float total = 0f;
		for (Pair<FluidStack, Float> p : atm) {
			atmosphere.put(p.getFirst(), p.getSecond());
			total += p.getSecond();
		}
		for (Pair<FluidStack, Float> p : atm) {
			float a = atmosphere.remove(p.getFirst());
			atmosphere.put(p.getFirst(), a / total);
		}

		this.atmosphere = MathUtil.sortByValue(this.atmosphere);
	}

	public int size() {
		return atmosphere.size();
	}
	
	public Map.Entry<FluidStack, Float> get(int i) {
		return new ArrayList<>(atmosphere.entrySet()).get(i);
	}
}