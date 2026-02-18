package com.machina.api.client;

import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.StarchartGenerator;
import com.machina.api.starchart.obj.SolarSystem;

public class ClientStarchart {
	public static SolarSystem system;

	public static Starchart STARCHART = null;

	public static void sync(long seed) {
		system = StarchartGenerator.gen(seed, "Example");
	}
}
