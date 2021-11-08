package com.cy4.machina;

import com.cy4.machina.firesTesting.TileEntityTypesInit;
import com.cy4.machina.init.BlockInit;
import com.cy4.machina.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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

		TileEntityTypesInit.TILE_ENTITY_TYPE.register(modBus);

	}



	public static final ItemGroup MACHINA_ITEM_GROUP = new ItemGroup(ItemGroup.TABS.length, "machinaItemGroup") {
		@Override
		public ItemStack makeIcon() {
			return BlockInit.ROCKET_PLATFORM_BLOCK.asItem().getDefaultInstance();
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> items) {
			//items.add(ItemInit.TEST_ITEM.getDefaultInstance());

			items.add(BlockInit.ROCKET_PLATFORM_BLOCK.asItem().getDefaultInstance());

			items.add(BlockInit.ANIMATED_BUILDER_MOUNT.asItem().getDefaultInstance());
			items.add(BlockInit.ANIMATED_BUILDER.asItem().getDefaultInstance());
			items.add(BlockInit.PAD_SIZE_RELAY.asItem().getDefaultInstance());
			items.add(BlockInit.CONSOLE.asItem().getDefaultInstance());

			items.add(BlockInit.ROCKET.asItem().getDefaultInstance());
		}
	};
}
