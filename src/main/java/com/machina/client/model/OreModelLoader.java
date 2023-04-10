package com.machina.client.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.machina.registration.init.BlockInit;
import com.machina.util.text.MachinaRL;
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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class OreModelLoader implements IModelLoader<OreModelLoader.OreModelGeometry> {

	public static final ResourceLocation ID = new MachinaRL("ore_model_loader");

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	public OreModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
		return new OreModelGeometry();
	}

	public static class OreModelGeometry implements IModelGeometry<OreModelGeometry> {

		@Override
		public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
				Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
				ItemOverrideList overrides, ResourceLocation modelLocation) {
			return new OreModel(modelTransform, overrides, owner.getCameraTransforms());
		}

		@Override
		public Collection<RenderMaterial> getTextures(IModelConfiguration owner,
				Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
			Set<RenderMaterial> textures = new HashSet<>();
			BlockInit.ORE_MAP.values().forEach(m -> {
				m.values().forEach(b -> {
					textures.addAll(modelGetter.apply(b.get().getRegistryName()).getDependencies().stream()
							.map(rl -> ForgeHooksClient.getBlockMaterial(rl)).collect(Collectors.toList()));
				});
			});
			return textures;
		}
	}
}