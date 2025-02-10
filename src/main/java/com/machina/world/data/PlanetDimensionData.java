package com.machina.world.data;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.primitives.Ints;
import com.machina.Machina;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

public class PlanetDimensionData extends SavedData {

	public final Map<Integer, Set<Integer>> ids = new HashMap<>();
	public long lastKnownSeed = 0L;

	PlanetDimensionData() {
		super();
	}

	public PlanetDimensionData(CompoundTag tag) {
		load(tag);
	}

	public static final String ID = Machina.MOD_ID + "_planet_dimensions";

	public static PlanetDimensionData getDefaultInstance(MinecraftServer server) {
		return getDefaultInstance(Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage());
	}

	public static PlanetDimensionData getDefaultInstance(DimensionDataStorage storage) {
		return storage.computeIfAbsent(PlanetDimensionData::new, PlanetDimensionData::new, ID);
	}

	public void load(CompoundTag nbt) {
		ids.clear();
		ListTag listNBT = nbt.getList("ids", Tag.TAG_COMPOUND);
		for (Tag cnbt : listNBT) {
			CompoundTag tag = (CompoundTag) cnbt;
			int dim = tag.getInt("dim");
			int[] biomeNBT = tag.getIntArray("biomes");
			ids.put(dim, new HashSet<>(Ints.asList(biomeNBT)));
		}
		this.lastKnownSeed = nbt.getLong("lastKnownSeed");
	}

	@Override
	public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
		ListTag listNBT = new ListTag();
		for (Entry<Integer, Set<Integer>> e : ids.entrySet()) {
			CompoundTag tag = new CompoundTag();
			tag.putInt("dim", e.getKey());
			tag.putIntArray("biomes", e.getValue().stream().mapToInt(i -> i).toArray());
			listNBT.add(tag);
		}
		nbt.put("ids", listNBT);
		nbt.putLong("lastKnownSeed", lastKnownSeed);
		nbt.putString("dataOwnerMod", Machina.MOD_ID);
		return nbt;
	}

	public void updateSeed(long seed) {
		this.lastKnownSeed = seed;
		setDirty();
	}

	public void addId(int id) {
		ids.put(id, new HashSet<>());
		setDirty();
	}

	public void removeId(int id) {
		ids.remove(id);
		setDirty();
	}

	public void addBiome(int dim, int id) {
		ids.get(dim).add(id);
		setDirty();
	}
}