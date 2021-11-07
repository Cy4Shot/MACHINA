package com.cy4.machina;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.GeckoLib;

@Mod("machina")
public class Machina {
	 
	public Machina() {
		GeckoLib.initialize();
		MinecraftForge.EVENT_BUS.register(this);
	}
}
