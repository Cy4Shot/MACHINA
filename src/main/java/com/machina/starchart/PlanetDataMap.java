package com.machina.starchart;

import com.machina.api.world.data.PlanetData;
import com.matyrobbrt.lib.nbt.BaseNBTMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public class PlanetDataMap extends BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> {

    public PlanetDataMap() {
        super(rl -> StringNBT.valueOf(rl.toString()), PlanetData::serializeNBT,
                nbt -> new ResourceLocation(nbt.getAsString()), PlanetData::fromNBT);
    }

    @Override
    public PlanetData get(Object key) {
        if (key instanceof  ResourceLocation) {
            ResourceLocation rl = (ResourceLocation) key;
            return computeIfAbsent(rl, k -> new PlanetData());
        }
        return super.get(key);
    }
}
