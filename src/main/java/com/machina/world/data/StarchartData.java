package com.machina.world.data;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.WillNotClose;

import com.machina.Machina;
import com.machina.config.CommonConfig;
import com.machina.network.BaseNetwork;
import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CStarchartSync;
import com.machina.planet.trait.type.IPlanetTraitType;
import com.machina.registration.init.AttributeInit;
import com.machina.util.MachinaRL;
import com.machina.util.nbt.BaseNBTMap;
import com.machina.util.server.PlanetUtils;
import com.machina.util.server.ServerHelper;
import com.machina.util.text.StringUtils;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class StarchartData extends WorldSavedData {

	// Variables + Constructor
	private static final String ID = Machina.MOD_ID + "_starchart";

	private final BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> starchart = createEmptyStarchart();

	private boolean isGenerated;

	public StarchartData(String n) {
		super(n);
		isGenerated = false;
	}

	// Instance
	public static StarchartData getDefaultInstance(@WillNotClose MinecraftServer server) {
		ServerWorld w = server.getLevel(World.OVERWORLD);
		return w.getDataStorage().computeIfAbsent(() -> new StarchartData(ID), ID);
	}

	// Save + Load
	@Override
	public void load(CompoundNBT nbt) {
		isGenerated = nbt.getBoolean("generated");
		starchart.deserializeNBT(nbt.getCompound("starchart"));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putBoolean("generated", isGenerated);
		nbt.put("starchart", starchart.serializeNBT());
		return nbt;
	}

	public BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> getStarchart() {
		return starchart;
	}

	public <TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(ResourceLocation dimensionId,
			Class<TYPE> typeClass) {
		return starchart.computeIfAbsent(dimensionId, k -> new PlanetData()).getTraits().stream()
				.filter(typeClass::isInstance).map(typeClass::cast).collect(Collectors.toList());
	}

	public void setGenerated(boolean gen) {
		isGenerated = gen;
		this.setDirty();
	}

	@Override
	public void setDirty() {
		syncWithClients();
		super.setDirty();
	}

	public void syncWithClients() {
		BaseNetwork.sendToAll(MachinaNetwork.CHANNEL, new S2CStarchartSync(starchart));
	}

	public void syncClient(ServerPlayerEntity player) {
		BaseNetwork.sendTo(MachinaNetwork.CHANNEL, new S2CStarchartSync(starchart), player);
	}

	public boolean getGenerated() {
		return isGenerated;
	}

	public void generateIf(long seed) {
		if (!isGenerated) {

			Random rand = new Random(seed);
			int min = CommonConfig.minPlanets.get();
			int max = CommonConfig.maxPlanets.get();
			int num = min + rand.nextInt(max - min);

			for (int i = 0; i < num; i++) {
				starchart.put(new MachinaRL(i), PlanetData.fromRand(rand));
			}

			isGenerated = true;
			this.setDirty();
		}
	}

	// Static Getters
	public static BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> getStarchartForServer(
			MinecraftServer server) {
		return StarchartData.getDefaultInstance(server).getStarchart();
	}

	public int getNumPlanets() {
		return this.starchart.size();
	}

	public void debugStarchart() {
		StringUtils.printlnUtf8("Planets");
		for (int i = 0; i < starchart.size(); i++) {
			PlanetData p = starchart.values().stream().collect(Collectors.toList()).get(i);
			StringUtils.printlnUtf8((i == starchart.values().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F)
					+ StringUtils.TREE_H + p.getAttributeFormatted(AttributeInit.PLANET_NAME));
			for (int j = 0; j < p.getTraits().size(); j++) {
				StringUtils.printlnUtf8((i == starchart.values().size() - 1 ? " " : StringUtils.TREE_V) + " "
						+ (j == p.getTraits().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
						+ p.getTraits().get(j).toString());
			}
		}
	}

	public static BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> createEmptyStarchart() {
		return new BaseNBTMap<>(rl -> StringNBT.valueOf(rl.toString()), PlanetData::serializeNBT,
				nbt -> new ResourceLocation(nbt.getAsString()), PlanetData::fromNBT);
	}

	public static PlanetData getDataForDimension(MinecraftServer server, RegistryKey<World> dim) {
		return PlanetUtils.isDimensionPlanet(dim) ? getDataForDimension(server, PlanetUtils.getId(dim))
				: PlanetData.NONE;
	}

	public static PlanetData getDataOrNone(MinecraftServer server, RegistryKey<World> dim) {
		return PlanetUtils.isDimensionPlanet(dim) ? StarchartData.getDataForDimension(ServerHelper.server(), dim)
				: PlanetData.NONE;
	}

	public static PlanetData getDataForDimension(MinecraftServer server, int id) {
		return getStarchartForServer(server).computeIfAbsent(new MachinaRL(id), rl -> PlanetData.NONE);
	}

}
