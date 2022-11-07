package com.machina.planet.attribute.serializers;

public class ChanceSerializer extends FloatSerializer {

	public ChanceSerializer(String name, float d, float mi, float ma, int scale) {
		super(name, d, mi, ma, t -> t * scale);
	}
}