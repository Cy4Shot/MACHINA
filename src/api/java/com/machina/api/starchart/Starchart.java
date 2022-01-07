/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.starchart;

import com.machina.api.api_extension.ApiExtensions;
import com.machina.api.api_extension.IApiExtendable;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IPlanetTraitType;
import com.machina.api.world.data.PlanetData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import java.util.List;
import java.util.Map;

public interface Starchart {

    static Starchart getStarchartForServer(@Nonnull @WillNotClose MinecraftServer server) {
        return ApiExtensions.withExtension(StarchartGetter.class, getter -> getter.getStarchart(server));
    }

    default <TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(World level, Class<TYPE> typeClass) {
        return getTraitsForType(level.dimension().location(), typeClass);
    }

    <TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(ResourceLocation dimensionId, Class<TYPE> typeClass);

    @Nullable
    default PlanetData getDataForLevel(World level) {
        return getDataForLevel(level.dimension().location());
    }

    @Nullable
    PlanetData getDataForLevel(ResourceLocation dimensionId);

    /**
     * Getter for all the {@link PlanetData} that this starchart contains. <b>DO NOT</b>
     * modify the map returned by this!
     * @return
     */
    Map<ResourceLocation, PlanetData> getAllPlanetData();

    default void addTrait(ResourceLocation dimensionId, PlanetTrait trait) {
        getDataForLevel(dimensionId).addTrait(trait);
    }

    default void addTrait(World level, PlanetTrait trait) {
        addTrait(level.dimension().location(), trait);
    }

    default void removeTrait(ResourceLocation dimensionId, PlanetTrait trait) {
        getDataForLevel(dimensionId).removeTrait(trait);
    }

    default void removeTrait(World level, PlanetTrait trait) {
        removeTrait(level.dimension().location(), trait);
    }

    void setChanged();

    void generateIfNotExists(final long seed);

    @FunctionalInterface
    interface StarchartGetter extends IApiExtendable {

        Starchart getStarchart(MinecraftServer server);
    }

}