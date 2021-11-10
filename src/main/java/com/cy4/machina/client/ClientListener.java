package com.cy4.machina.client;

import com.cy4.machina.Machina;
import com.cy4.machina.client.particle.ElectricitySparkParticle;
import com.cy4.machina.client.renderer.tile.RocketTileRenderer;
import com.cy4.machina.init.BlockInit;
import com.cy4.machina.init.FluidInit;
import com.cy4.machina.init.ParticleTypesInit;
import com.cy4.machina.init.TileEntityTypesInit;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.Fluid;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientListener {
	
    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.ROCKET_TILE, RocketTileRenderer::new);
        RenderTypeLookup.setRenderLayer(BlockInit.ROCKET, RenderType.cutout());
        
        translucent(FluidInit.LIQUID_HYDROGEN.get());
        translucent(FluidInit.LIQUID_HYDROGEN_FLOWING.get());
        translucent(FluidInit.LIQUID_HYDROGEN_BLOCK);
    }
    
    private static void translucent(Fluid fluid) {
    	RenderTypeLookup.setRenderLayer(fluid, RenderType.translucent());
    }
    
    private static void translucent(Block block) {
    	RenderTypeLookup.setRenderLayer(block, RenderType.translucent());
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onParticleFactoryRegister(ParticleFactoryRegisterEvent event) {
    	Minecraft mc = Minecraft.getInstance();
    	mc.particleEngine.register(ParticleTypesInit.ELECTRICITY_SPARK, ElectricitySparkParticle.Factory::new);
    }
}
