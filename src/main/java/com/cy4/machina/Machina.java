package com.cy4.machina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.init.CommandInit;
import com.cy4.machina.starchart.pool.PlanetTraitPoolManager;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(Machina.MOD_ID)
public class Machina {
	
  public static final Logger LOGGER = LogManager.getLogger();
  public static final String MOD_ID = "machina";
  
  public static final ItemGroup MACHINA_ITEM_GROUP = new ItemGroup(ItemGroup.TABS.length, "machinaItemGroup") {
		@Override
		public ItemStack makeIcon() {
			return ItemStack.EMPTY;
		}
	};
	
	public static PlanetTraitPoolManager TRAIT_POOL_MANAGER = new PlanetTraitPoolManager();
	 
	public Machina() {
		GeckoLib.initialize();
		
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		modBus.addListener(this::onCommonSetup);
		
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(EventPriority.HIGH, this::onRegisterCommands);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void onRegisterCommands(final RegisterCommandsEvent event) {
		CommandInit.registerCommands(event);
	}
	
	public void onCommonSetup(final FMLCommonSetupEvent event) {
		CapabilityPlanetTrait.register();
	}
}
