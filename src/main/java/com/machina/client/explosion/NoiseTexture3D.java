package com.machina.client.explosion;

import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryUtil;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class NoiseTexture3D {

	private static int textureId = -1;
	private static final ResourceLocation VOLUME_LOCATION = MachinaRL.create(Machina.MOD_ID, "textures/explosion/volume.bin");

	public static int getTextureId() {
		return textureId;
	}

	public static boolean isLoaded() {
		return textureId != -1;
	}

	public static void load(ResourceManager resourceManager) {
		if (textureId != -1) {
			return; // Already loaded
		}

		try (InputStream stream = resourceManager.getResource(VOLUME_LOCATION).orElseThrow().open()) {
			byte[] fileData = stream.readAllBytes();

			// Parse header: BIN\0 + width + height + depth + channels (20 bytes)
			if (fileData.length < 20) {
				Machina.LOGGER.error("Volume file too small");
				return;
			}

			int width = readInt(fileData, 4);
			int height = readInt(fileData, 8);
			int depth = readInt(fileData, 12);
			int channels = readInt(fileData, 16);

			Machina.LOGGER.info("Loading 3D noise texture: {}x{}x{}, {} channels", width, height, depth, channels);

			int dataSize = width * height * depth * channels;
			if (fileData.length < 20 + dataSize) {
				Machina.LOGGER.error("Volume file data incomplete");
				return;
			}

			// Create 3D texture
			textureId = GL11.glGenTextures();
			GL12.glBindTexture(GL12.GL_TEXTURE_3D, textureId);

			// Upload data
			ByteBuffer buffer = MemoryUtil.memAlloc(dataSize);
			buffer.put(fileData, 20, dataSize);
			buffer.flip();

			GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RED, width, height, depth, 0,
					GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, buffer);

			MemoryUtil.memFree(buffer);

			// Set texture parameters - trilinear filtering with repeat wrap
			GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL12.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL11.GL_REPEAT);

			GL12.glBindTexture(GL12.GL_TEXTURE_3D, 0);

			Machina.LOGGER.info("3D noise texture loaded successfully, id={}", textureId);

		} catch (Exception e) {
			Machina.LOGGER.error("Failed to load 3D noise texture", e);
		}
	}

	private static int readInt(byte[] data, int offset) {
		return (data[offset] & 0xFF) |
			   ((data[offset + 1] & 0xFF) << 8) |
			   ((data[offset + 2] & 0xFF) << 16) |
			   ((data[offset + 3] & 0xFF) << 24);
	}

	public static void bind(int unit) {
		if (textureId != -1) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
			GL12.glBindTexture(GL12.GL_TEXTURE_3D, textureId);
		}
	}
}
