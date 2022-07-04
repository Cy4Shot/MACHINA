package com.machina.planet.attribute.serializers;

import java.util.Random;
import java.util.function.Function;

public class ChanceSerializer extends FloatSerializer {

	public ChanceSerializer(Float def, Function<Random, Float> gen, int scale) {
		super(def, gen, t -> t * scale);
	}
}
