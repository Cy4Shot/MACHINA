package com.machina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.api.util.MachinaRL;
import com.machina.config.ClientConfig;
import com.machina.registration.Registration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Machina.MOD_ID)
public class Machina {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "machina";
    public static final MachinaRL MACHINA_ID = MachinaRL.create(MOD_ID);

    public Machina(IEventBus modEventBus, ModContainer modContainer) {
        Registration.register(modEventBus, modContainer);
        NeoForge.EVENT_BUS.register(this);
    }

    public static String getVersion() {
        return "Alpha 0.1.0";
    }

    public static boolean isDevEnvironment() {
        return !FMLEnvironment.production || ClientConfig.devMode.get();
    }
}