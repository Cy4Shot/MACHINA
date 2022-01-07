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

package com.machina.api.starchart;

import com.google.common.collect.ImmutableList;
import com.machina.api.api_extension.ApiExtensions;
import com.machina.api.api_extension.ApiExtendable;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IPlanetTraitType;
import com.machina.api.world.data.PlanetData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An interface used for referencing the starchart of a server.
 */
public interface Starchart {

    /**
     * Get the starchart for the server, creating it if it doesn't exist
     * @param server the server to get the starchart for
     * @return the starchart
     */
    static Starchart getStarchartForServer(@Nonnull @WillNotClose MinecraftServer server) {
        return ApiExtensions.withExtension(StarchartGetter.class, getter -> getter.getStarchart(server));
    }

    /**
     * Gets all the traits of a specific {@code TYPE} for a {@code level}.
     * @param level the level to get the traits from
     * @param typeClass the class of the {@code TYPE} to filter
     * @param <TYPE> the type to filter
     * @return a list containing all the traits of the {@code TYPE}.
     * <b>DO NOT MODIFY THIS LIST</b>
     */
    default <TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(World level, Class<TYPE> typeClass) {
        return getTraitsForType(level.dimension().location(), typeClass);
    }

    /**
     * Gets all the traits of a specific {@code TYPE} for a dimension with specified
     * {@code dimensionId}.
     * @param dimensionId the dimension to retrieve the traits for
     * @param typeClass the class of the {@code TYPE} to filter
     * @param <TYPE> the type to filter
     * @return a list containing all the traits of the {@code TYPE}.
     * <b>DO NOT MODIFY THIS LIST</b>
     */
    default <TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(ResourceLocation dimensionId, Class<TYPE> typeClass) {
        return ImmutableList.copyOf(getAllPlanetData().computeIfAbsent(dimensionId, k -> new PlanetData()).getTraits().stream()
                .filter(typeClass::isInstance).map(typeClass::cast).collect(Collectors.toList()));
    }

    ;

    /**
     * Gets the {@link PlanetData} for the specified level. <br>
     * This cannot be null! It will be computed if absent
     * @param level the level to retrieve the data for
     * @return the {@link PlanetData} for the level
     */
    @Nonnull
    default PlanetData getDataForLevel(World level) {
        return getDataForLevel(level.dimension().location());
    }

    /**
     * Gets the {@link PlanetData} for the specified dimension ID. <br>
     * This cannot be null! It will be computed if absent
     * @param dimensionId the dimension to retrieve the data for
     * @return the {@link PlanetData} for the dimension
     */
    @Nonnull
    PlanetData getDataForLevel(ResourceLocation dimensionId);

    /**
     * Getter for all the {@link PlanetData} that this starchart contains. <br>
     * <b>DO NOT</b> modify the map returned by this!
     * @return
     */
    Map<ResourceLocation, PlanetData> getAllPlanetData();

    /**
     * Adds a trait to the specified {@code dimensionId}, calling {@link #setChanged()}
     * as well
     * @param dimensionId the dimension to add the trait to
     * @param trait the trait to add
     */
    default void addTrait(ResourceLocation dimensionId, PlanetTrait trait) {
        getDataForLevel(dimensionId).addTrait(trait);
        setChanged();
    }

    /**
     * Adds a trait to the specified {@code level}, calling {@link #setChanged()}
     * as well
     * @param level the level to add the trait to
     * @param trait the trait to add
     */
    default void addTrait(World level, PlanetTrait trait) {
        addTrait(level.dimension().location(), trait);
    }

    /**
     * Removes a trait from the specified {@code dimensionId}, calling {@link #setChanged()}
     * as well
     * @param dimensionId the dimension to remove the trait from
     * @param trait the trait to remove
     */
    default void removeTrait(ResourceLocation dimensionId, PlanetTrait trait) {
        getDataForLevel(dimensionId).removeTrait(trait);
        setChanged();
    }

    /**
     * Removes a trait from the specified {@code level}, calling {@link #setChanged()}
     * as well
     * @param level the level to remove the trait from
     * @param trait the trait to remove
     */
    default void removeTrait(World level, PlanetTrait trait) {
        removeTrait(level.dimension().location(), trait);
    }

    /**
     * Called in order to save the starchart when it is changed.
     */
    void setChanged();

    /**
     * Generates the starchart, if it does not exist.
     * @param seed the seed on which the generation will be based
     */
    void generateIfNotExists(final long seed);

    /**
     * Syncs the starchart with all the players in the server
     */
    default void syncWithClients() {
        ApiExtensions.useExtension(StarchartSyncer.class, syncer -> syncer.syncWithClients(this));
    }

    /**
     * Syncs the starchart to a specific {@code player}
     * @param player the player to sync the data with
     */
    default void syncWithPlayer(ServerPlayerEntity player) {
        ApiExtensions.useExtension(StarchartSyncer.class, syncer -> syncer.syncWithPlayer(this, player));
    }

    /**
     * @return is the starchart was generated
     */
    boolean isGenerated();

    /**
     * Sets the starchart's generated property
     * @param generated the starchart's new generated property
     */
    void setGenerated(boolean generated);

    /**
     * An {@link ApiExtendable} interface which will be used
     * in order to retrieve the starchart for a server
     */
    @FunctionalInterface
    interface StarchartGetter extends ApiExtendable {

        Starchart getStarchart(MinecraftServer server);
    }

    /**
     * An {@link ApiExtendable} interface which will be used
     * in order to sync the starchart to clients
     */
    interface StarchartSyncer extends ApiExtendable {

        void syncWithClients(Starchart starchart);

        void syncWithPlayer(Starchart starchart, ServerPlayerEntity player);

    }

    /**
     * An {@link ApiExtendable} interface which will be used
     * in order to serialize and deserialize starcharts.
     * <b>This interface will have an extension hardcoded in order to only
     * work with the official starchart implementation.</b>
     */
    interface StarchartSerializer extends ApiExtendable {

        CompoundNBT serializeNBT(Starchart starchart);

        Starchart deserializeNBT(CompoundNBT nbt);

    }

}
