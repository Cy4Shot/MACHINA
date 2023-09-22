package com.machina.api.starchart.obj;

import com.machina.api.util.math.BetterRandom;

public class Test {
	public static void main(String[] args) {
		for(int i = 0; i < 1000; i++) {
			System.out.println( new BetterRandom().nextRange(5, 10));
		}
	}
}
