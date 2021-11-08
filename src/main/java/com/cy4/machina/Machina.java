package com.cy4.machina;

import com.cy4.machina.firesTesting.BlockInit;
import com.cy4.machina.firesTesting.MachinaItemGroup;
import com.cy4.machina.firesTesting.TileEntityTypesInit;
import com.cy4.machina.init.ItemInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;

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

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		//bus.addListener(this::setup);
		//modBus.addListener(this::clientSetup);
		//modBus.addListener(this::gatherData);

		//Registries
		DeferredRegister<?>[] registers = {
				BlockInit.BLOCKS,
				TileEntityTypesInit.TILE_ENTITY_TYPE,
				ItemInit.ITEMS
		};

		for (DeferredRegister<?> register : registers) {
			register.register(modBus);
		}
	}

	public static final ItemGroup MACHINA_ITEM_GROUP = new MachinaItemGroup("machina_item_group");

	/**
	public static final ItemGroup MACHINA_ITEM_GROUP = new ItemGroup(ItemGroup.TABS.length, "machinaItemGroup") {
		@Override
		public ItemStack makeIcon() {
			return ItemStack.EMPTY;
		}
	};
	 **/
}
