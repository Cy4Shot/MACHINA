package com.machina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.client.dimension.CustomDimensionRenderInfo;
import com.machina.registration.Registration;
import com.machina.util.MachinaRL;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
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
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			CustomDimensionRenderInfo.registerDimensionRenderInfo();
		});

		Registration.register(FMLJavaModLoadingContext.get().getModEventBus());
		GeckoLib.initialize();
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static boolean isDevEnvironment() {
		return !FMLEnvironment.production;
	}
}
