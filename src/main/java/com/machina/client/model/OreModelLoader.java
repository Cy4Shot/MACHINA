package com.machina.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.machina.client.model.OreModelLoader.OreModelGeometry;
import com.machina.util.MachinaRL;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class OreModelLoader implements IModelLoader<OreModelGeometry> {

	public static final ResourceLocation ID = new MachinaRL("ore_model_loader");

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	public OreModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {

		JsonObject tx = modelContents.getAsJsonObject("textures");
		ResourceLocation pt = new ResourceLocation(tx.get("particle").getAsString());
		ResourceLocation bg = new ResourceLocation(tx.get("bg").getAsString());
		ResourceLocation fg = new ResourceLocation(tx.get("fg").getAsString());
		return new OreModelGeometry(pt, bg, fg);
	}

	public static class OreModelGeometry implements IModelGeometry<OreModelGeometry> {

		private final ResourceLocation pt;
		private final ResourceLocation bg;
		private final ResourceLocation fg;

		public OreModelGeometry(ResourceLocation pt, ResourceLocation bg, ResourceLocation fg) {
			this.pt = pt;
			this.bg = bg;
			this.fg = fg;
		}

		@Override
		public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
				Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
				ItemOverrideList overrides, ResourceLocation modelLocation) {
			return new OreModel(pt, bg, fg);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Collection<RenderMaterial> getTextures(IModelConfiguration owner,
				Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
			return Collections.EMPTY_LIST;
		}
	}
}