/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2021 Machina Team
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.starchart;

import com.google.common.collect.ImmutableMap;
import com.machina.api.api_extension.ApiExtension;
import com.machina.api.starchart.Starchart;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.StringUtils;
import com.machina.api.world.data.PlanetData;
import com.machina.config.CommonConfig;
import com.machina.init.PlanetAttributeTypesInit;
import com.machina.network.MachinaNetwork;
import com.machina.network.message.S2CStarchartSyncMessage;
import com.matyrobbrt.lib.network.BaseNetwork;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static com.machina.api.ModIDs.MACHINA;

public class StarchartImpl extends WorldSavedData implements Starchart {

    private static final String ID = MACHINA + "_starchart";

    /**
     * Use an API Extension in order for the API to have access to the starchart
     */
    @ApiExtension(StarchartGetter.class)
    private static final StarchartGetter API_STARCHART_GETTER = server -> server.getLevel(World.OVERWORLD)
            .getDataStorage().computeIfAbsent(() -> new StarchartImpl(ID), ID);

    @ApiExtension(StarchartSerializer.class)
    private static final Starchart.StarchartSerializer SERIALIZER = new StarchartSerializer() {
        @Override
        public CompoundNBT serializeNBT(Starchart starchart) {
            if (starchart instanceof StarchartImpl) {
                return ((StarchartImpl) starchart).save(new CompoundNBT());
            }
            return new CompoundNBT();
        }

        @Override
        public Starchart deserializeNBT(CompoundNBT nbt) {
            StarchartImpl starchart = new StarchartImpl(ID);
            starchart.deserializeNBT(nbt);
            return starchart;
        }
    };

    @ApiExtension(StarchartSyncer.class)
    public static final StarchartSyncer SYNCER = new StarchartSyncer() {
        @Override
        public void syncWithClients(Starchart starchart) {
            BaseNetwork.sendToAll(MachinaNetwork.CHANNEL, new S2CStarchartSyncMessage(starchart));
        }

        @Override
        public void syncWithPlayer(Starchart starchart, ServerPlayerEntity player) {
            BaseNetwork.sendTo(MachinaNetwork.CHANNEL, new S2CStarchartSyncMessage(starchart), player);
        }
    };

    private final PlanetDataMap planetData = new PlanetDataMap();

    private boolean isGenerated;

    private StarchartImpl(String name) {
        super(name);
        isGenerated = false;
    }

    @Override
    public Map<ResourceLocation, PlanetData> getAllPlanetData() {
        return ImmutableMap.copyOf(planetData);
    }

    // Save + Load
    @Override
    public void load(CompoundNBT nbt) {
        isGenerated = nbt.getBoolean("generated");
        planetData.deserializeNBT(nbt.getCompound("PlanetData"));
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putBoolean("generated", isGenerated);
        nbt.put("PlanetData", planetData.serializeNBT());
        return nbt;
    }

    @Override
    public PlanetData getDataForLevel(ResourceLocation dimensionId) {
        return planetData.get(dimensionId);
    }

    @Override
    public void setGenerated(boolean gen) {
        isGenerated = gen;
        this.setDirty();
    }

    @Override
    public void setDirty() {
        super.setDirty();
        cache();
        syncWithClients();
    }

    @Override
    public void setChanged() {
        setDirty();
    }

    public void cache() {
        // TODO Here we should cache the trait types, because streams are not the
        // fastest things in the world
    }

    @Override
    public boolean isGenerated() {
        return isGenerated;
    }

    @Override
    public void generateIfNotExists(long seed) {
        if (!isGenerated) {

            Random rand = new Random(seed);
            int min = CommonConfig.minPlanets.get();
            int max = CommonConfig.maxPlanets.get();
            int num = min + rand.nextInt(max - min);

            for (int i = 0; i < num; i++) {
                planetData.put(new MachinaRL(i), PlanetData.fromRand(rand));
            }

            isGenerated = true;
            this.setDirty();
        }
    }

    public static void debugStarchart(Starchart starchart) {
        StringUtils.printlnUtf8("Planets");
        for (int i = 0; i < starchart.getAllPlanetData().size(); i++) {
            PlanetData p = starchart.getAllPlanetData().values().stream().collect(Collectors.toList()).get(i);
            StringUtils.printlnUtf8((i == starchart.getAllPlanetData().values().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F)
                    + StringUtils.TREE_H + p.getAttributeFormatted(PlanetAttributeTypesInit.PLANET_NAME));
            for (int j = 0; j < p.getTraits().size(); j++) {
                StringUtils.printlnUtf8((i == starchart.getAllPlanetData().values().size() - 1 ? " " : StringUtils.TREE_V) + " "
                        + (j == p.getTraits().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
                        + p.getTraits().get(j).toString());
            }
        }
    }

}
