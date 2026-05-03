package com.machina.datagen.client.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.IGeneratedBlockState;

public class CTMBlockStateBuilder implements IGeneratedBlockState {

	public static final ResourceLocation CTM_LOADER = ResourceLocation.fromNamespaceAndPath("athena", "ctm");

	private final ConfiguredModel model;
	private ResourceLocation loader;
	private Map<String, ResourceLocation> ctmTextures = new HashMap<>();
	private TagKey<Block> blockTag;

	public CTMBlockStateBuilder(ConfiguredModel model) {
		this.model = model;
	}

	public CTMBlockStateBuilder setLoader(ResourceLocation loader) {
		this.loader = loader;
		return this;
	}

	public CTMBlockStateBuilder addCTMTexture(String name, ResourceLocation texture) {
		this.ctmTextures.put(name, texture);
		return this;
	}

	public CTMBlockStateBuilder setCTMTag(TagKey<Block> blockTag) {
		this.blockTag = blockTag;
		return this;
	}

	@Override
	public JsonObject toJson() {
		JsonObject modelJson = new JsonObject();
		modelJson.addProperty("model", model.model.getLocation().toString());
		if (model.rotationX != 0)
			modelJson.addProperty("x", model.rotationX);
		if (model.rotationY != 0)
			modelJson.addProperty("y", model.rotationY);
		if (model.uvLock)
			modelJson.addProperty("uvlock", model.uvLock);
		if (model.weight != ConfiguredModel.DEFAULT_WEIGHT)
			modelJson.addProperty("weight", model.weight);

		JsonObject variants = new JsonObject();
		variants.add("", modelJson);

		JsonObject ctmTextures = new JsonObject();
		for (Entry<String, ResourceLocation> entry : this.ctmTextures.entrySet()) {
			ctmTextures.addProperty(entry.getKey(), entry.getValue().toString());
		}

		JsonObject main = new JsonObject();
		main.add("variants", variants);
		main.addProperty("athena:loader", this.loader.toString());
		main.add("ctm_textures", ctmTextures);

		if (this.blockTag != null) {
			JsonObject connectTo = new JsonObject();
			connectTo.addProperty("type", "tag");
			connectTo.addProperty("tag", this.blockTag.location().toString());
			main.add("connect_to", connectTo);
		}

		return main;
	}
}
