package com.cy4.machina.firesTesting.client;

import com.cy4.machina.Machina;
import com.cy4.machina.firesTesting.BlockInit;
import com.cy4.machina.firesTesting.TileEntityTypesInit;
import com.cy4.machina.firesTesting.client.renderer.tile.RocketTileRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.example.client.renderer.tile.BotariumTileRenderer;
import software.bernie.example.registry.BlockRegistry;
import software.bernie.example.registry.TileRegistry;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.ROCKET_TILE.get(), RocketTileRenderer::new);
        RenderTypeLookup.setRenderLayer(BlockInit.ROCKET.get(), RenderType.cutout());
    }
}
