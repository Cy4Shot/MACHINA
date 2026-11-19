package com.machina.datagen.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;

import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.machina.Machina;
import com.machina.registration.init.FamiliesInit;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DatagenOreTextures implements DataProvider {

	private final PackOutput output;
	private final ExistingFileHelper existingFileHelper;
	private final Path modResourcesPath;

	public DatagenOreTextures(PackOutput output, ExistingFileHelper existingFileHelper, Path modResourcesPath) {
		this.output = output;
		this.existingFileHelper = existingFileHelper;
		this.modResourcesPath = modResourcesPath;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		List<CompletableFuture<?>> futures = new ArrayList<>();

		FamiliesInit.ORES.forEach(fam -> {
			fam.ore().ifPresent(ores -> {
				ResourceLocation oreName = ores.name();
				ores.map().forEach((baseKey, oreBlock) -> {
					ResourceLocation baseLocation = baseKey.location();
					String blockName = BuiltInRegistries.BLOCK.getKey(oreBlock.get()).getPath();
					futures.add(CompletableFuture.runAsync(() -> {
						generateCompositeTexture(cache, baseLocation, oreName, blockName);
						generateBlockModel(cache, blockName);
						generateItemModel(cache, blockName);
					}));
				});
			});
		});

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}

	@SuppressWarnings("deprecation")
	private void generateCompositeTexture(CachedOutput cache, ResourceLocation base, ResourceLocation ore,
			String outputName) {
		try {
			BufferedImage baseImage = loadTexture(base);
			BufferedImage oreImage = loadTexture(ore);

			if (baseImage == null) {
				Machina.LOGGER.warn("Could not load base texture: {} for ore {}", base, outputName);
				return;
			}
			if (oreImage == null) {
				Machina.LOGGER.warn("Could not load ore texture: {} for ore {}", ore, outputName);
				return;
			}

			BufferedImage result = compositeImages(baseImage, oreImage);

			Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(Machina.MOD_ID)
					.resolve("textures").resolve("block").resolve("ore").resolve(outputName + ".png");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(result, "PNG", baos);
			byte[] data = baos.toByteArray();

			cache.writeIfNeeded(outputPath, data, Hashing.sha1().hashBytes(data));

		} catch (IOException e) {
			Machina.LOGGER.error("Failed to generate composite ore texture for {}", outputName, e);
		}
	}

	@SuppressWarnings("deprecation")
	private void generateBlockModel(CachedOutput cache, String blockName) {
		try {
			JsonObject model = new JsonObject();
			model.addProperty("parent", "minecraft:block/cube_all");

			JsonObject textures = new JsonObject();
			textures.addProperty("all", Machina.MOD_ID + ":block/ore/" + blockName);
			model.add("textures", textures);

			Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(Machina.MOD_ID)
					.resolve("models").resolve("block").resolve(blockName + ".json");

			byte[] data = model.toString().getBytes(StandardCharsets.UTF_8);
			cache.writeIfNeeded(outputPath, data, Hashing.sha1().hashBytes(data));

		} catch (IOException e) {
			Machina.LOGGER.error("Failed to generate block model for {}", blockName, e);
		}
	}

	@SuppressWarnings("deprecation")
	private void generateItemModel(CachedOutput cache, String blockName) {
		try {
			JsonObject model = new JsonObject();
			model.addProperty("parent", Machina.MOD_ID + ":block/" + blockName);

			Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(Machina.MOD_ID)
					.resolve("models").resolve("item").resolve(blockName + ".json");

			byte[] data = model.toString().getBytes(StandardCharsets.UTF_8);
			cache.writeIfNeeded(outputPath, data, Hashing.sha1().hashBytes(data));

		} catch (IOException e) {
			Machina.LOGGER.error("Failed to generate item model for {}", blockName, e);
		}
	}

	private BufferedImage loadTexture(ResourceLocation location) {
		String namespace = location.getNamespace();
		String texturePath = "textures/block/" + location.getPath() + ".png";
		ResourceLocation fullLoc = ResourceLocation.fromNamespaceAndPath(namespace, texturePath);

		if (namespace.equals(Machina.MOD_ID)) {
			Path localPath = modResourcesPath.resolve("assets").resolve(namespace).resolve(texturePath);
			if (Files.exists(localPath)) {
				try (InputStream stream = Files.newInputStream(localPath)) {
					return ImageIO.read(stream);
				} catch (IOException e) {
					Machina.LOGGER.debug("Could not load mod texture from local path: {}", localPath);
				}
			}
		}

		try {
			if (existingFileHelper.exists(fullLoc, PackType.CLIENT_RESOURCES)) {
				Resource resource = existingFileHelper.getResource(fullLoc, PackType.CLIENT_RESOURCES);
				try (InputStream stream = resource.open()) {
					return ImageIO.read(stream);
				}
			}
		} catch (IOException e) {
			Machina.LOGGER.debug("Could not load texture via ExistingFileHelper: {}", fullLoc);
		}

		try {
			String resourcePath = "/assets/" + namespace + "/" + texturePath;
			InputStream stream = getClass().getResourceAsStream(resourcePath);
			if (stream != null) {
				BufferedImage img = ImageIO.read(stream);
				stream.close();
				return img;
			}
		} catch (IOException e) {
			Machina.LOGGER.debug("Could not load texture from classpath: {}", location);
		}

		return null;
	}

	private BufferedImage compositeImages(BufferedImage base, BufferedImage overlay) {
		int width = Math.max(base.getWidth(), overlay.getWidth());
		int height = Math.max(base.getHeight(), overlay.getHeight());

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int baseX = x % base.getWidth();
				int baseY = y % base.getHeight();
				int overlayX = x % overlay.getWidth();
				int overlayY = y % overlay.getHeight();

				int baseColor = base.getRGB(baseX, baseY);
				int overlayColor = overlay.getRGB(overlayX, overlayY);

				int blended = alphaBlend(baseColor, overlayColor);
				result.setRGB(x, y, blended);
			}
		}

		return result;
	}

	private int alphaBlend(int background, int foreground) {
		int fgA = (foreground >> 24) & 0xFF;
		int fgR = (foreground >> 16) & 0xFF;
		int fgG = (foreground >> 8) & 0xFF;
		int fgB = foreground & 0xFF;

		int bgA = (background >> 24) & 0xFF;
		int bgR = (background >> 16) & 0xFF;
		int bgG = (background >> 8) & 0xFF;
		int bgB = background & 0xFF;

		if (fgA == 0) {
			return background;
		}
		if (fgA == 255) {
			return foreground;
		}

		float alpha = fgA / 255.0f;
		float invAlpha = 1.0f - alpha;

		int outR = (int) (fgR * alpha + bgR * invAlpha);
		int outG = (int) (fgG * alpha + bgG * invAlpha);
		int outB = (int) (fgB * alpha + bgB * invAlpha);
		int outA = Math.max(fgA, bgA);

		return (outA << 24) | (outR << 16) | (outG << 8) | outB;
	}

	@Override
	public String getName() {
		return "Machina Ore Textures";
	}
}
