package com.machina.world.data;

import java.util.HashSet;

import com.machina.Machina;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class PlanetDimensionData extends SavedData {

	public final HashSet<String> dimensionIds = new HashSet<>();

	PlanetDimensionData() {
		super();
	}

	PlanetDimensionData(CompoundTag tag) {
		load(tag);
	}

	private static final String ID = Machina.MOD_ID + "_planet_dimensions";

	public static PlanetDimensionData getDefaultInstance(MinecraftServer server) {
		return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent((t) -> new PlanetDimensionData(t),
				() -> new PlanetDimensionData(), ID);
	}

	public void load(CompoundTag nbt) {
		dimensionIds.clear();
		ListTag listNBT = nbt.getList("dimensionIds", Tag.TAG_STRING);
		for (Tag inbt : listNBT) {
			StringTag stringNBT = (StringTag) inbt;
			dimensionIds.add(stringNBT.getAsString());
		}
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		ListTag listNBT = new ListTag();
		for (String str : dimensionIds) {
			listNBT.add(StringTag.valueOf(str));
		}
		nbt.put("dimensionIds", listNBT);
		nbt.putString("dataOwnerMod", Machina.MOD_ID);
		return nbt;
	}

	public void addId(String id) {
		dimensionIds.add(id);
		setDirty();
	}

	public void removeId(String id) {
		dimensionIds.remove(id);
		setDirty();
	}
}