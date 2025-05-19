package com.machina.api.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machina.api.util.loader.FluidJson;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public abstract class MachinaRecipe<C extends Container> implements Recipe<C> {

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

	@Override
	public @NotNull ResourceLocation getId() {
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
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> x = NonNullList.create();
		inputItems.forEach(i -> x.add(Ingredient.of(i)));
		return x;
	}

	@Override
	public boolean matches(@NotNull C inv, @NotNull Level level) {
		return true;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull C inv, @NotNull RegistryAccess registry) {
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
	public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registry) {
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

	public static class MachinaRecipeSerializer<C extends Container> implements RecipeSerializer<MachinaRecipe<C>> {

		private final Supplier<MachinaRecipeType<C>> type;
		private final RecipeFactory<MachinaRecipe<C>> factory;

		public MachinaRecipeSerializer(Supplier<MachinaRecipeType<C>> type) {
			this.type = type;
			this.factory = new RecipeFactory<MachinaRecipe<C>>() {
				@Override
				public MachinaRecipe<C> apply(ResourceLocation loc, int energy, int time, float pressure,
						float temperature, int periodicConsumption, List<ItemStack> inputItems,
						List<FluidStack> inputFluids, List<ItemStack> outputItems, List<FluidStack> outputFluids) {
					return new MachinaRecipe<C>(loc, energy, time, pressure, temperature, periodicConsumption,
							inputItems, inputFluids, outputItems, outputFluids) {
						@Override
						public @NotNull RecipeType<MachinaRecipe<C>> getType() {
							return type.get();
						}
					};
				}
			};
		}

		private int getFlags() {
			return type.get().getFlags();
		}

		@Override
		public @NotNull MachinaRecipe<C> fromJson(@NotNull ResourceLocation loc, JsonObject obj) {
			int energy = 0;
			int time = 0;
			float pressure = 0;
			float temperature = 0;
			int periodicConsumption = 1;
			ArrayList<ItemStack> inputItems = new ArrayList<>();
			ArrayList<FluidStack> inputFluids = new ArrayList<>();
			ArrayList<ItemStack> outputItems = new ArrayList<>();
			ArrayList<FluidStack> outputFluids = new ArrayList<>();

			if (obj.has("inputItems") && obj.get("inputItems").isJsonArray()) {
				obj.getAsJsonArray("inputItems")
						.forEach(e -> inputItems.add(ShapedRecipe.itemStackFromJson(e.getAsJsonObject())));
			}
			if (obj.has("inputFluids") && obj.get("inputFluids").isJsonArray()) {
				obj.getAsJsonArray("inputFluids").forEach(e -> inputFluids.add(FluidJson.load(e)));
			}
			if (obj.has("outputItems") && obj.get("outputItems").isJsonArray()) {
				obj.getAsJsonArray("outputItems")
						.forEach(e -> outputItems.add(ShapedRecipe.itemStackFromJson(e.getAsJsonObject())));
			}
			if (obj.has("outputFluids") && obj.get("outputFluids").isJsonArray()) {
				obj.getAsJsonArray("outputFluids").forEach(e -> outputFluids.add(FluidJson.load(e)));
			}

			int flags = getFlags();
			if ((flags & HAS_ENERGY) != 0) {
				if (obj.has("energy")) {
					energy = obj.get("energy").getAsInt();
				}
			}
			if ((flags & HAS_TIME) != 0) {
				if (obj.has("time")) {
					time = obj.get("time").getAsInt();
				}
			}
			if ((flags & HAS_PRESSURE) != 0) {
				if (obj.has("pressure")) {
					pressure = obj.get("pressure").getAsFloat();
				}
			}
			if ((flags & HAS_TEMPERATURE) != 0) {
				if (obj.has("temperature")) {
					temperature = obj.get("temperature").getAsFloat();
				}
			}

			if ((flags & HAS_PERIODIC_CONSUMPTION) != 0) {
				if (obj.has("periodicConsumption")) {
					periodicConsumption = obj.get("periodicConsumption").getAsInt();
				}
			}

			return factory.apply(loc, energy, time, pressure, temperature, periodicConsumption, inputItems, inputFluids,
					outputItems, outputFluids);
		}

		public void toJson(JsonObject obj, MachinaRecipe<C> recipe) {
			int flags = getFlags();

			if ((flags & HAS_ENERGY) != 0) {
				obj.addProperty("energy", recipe.getEnergy());
			}

			if ((flags & HAS_TIME) != 0) {
				obj.addProperty("time", recipe.getTime());
			}

			if ((flags & HAS_PRESSURE) != 0) {
				obj.addProperty("pressure", recipe.getPressure());
			}

			if ((flags & HAS_TEMPERATURE) != 0) {
				obj.addProperty("temperature", recipe.getTemperature());
			}

			if ((flags & HAS_PERIODIC_CONSUMPTION) != 0) {
				obj.addProperty("periodicConsumption", recipe.periodicConsumption);
			}

			JsonArray inputItems = new JsonArray();
			recipe.getInputItems().forEach(e -> {
				JsonObject o = new JsonObject();
				o.addProperty("item", BuiltInRegistries.ITEM.getKey(e.getItem()).toString());
				o.addProperty("count", e.getCount());
				inputItems.add(o);
			});
			obj.add("inputItems", inputItems);

			JsonArray inputFluids = new JsonArray();
			recipe.getInputFluids().forEach(e -> inputFluids.add(FluidJson.save(e)));
			obj.add("inputFluids", inputFluids);

			JsonArray outputItems = new JsonArray();
			recipe.getOutputItems().forEach(e -> {
				JsonObject o = new JsonObject();
				o.addProperty("item", BuiltInRegistries.ITEM.getKey(e.getItem()).toString());
				o.addProperty("count", e.getCount());
				outputItems.add(o);
			});
			obj.add("outputItems", outputItems);

			JsonArray outputFluids = new JsonArray();
			recipe.getOutputFluids().forEach(e -> outputFluids.add(FluidJson.save(e)));
			obj.add("outputFluids", outputFluids);
		}

		@Override
		public @Nullable MachinaRecipe<C> fromNetwork(@NotNull ResourceLocation loc, FriendlyByteBuf buf) {
			int energy = 0;
			int time = 0;
			float pressure = 0;
			float temperature = 0;
			int periodicConsumption = 1;

			int num0 = buf.readVarInt();
			ArrayList<ItemStack> inputItems = new ArrayList<>(num0);
			for (int i = 0; i < num0; i++) {
				inputItems.add(buf.readItem());
			}

			int num1 = buf.readVarInt();
			ArrayList<FluidStack> inputFluids = new ArrayList<>(num1);
			for (int i = 0; i < num1; i++) {
				inputFluids.add(FluidStack.readFromPacket(buf));
			}

			int num2 = buf.readVarInt();
			ArrayList<ItemStack> outputItems = new ArrayList<>(num2);
			for (int i = 0; i < num2; i++) {
				outputItems.add(buf.readItem());
			}

			int num3 = buf.readVarInt();
			ArrayList<FluidStack> outputFluids = new ArrayList<>(num3);
			for (int i = 0; i < num3; i++) {
				outputFluids.add(FluidStack.readFromPacket(buf));
			}

			int flags = getFlags();
			if ((flags & HAS_ENERGY) != 0) {
				energy = buf.readVarInt();
			}
			if ((flags & HAS_TIME) != 0) {
				time = buf.readVarInt();
			}
			if ((flags & HAS_PRESSURE) != 0) {
				pressure = buf.readFloat();
			}
			if ((flags & HAS_TEMPERATURE) != 0) {
				temperature = buf.readFloat();
			}

			if ((flags & HAS_PERIODIC_CONSUMPTION) != 0) {
				periodicConsumption = buf.readVarInt();
			}

			return factory.apply(loc, energy, time, pressure, temperature, periodicConsumption, inputItems, inputFluids,
					outputItems, outputFluids);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, MachinaRecipe<C> recipe) {
			int num0 = recipe.inputItems.size();
			buf.writeVarInt(num0);
			for (int i = 0; i < num0; i++) {
				buf.writeItem(recipe.inputItems.get(i));
			}

			int num1 = recipe.inputFluids.size();
			buf.writeVarInt(num1);
			for (int i = 0; i < num1; i++) {
				recipe.inputFluids.get(i).writeToPacket(buf);
			}

			int num2 = recipe.outputItems.size();
			buf.writeVarInt(num2);
			for (int i = 0; i < num2; i++) {
				buf.writeItem(recipe.outputItems.get(i));
			}

			int num3 = recipe.outputFluids.size();
			buf.writeVarInt(num3);
			for (int i = 0; i < num3; i++) {
				recipe.outputFluids.get(i).writeToPacket(buf);
			}

			int flags = getFlags();
			if ((flags & HAS_ENERGY) != 0) {
				buf.writeVarInt(recipe.getEnergy());
			}
			if ((flags & HAS_TIME) != 0) {
				buf.writeVarInt(recipe.getTime());
			}
			if ((flags & HAS_PRESSURE) != 0) {
				buf.writeFloat(recipe.getPressure());
			}
			if ((flags & HAS_TEMPERATURE) != 0) {
				buf.writeFloat(recipe.getTemperature());
			}

			if ((flags & HAS_PERIODIC_CONSUMPTION) != 0) {
				buf.writeVarInt(recipe.periodicConsumption);
			}
		}
	}

	@FunctionalInterface
	public interface RecipeFactory<R extends MachinaRecipe<?>> {
		R apply(ResourceLocation loc, int energy, int time, float pressure, float temperature, int periodicConsumption,
				List<ItemStack> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems,
				List<FluidStack> outputFluids);
	}
}