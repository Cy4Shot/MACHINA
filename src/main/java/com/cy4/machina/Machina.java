package com.cy4.machina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.GeckoLib;

@Mod(Machina.MOD_ID)
public class Machina {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "machina";
	 
	public Machina() {
		GeckoLib.initialize();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static final ItemGroup MACHINA_ITEM_GROUP = new ItemGroup(ItemGroup.TABS.length, "machinaItemGroup") {
		@Override
		public ItemStack makeIcon() {
			return ItemStack.EMPTY;
		}
	};
}
