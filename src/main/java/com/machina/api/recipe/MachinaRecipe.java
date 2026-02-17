package com.machina.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machina.api.util.loader.FluidJson;
import com.machina.api.util.reflect.MachinaStreamCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class MachinaRecipe<C extends RecipeInput> implements Recipe<C> {

    public static final short HAS_ENERGY = 0x1;
    public static final short HAS_PRESSURE = 0x2;
    public static final short HAS_TEMPERATURE = 0x4;
    public static final short HAS_TIME = 0x8;
    public static final short HAS_PERIODIC_CONSUMPTION = 0x10;

    private final ResourceLocation id;
    protected final List<ItemStack> inputItems = new ArrayList<>();
    protected final List<FluidStack> inputFluids = new ArrayList<>();
    protected final List<ItemStack> outputItems = new ArrayList<>();
    protected final List<FluidStack> outputFluids = new ArrayList<>();
    private final int energy;
    private final int time;
    private final float pressure;
    private final float temperature;
    private final int periodicConsumption;

    public MachinaRecipe(ResourceLocation id, int energy, int time, float pressure, float temperature,
            int periodicConsumption, List<ItemStack> inputItems, List<FluidStack> inputFluids,
            List<ItemStack> outputItems, List<FluidStack> outputFluids) {

        if (inputItems == null || inputFluids == null || outputItems == null || outputFluids == null) {
            throw new IllegalArgumentException("Input and output lists must not be null");
        }

        this.id = id;
        this.time = Math.max(1, time);
        this.energy = Math.max(0, energy);
        this.pressure = pressure;
        this.temperature = temperature;
        this.periodicConsumption = periodicConsumption;
        this.inputItems.addAll(inputItems);
        this.inputFluids.addAll(inputFluids);
        this.outputItems.addAll(outputItems);
        this.outputFluids.addAll(outputFluids);
    }

    private static <C extends RecipeInput> MachinaRecipe<C> createFromCodec(ResourceLocation id, int energy, int time,
            float pressure, float temperature, int periodicConsumption, List<ItemStack> inputItems,
            List<FluidStack> inputFluids, List<ItemStack> outputItems, List<FluidStack> outputFluids) {
        return new MachinaRecipe<C>(id, energy, time, pressure, temperature, periodicConsumption, inputItems,
                inputFluids, outputItems, outputFluids);
    }

    public ResourceLocation getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    public float getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getPeriodicConsumption() {
        return periodicConsumption;
    }

    public int getPowerRate() {
        return energy / time;
    }

    public List<ItemStack> getInputItems() {
        return inputItems;
    }

    public List<FluidStack> getInputFluids() {
        return inputFluids;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }

    public List<FluidStack> getOutputFluids() {
        return outputFluids;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> x = NonNullList.create();
        inputItems.forEach(i -> x.add(Ingredient.of(i)));
        return x;
    }

    @Override
    public boolean matches(@NotNull C inv, @NotNull Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(C input, Provider registries) {
        if (getOutputItems().isEmpty()) {
            return ItemStack.EMPTY;
        }
        return getOutputItems().get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(Provider registries) {
        if (getOutputItems().isEmpty()) {
            return ItemStack.EMPTY;
        }
        return getOutputItems().get(0).copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public MachinaRecipeType<C> getMachinaType() {
        RecipeType<?> type = getType();
        if (type instanceof MachinaRecipeType) {
            return ((MachinaRecipeType<C>) type);
        }
        return null;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return new MachinaRecipeSerializer<>(this::getMachinaType);
    }

    public static class MachinaRecipeSerializer<C extends RecipeInput> implements RecipeSerializer<MachinaRecipe<C>> {

        private final Supplier<MachinaRecipeType<C>> type;
        private final RecipeFactory<MachinaRecipe<C>> factory;

        public MachinaRecipeSerializer(Supplier<MachinaRecipeType<C>> type) {
            this.type = type;
            this.factory = (loc, energy, time, pressure, temperature, periodicConsumption, inputItems, inputFluids,
                    outputItems, outputFluids) -> new MachinaRecipe<>(loc, energy, time, pressure, temperature,
                            periodicConsumption, inputItems, inputFluids, outputItems, outputFluids) {
                        @Override
                        public @NotNull RecipeType<MachinaRecipe<C>> getType() {
                            return type.get();
                        }
                    };
        }

        private int getFlags() {
            return type.get().getFlags();
        }

        @Override
        public MapCodec<MachinaRecipe<C>> codec() {
            return RecordCodecBuilder.mapCodec(inst -> inst
                    .group(ItemStack.CODEC.listOf().fieldOf("input_items").forGetter(r -> r.inputItems),
                            /* if you have a codec for FluidStack */ FluidStack.CODEC.listOf().fieldOf("input_fluids")
                                    .forGetter(r -> r.inputFluids),
                            ItemStack.CODEC.listOf().fieldOf("output_items").forGetter(r -> r.outputItems),
                            FluidStack.CODEC.listOf().fieldOf("output_fluids").forGetter(r -> r.outputFluids),
                            Codec.INT.fieldOf("energy").forGetter(MachinaRecipe::getEnergy),
                            Codec.INT.fieldOf("time").forGetter(MachinaRecipe::getTime),
                            Codec.FLOAT.fieldOf("pressure").forGetter(MachinaRecipe::getPressure),
                            Codec.FLOAT.fieldOf("temperature").forGetter(MachinaRecipe::getTemperature),
                            Codec.INT.fieldOf("periodic_consumption").forGetter(r -> r.periodicConsumption))
                    .apply(inst, (energy, time, pressure, temperature, periodic, inItems, inFluids, outItems, outFluids) ->
                    factory.apply(
                        loc, energy, time, pressure, temperature, periodic,
                        inItems, inFluids, outItems, outFluids
                    )
                ));
        }
        
        Function<RecipeFactory<MachinaRecipe<C>>, StreamCodec<RegistryFriendlyByteBuf, MachinaRecipe<C>>> CODEC = f -> MachinaStreamCodecs.composite(
                ByteBufCodecs.VAR_INT, (MachinaRecipe<C> r) -> r.getEnergy(),
                ByteBufCodecs.VAR_INT, MachinaRecipe::getTime,
                ByteBufCodecs.FLOAT, MachinaRecipe::getPressure,
                ByteBufCodecs.FLOAT, MachinaRecipe::getTemperature,
                ByteBufCodecs.VAR_INT, MachinaRecipe::getPeriodicConsumption,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), MachinaRecipe::getInputItems,
                FluidStack.STREAM_CODEC.apply(ByteBufCodecs.list()), MachinaRecipe::getInputFluids,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), MachinaRecipe::getOutputItems,
                FluidStack.STREAM_CODEC.apply(ByteBufCodecs.list()), MachinaRecipe::getOutputFluids,
                (energy, time, pressure, temperature, periodic, inItems, inFluids, outItems, outFluids) ->
                    f.apply(
                        loc, energy, time, pressure, temperature,
                        periodic, inItems, inFluids, outItems, outFluids
                    )
            );

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MachinaRecipe<C>> streamCodec() {
            return CODEC.apply(factory);
        }
    }

    @FunctionalInterface
    public interface RecipeFactory<R extends MachinaRecipe<?>> {
        R apply(ResourceLocation loc, int energy, int time, float pressure, float temperature, int periodicConsumption,
                List<ItemStack> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems,
                List<FluidStack> outputFluids);
    }
}