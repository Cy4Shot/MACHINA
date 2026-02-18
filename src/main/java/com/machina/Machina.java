package com.machina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.config.ClientConfig;
import com.machina.registration.Registration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Machina.MOD_ID)
public class Machina {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "machina";
    public static final String VERSION = "0.1.0";

    public Machina(IEventBus modEventBus, ModContainer modContainer) {
        Registration.register(modEventBus, modContainer);
    }

    public static boolean isDevEnvironment() {
        return !FMLEnvironment.production || ClientConfig.devMode.get();
    }
}