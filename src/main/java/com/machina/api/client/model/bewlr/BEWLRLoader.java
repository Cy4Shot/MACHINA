package com.machina.api.client.model.bewlr;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.machina.api.client.model.bewlr.BEWLRLoader.BEWLRGeometry;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class BEWLRLoader implements IGeometryLoader<BEWLRGeometry> {

    public static final BEWLRLoader INSTANCE = new BEWLRLoader();

    @Override
    public BEWLRGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
            throws JsonParseException {
        return new BEWLRGeometry();
    }

    public static class BEWLRGeometry implements IUnbakedGeometry<BEWLRGeometry> {

        public BEWLRGeometry() {
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                               Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides,
                               ResourceLocation modelLocation) {
            return new BEWLRItemModel(context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight());
        }
    }
}
