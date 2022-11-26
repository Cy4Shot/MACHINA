package com.machina;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.config.ClientConfig;
import com.machina.registration.Registration;
import com.machina.util.text.MachinaRL;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib3.GeckoLib;

@Mod(Machina.MOD_ID)
public class Machina {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "machina";
	public static final MachinaRL MACHINA_ID = new MachinaRL(MOD_ID);

	public Machina() {
		Registration.register(FMLJavaModLoadingContext.get().getModEventBus());
		GeckoLib.initialize();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static String getVersion() {
		return "Alpha 0.1.0";
	}

	public static boolean isDevEnvironment() {
		return !FMLEnvironment.production || ClientConfig.devMode.get();
	}

	public static boolean isOsWindows() {
		return SystemUtils.IS_OS_WINDOWS;
	}
}