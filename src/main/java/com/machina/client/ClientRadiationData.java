package com.machina.client;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.level.ChunkPos;

public class ClientRadiationData {

	private static final Set<ChunkPos> irradiatedChunks = new HashSet<>();

	public static void setIrradiatedChunks(Set<ChunkPos> chunks) {
		irradiatedChunks.clear();
		irradiatedChunks.addAll(chunks);
	}

	public static void addChunk(ChunkPos pos) {
		irradiatedChunks.add(pos);
	}

	public static void removeChunk(ChunkPos pos) {
		irradiatedChunks.remove(pos);
	}

	public static void clear() {
		irradiatedChunks.clear();
	}

	public static boolean isChunkIrradiated(ChunkPos pos) {
		return irradiatedChunks.contains(pos);
	}

	public static boolean isChunkIrradiated(int chunkX, int chunkZ) {
		return irradiatedChunks.contains(new ChunkPos(chunkX, chunkZ));
	}

	public static Set<ChunkPos> getIrradiatedChunks() {
		return new HashSet<>(irradiatedChunks);
	}
}
