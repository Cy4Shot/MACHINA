package com.machina.api.client.model.connector;

import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.machina.api.client.model.connector.ConnectorModelLoader.ConnectorGeometry;

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

public class ConnectorModelLoader implements IGeometryLoader<ConnectorGeometry> {

	public static final ConnectorModelLoader INSTANCE = new ConnectorModelLoader();

	@Override
	public ConnectorGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
			throws JsonParseException {
		String type = jsonObject.get("type").getAsString();
		return new ConnectorGeometry(type);
	}

	public static class ConnectorGeometry implements IUnbakedGeometry<ConnectorGeometry> {
		
		private final String type;
		
		public ConnectorGeometry(String type) {
			this.type = type;
		}

		@Override
		public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
				Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides,
				ResourceLocation modelLocation) {
			return new ConnectorModel(context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(),
					spriteGetter, type);
		}
	}
}
