package com.cy4.machina.client.renderer.tile;

import com.cy4.machina.client.models.tile.RocketTileModel;
import com.cy4.machina.tile_entities.RocketTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class RocketTileRenderer extends GeoBlockRenderer<RocketTile> {
    public RocketTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new RocketTileModel());
    }

    @Override
    public RenderType getRenderType(RocketTile animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}