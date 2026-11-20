package com.machina.world.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.machina.Machina;
import com.machina.api.network.s2c.S2CRadiationSync;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;

public class RadiationData extends SavedData {

	public static final long PERMANENT = -1L;

	private static final Factory<RadiationData> FACTORY = new Factory<>(RadiationData::new, RadiationData::new);
	public static final String ID = Machina.MOD_ID + "_radiation";

	private final Map<ChunkPos, Long> irradiatedChunks = new HashMap<>();
	private ServerLevel level;

	public RadiationData() {
		super();
	}

	public RadiationData(CompoundTag tag, Provider registries) {
		load(tag);
	}

	public static RadiationData get(ServerLevel level) {
		RadiationData data = level.getDataStorage().computeIfAbsent(FACTORY, ID);
		data.level = level;
		return data;
	}

	public void syncToAllPlayers() {
		if (level == null) return;
		S2CRadiationSync packet = new S2CRadiationSync(getIrradiatedChunks());
		for (ServerPlayer player : level.players()) {
			PacketDistributor.sendToPlayer(player, packet);
		}
	}

	public void syncToPlayer(ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, new S2CRadiationSync(getIrradiatedChunks()));
	}

	private void load(CompoundTag tag) {
		irradiatedChunks.clear();
		ListTag listTag = tag.getList("irradiatedChunks", Tag.TAG_COMPOUND);
		for (Tag t : listTag) {
			CompoundTag chunkTag = (CompoundTag) t;
			int x = chunkTag.getInt("x");
			int z = chunkTag.getInt("z");
			long expiresTick = chunkTag.contains("expiresTick") ? chunkTag.getLong("expiresTick") : PERMANENT;
			irradiatedChunks.put(new ChunkPos(x, z), expiresTick);
		}
	}

	@Override
	public CompoundTag save(CompoundTag tag, Provider registries) {
		ListTag listTag = new ListTag();
		for (Map.Entry<ChunkPos, Long> entry : irradiatedChunks.entrySet()) {
			CompoundTag chunkTag = new CompoundTag();
			chunkTag.putInt("x", entry.getKey().x);
			chunkTag.putInt("z", entry.getKey().z);
			chunkTag.putLong("expiresTick", entry.getValue());
			listTag.add(chunkTag);
		}
		tag.put("irradiatedChunks", listTag);
		tag.putString("dataOwnerMod", Machina.MOD_ID);
		return tag;
	}

	public void tick(long currentGameTime) {
		boolean changed = false;
		Iterator<Map.Entry<ChunkPos, Long>> it = irradiatedChunks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<ChunkPos, Long> entry = it.next();
			long expiresTick = entry.getValue();
			if (expiresTick != PERMANENT && currentGameTime >= expiresTick) {
				it.remove();
				changed = true;
			}
		}
		if (changed) {
			setDirty();
			syncToAllPlayers();
		}
	}

	public boolean isChunkIrradiated(ChunkPos pos) {
		return irradiatedChunks.containsKey(pos);
	}

	public boolean isChunkIrradiated(int chunkX, int chunkZ) {
		return irradiatedChunks.containsKey(new ChunkPos(chunkX, chunkZ));
	}

	public long getRemainingTicks(ChunkPos pos, long currentGameTime) {
		Long expiresTick = irradiatedChunks.get(pos);
		if (expiresTick == null) {
			return 0;
		}
		if (expiresTick == PERMANENT) {
			return PERMANENT;
		}
		return Math.max(0, expiresTick - currentGameTime);
	}

	public void setChunkIrradiated(ChunkPos pos, long durationTicks, long currentGameTime) {
		long expiresTick = durationTicks == PERMANENT ? PERMANENT : currentGameTime + durationTicks;
		irradiatedChunks.put(pos, expiresTick);
		setDirty();
		syncToAllPlayers();
	}

	public void setChunkIrradiated(int chunkX, int chunkZ, long durationTicks, long currentGameTime) {
		setChunkIrradiated(new ChunkPos(chunkX, chunkZ), durationTicks, currentGameTime);
	}

	public void setChunkIrradiatedPermanent(ChunkPos pos) {
		irradiatedChunks.put(pos, PERMANENT);
		setDirty();
		syncToAllPlayers();
	}

	public void setChunkIrradiatedPermanent(int chunkX, int chunkZ) {
		setChunkIrradiatedPermanent(new ChunkPos(chunkX, chunkZ));
	}

	public void clearChunkRadiation(ChunkPos pos) {
		if (irradiatedChunks.remove(pos) != null) {
			setDirty();
			syncToAllPlayers();
		}
	}

	public void clearChunkRadiation(int chunkX, int chunkZ) {
		clearChunkRadiation(new ChunkPos(chunkX, chunkZ));
	}

	public Set<ChunkPos> getIrradiatedChunks() {
		return new HashSet<>(irradiatedChunks.keySet());
	}

	public void clearAll() {
		if (!irradiatedChunks.isEmpty()) {
			irradiatedChunks.clear();
			setDirty();
			syncToAllPlayers();
		}
	}
}
