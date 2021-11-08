package com.cy4.machina;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.config.ClientConfig;
import com.cy4.machina.config.CommonConfig;
import com.cy4.machina.config.ServerConfig;
import com.cy4.machina.events.TraitHandlers;
import com.cy4.machina.init.CommandInit;
import com.cy4.machina.init.ItemInit;
import com.cy4.machina.starchart.pool.PlanetTraitPoolManager;
import com.cy4.machina.world.DynamicDimensionHelper;
import com.cy4.machina.world.data.PlanetDimensionData;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(Machina.MOD_ID)
public class Machina {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "machina";

	public static PlanetTraitPoolManager traitPoolManager = new PlanetTraitPoolManager();
	public static final ResourceLocation MACHINA_ID = new ResourceLocation(MOD_ID, MOD_ID);
	
	public static final String CONFIG_DIR_PATH = "config/" + MOD_ID + "/";
	public static final File CONFIF_DIR = new File(CONFIG_DIR_PATH);

	public Machina() {
		GeckoLib.initialize();
		
		if (!CONFIF_DIR.exists()) {
			LOGGER.info("Created Machina config folder!");
			CONFIF_DIR.mkdirs();
		}

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		modBus.addListener(this::onCommonSetup);

		forgeBus.addListener(EventPriority.HIGH, this::onServerStart);
		forgeBus.addListener(EventPriority.HIGH, this::onRegisterCommands);
		forgeBus.addListener(EventPriority.HIGH, TraitHandlers::handleSuperHot);

		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.SPEC, MOD_ID + "/common.toml");
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.SPEC, MOD_ID + "/client.toml");
		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.SPEC, "machina-server.toml");
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static final ItemGroup MACHINA_ITEM_GROUP = new ItemGroup(ItemGroup.TABS.length, "machinaItemGroup") {

		@Override
		public ItemStack makeIcon() {
			//return BlockInit.ROCKET_PLATFORM_BLOCK.asItem().getDefaultInstance();
			return ItemInit.ITEM_GROUP_ICON.getDefaultInstance();
		}
	};

	public void onRegisterCommands(final RegisterCommandsEvent event) {
		CommandInit.registerCommands(event);
	}

	public void onCommonSetup(final FMLCommonSetupEvent event) {
		CapabilityPlanetTrait.register();
	}

	// Load all the worlds to ensure player spawn spots!
	@SuppressWarnings("resource")
	public void onServerStart(final FMLServerStartingEvent event) {
		PlanetDimensionData.getDefaultInstance(event.getServer()).dimensionIds.forEach(id -> {
			DynamicDimensionHelper.createPlanet(event.getServer(), id);
		});
	}
}
