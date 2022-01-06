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

package com.machina.api.world.data;

import java.util.Random;
import java.util.stream.Collectors;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.registry.MachinaRegistries;
import com.machina.api.starchart.PlanetAttributeList;
import com.machina.api.planet.attribute.PlanetAttributeType;
import com.machina.api.starchart.PlanetTraitList;
import com.machina.api.planet.trait.pool.PlanetTraitPoolManager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class PlanetData implements INBTSerializable<CompoundNBT> {

    private final PlanetTraitList traits = new PlanetTraitList();
    private final PlanetAttributeList attributes = new PlanetAttributeList();

    public static PlanetData fromNBT(CompoundNBT nbt) {
        PlanetData pd = new PlanetData();
        pd.deserializeNBT(nbt);
        return pd;
    }

    public static PlanetData fromRand(Random rand) {
        PlanetData pd = new PlanetData();
        pd.generate(rand);
        return pd;
    }

    public void generate(Random rand) {
        // Attributes
        MachinaRegistries.PLANET_ATTRIBUTE_TYPES.forEach(type -> {
            attributes.set(type, type.generator.apply(rand)); // Kinda Hacky .. dunno
        });

        // Traits
        traits.clear();
        PlanetTraitPoolManager.INSTANCE.forEach((location, pool) -> traits.addAll(
                pool.roll(rand).stream().map(MachinaRegistries.PLANET_TRAITS::getValue).collect(Collectors.toList())));
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.put("traits", traits.serializeNBT());
        nbt.put("attributes", attributes.serializeNBT());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        deserialize(nbt, this);
    }

    public static PlanetData deserialize(CompoundNBT nbt) {
        PlanetData data = new PlanetData();
        deserialize(nbt, data);
        return data;
    }

    private static PlanetData deserialize(CompoundNBT nbt, PlanetData data) {
        data.traits.deserializeNBT(nbt.getCompound("traits"));
        data.attributes.deserializeNBT(nbt.getCompound("attributes"));
        return data;
    }

    public PlanetTraitList getTraits() {
        return traits;
    }

    public void addTrait(PlanetTrait trait) {
        traits.addTrait(trait);
    }

    public void removeTrait(PlanetTrait trait) {
        traits.removeTrait(trait);
    }

    public PlanetAttributeList getAttributes() {
        return attributes;
    }

    public <T> T getAttribute(PlanetAttributeType<T> type) {
        return attributes.getAttributeForType(type).get().getValue();
    }

    public <T> String getAttributeFormatted(PlanetAttributeType<T> type) {
        return String.valueOf(attributes.getAttributeForType(type).get().getValue()) + type.getMeasureUnit();
    }

    /**
     * Cy4, learn java :kekw:.. using this when computingIfAbsent will result in this
     * field being put into the map, which means that any modifications in that map will affect this field
     */
    @Deprecated
    public static PlanetData NONE = new PlanetData();
}
