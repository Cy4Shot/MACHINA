package com.machina.world.data;

import java.util.UUID;

import javax.annotation.WillNotClose;

import com.machina.Machina;
import com.machina.network.BaseNetwork;
import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CResearchSync;
import com.machina.research.ResearchTree;
import com.machina.util.helper.ServerHelper;
import com.machina.util.nbt.BaseNBTMap;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class ResearchData extends WorldSavedData {

	// Variables + Constructor
	private static final String ID = Machina.MOD_ID + "_research";
	private final BaseNBTMap<UUID, ResearchTree, StringNBT, CompoundNBT> researches = createResearch();

	public ResearchData(String n) {
		super(n);
	}

	// Instance
	public static ResearchData getDefaultInstance(@WillNotClose MinecraftServer server) {
		ServerWorld w = server.getLevel(World.OVERWORLD);
		return w.getDataStorage().computeIfAbsent(() -> new ResearchData(ID), ID);
	}

	// Save + Load
	@Override
	public void load(CompoundNBT nbt) {
		researches.deserializeNBT(nbt.getCompound("researches"));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("researches", researches.serializeNBT());
		return nbt;
	}

	@Override
	public void setDirty() {
		syncWithClients();
		super.setDirty();
	}

	public void syncWithClients() {
		ServerHelper.server().getPlayerList().getPlayers().forEach(player -> syncClient(player));
	}

	public void syncClient(ServerPlayerEntity player) {
		ensurePlayer(player);
		BaseNetwork.sendTo(MachinaNetwork.CHANNEL, new S2CResearchSync(researches.get(player.getUUID())), player);
	}

	// Other Static Methods
	public final BaseNBTMap<UUID, ResearchTree, StringNBT, CompoundNBT> createResearch() {
		return new BaseNBTMap<>(id -> StringNBT.valueOf(id.toString()), ResearchTree::serializeNBT,
				nbt -> UUID.fromString(nbt.getAsString()), ResearchTree::fromNBT);
	}

	public void ensurePlayer(ServerPlayerEntity player) {
		UUID id = player.getUUID();
		if (!researches.containsKey(id)) {
			researches.put(id, new ResearchTree());
			setDirty();
		}
	}

	public static ResearchTree getResearchForPlayer(MinecraftServer server, ServerPlayerEntity player) {
		ResearchData data = ResearchData.getDefaultInstance(server);
		data.ensurePlayer(player);
		return data.researches.get(player.getUUID());
	}
}