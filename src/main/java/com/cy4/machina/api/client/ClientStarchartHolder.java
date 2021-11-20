package com.cy4.machina.api.client;

import com.cy4.machina.starchart.Starchart;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientStarchartHolder {
	private static Starchart starchart;

	public static Starchart getStarchart() {
		return starchart;
	}

	public static void setStarchart(Starchart starchart) {
		ClientStarchartHolder.starchart = starchart;
	}
}
