package com.machina.datagen.client.anim;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.machina.Machina;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

public abstract class BaseAnimationProvider implements IDataProvider {

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private final Map<String, JsonObject> data = new TreeMap<>();

	String modid;
	private final DataGenerator generator;

	public BaseAnimationProvider(DataGenerator gen, String modid) {
		this.modid = modid;
		this.generator = gen;

	}

	@Override
	public void run(DirectoryCache cache) throws IOException {
		createAnimations();
		for (Map.Entry<String, JsonObject> entry : data.entrySet()) {
			saveAnimation(cache, entry.getValue(), entry.getKey());
		}
	}

	public abstract void createAnimations();

	public void noAnim(Block block) {
		JsonObject obj = new JsonObject();
		obj.addProperty("format_version", "1.8.0");
		obj.addProperty("geckolib_format_version", "2");
		obj.add("animations", new JsonObject());
		add(name(block), obj);
	}

	private void add(String key, JsonObject value) {
		if (data.put(key, value) != null)
			throw new IllegalStateException("Duplicate  key " + key);
	}

	private String name(Block block) {
		return block.getRegistryName().getPath();
	}

	private void saveAnimation(DirectoryCache cache, JsonObject animJson, String owner) {
		Path mainOutput = generator.getOutputFolder();
		String pathSuffix = "assets/" + modid + "/animations/" + owner + ".animation.json";
		Path outputPath = mainOutput.resolve(pathSuffix);
		try {
			IDataProvider.save(GSON, cache, animJson, outputPath);
		} catch (IOException e) {
			Machina.LOGGER.error("Couldn't save animation to {}", outputPath, e);
		}
	}

	@Override
	public String getName() {
		return "Geckolib Animations: " + modid;
	}
}