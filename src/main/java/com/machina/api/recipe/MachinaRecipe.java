package com.machina.api.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machina.api.util.loader.FluidJson;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public abstract class MachinaRecipe<C extends Container> implements Recipe<C> {

	public static final short HAS_ENERGY = 0x1;
	public static final short HAS_PRESSURE = 0x2;
	public static final short HAS_TEMPERATURE = 0x4;
	public static final short HAS_TIME = 0x8;

	private ResourceLocation id;
	protected final List<Ingredient> inputItems = new ArrayList<>();
	protected final List<FluidStack> inputFluids = new ArrayList<>();
	protected final List<ItemStack> outputItems = new ArrayList<>();
	protected final List<FluidStack> outputFluids = new ArrayList<>();
	private int energy;
	private int time;
	private float pressure;
	private float temperature;
	private float xp;

	public MachinaRecipe(ResourceLocation id, int energy, int time, float pressure, float temperature, float xp,
			List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems,
			List<FluidStack> outputFluids) {

		if (inputItems == null || inputFluids == null || outputItems == null || outputFluids == null) {
			throw new IllegalArgumentException("Input and output lists must not be null");
		}

		this.id = id;
		this.time = Math.max(1, time);
		this.energy = Math.max(0, energy);
		this.pressure = pressure;
		this.temperature = temperature;
		this.xp = Math.max(0, xp);
		this.inputItems.addAll(inputItems);
		this.inputFluids.addAll(inputFluids);
		this.outputItems.addAll(outputItems);
		this.outputFluids.addAll(outputFluids);
	}

	protected abstract RecipeRegistryObject<C> getRegistryObject();

	@Override
	public RecipeType<MachinaRecipe<C>> getType() {
		return getRegistryObject().type().get();
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
	public ResourceLocation getId() {
		return id;
	}

	public int getFlags() {
		MachinaRecipeType<C> type = getMachinaType();
		return type != null ? type.getFlags() : 0;
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

	public float getXp() {
		return xp;
	}

	public int getPowerRate() {
		return energy / time;
	}

	public List<Ingredient> getInputItems() {
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
	public boolean matches(C inv, Level level) {
		return true;
	}

	@Override
	public ItemStack assemble(C inv, RegistryAccess registry) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registry) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public boolean hasEnergy() {
		return (getFlags() & HAS_ENERGY) != 0;
	}

	public boolean hasPressure() {
		return (getFlags() & HAS_PRESSURE) != 0;
	}

	public boolean hasTemperature() {
		return (getFlags() & HAS_TEMPERATURE) != 0;
	}

	public boolean hasTime() {
		return (getFlags() & HAS_TIME) != 0;
	}

	public abstract RecipeFactory<MachinaRecipe<C>> getFactory();

	@Override
	public RecipeSerializer<?> getSerializer() {
		return new MachinaRecipeSerializer<>(this::getMachinaType, getFactory());
	}

	public static class MachinaRecipeSerializer<C extends Container> implements RecipeSerializer<MachinaRecipe<C>> {

		private final Supplier<MachinaRecipeType<C>> type;
		private final RecipeFactory<MachinaRecipe<C>> factory;

		public MachinaRecipeSerializer(Supplier<MachinaRecipeType<C>> type, RecipeFactory<MachinaRecipe<C>> factory) {
			this.type = type;
			this.factory = factory;
		}

		private int getFlags() {
			return type.get().getFlags();
		}

		@Override
		public MachinaRecipe<C> fromJson(ResourceLocation loc, JsonObject obj) {
			float experience = 0f;
			int energy = 0;
			int time = 0;
			float pressure = 0;
			float temperature = 0;
			ArrayList<Ingredient> inputItems = new ArrayList<>();
			ArrayList<FluidStack> inputFluids = new ArrayList<>();
			ArrayList<ItemStack> outputItems = new ArrayList<>();
			ArrayList<FluidStack> outputFluids = new ArrayList<>();

			if (obj.has("xp")) {
				experience = obj.get("xp").getAsFloat();
			}
			if (obj.has("inputItems") && obj.get("inputItems").isJsonArray()) {
				obj.getAsJsonArray("inputItems").forEach(e -> inputItems.add(Ingredient.fromJson(e)));
			}
			if (obj.has("inputFluids") && obj.get("inputFluids").isJsonArray()) {
				obj.getAsJsonArray("inputFluids").forEach(e -> inputFluids.add(FluidJson.load(e)));
			}
			if (obj.has("outputItems") && obj.get("outputItems").isJsonArray()) {
				obj.getAsJsonArray("outputItems")
						.forEach(e -> outputItems.add(Ingredient.fromJson(e.getAsJsonObject()).getItems()[0]));
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

			return factory.apply(loc, energy, time, pressure, temperature, experience, inputItems, inputFluids, outputItems,
					outputFluids);
		}

		public void toJson(JsonObject obj, MachinaRecipe<C> recipe) {
			int flags = getFlags();

			obj.addProperty("xp", recipe.getXp());

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

			JsonArray inputItems = new JsonArray();
			recipe.getInputItems().forEach(e -> inputItems.add(e.toJson()));
			obj.add("inputItems", inputItems);

			JsonArray inputFluids = new JsonArray();
			recipe.getInputFluids().forEach(e -> inputFluids.add(FluidJson.save(e)));
			obj.add("inputFluids", inputFluids);

			JsonArray outputItems = new JsonArray();
			recipe.getOutputItems().forEach(e -> outputItems.add(Ingredient.of(e).toJson()));
			obj.add("outputItems", outputItems);

			JsonArray outputFluids = new JsonArray();
			recipe.getOutputFluids().forEach(e -> outputFluids.add(FluidJson.save(e)));
			obj.add("outputFluids", outputFluids);
		}

		@Override
		public @Nullable MachinaRecipe<C> fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			float experience = buf.readFloat();
			int energy = 0;
			int time = 0;
			float pressure = 0;
			float temperature = 0;

			int num0 = buf.readVarInt();
			ArrayList<Ingredient> inputItems = new ArrayList<>(num0);
			for (int i = 0; i < num0; i++) {
				inputItems.add(Ingredient.fromNetwork(buf));
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

			return factory.apply(loc, energy, time, pressure, temperature, experience, inputItems, inputFluids, outputItems,
					outputFluids);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, MachinaRecipe<C> recipe) {
			buf.writeFloat(recipe.getXp());

			int num0 = recipe.inputItems.size();
			buf.writeVarInt(num0);
			for (int i = 0; i < num0; i++) {
				recipe.inputItems.get(i).toNetwork(buf);
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
		}
	}

	@FunctionalInterface
	public interface RecipeFactory<R extends MachinaRecipe<?>> {
		R apply(ResourceLocation loc, int energy, int time, float pressure, float temperature, float xp, List<Ingredient> inputItems,
				List<FluidStack> inputFluids, List<ItemStack> outputItems, List<FluidStack> outputFluids);
	}
}