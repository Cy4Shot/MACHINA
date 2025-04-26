package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;

import com.machina.Machina;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipe.MachinaRecipeSerializer;
import com.machina.api.recipe.MachinaRecipe.RecipeFactory;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.api.recipe.MachinaRecipeType;
import com.machina.api.util.MachinaRL;
import com.machina.block.entity.machine.ComposterVatBlockEntity;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.compat.jei.JeiRecipeRegistrar;
import com.machina.recipe.ComposterVatRecipe;
import com.machina.recipe.CompressorRecipe;
import com.machina.recipe.GrinderRecipe;
import com.machina.recipe.MelterRecipe;
import com.machina.recipe.ReactionChamberRecipe;
import com.machina.recipe.maps.ComposterVatRecipeMaps;
import com.machina.recipe.maps.CompressorRecipeMaps;
import com.machina.recipe.maps.GrinderRecipeMaps;
import com.machina.recipe.maps.MelterRecipeMaps;
import com.machina.recipe.maps.ReactionChamberRecipeMaps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
			.create(ForgeRegistries.RECIPE_TYPES, Machina.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, Machina.MOD_ID);
	public static final List<MachinaRecipeMaps<?>> MAPS = new ArrayList<>();
	public static final List<RecipeRegistryObject<?>> RECIPES = new ArrayList<>();

	//@formatter:off
	public static final RecipeRegistryObject<GrinderBlockEntity> GRINDER =
			register("grinder", BlockInit.GRINDER, GrinderRecipe::new, GrinderRecipeMaps.INSTANCE, 0, 160);
	public static final RecipeRegistryObject<CompressorBlockEntity> COMPRESSOR =
			register("compressor", BlockInit.COMPRESSOR, CompressorRecipe::new, CompressorRecipeMaps.INSTANCE, 16, 160);
	public static final RecipeRegistryObject<MelterBlockEntity> MELTER =
			register("melter", BlockInit.MELTER, MelterRecipe::new, MelterRecipeMaps.INSTANCE, 32, 160);
	public static final RecipeRegistryObject<ReactionChamberBlockEntity> REACTION_CHAMBER =
			register("reaction_chamber", BlockInit.REACTION_CHAMBER, ReactionChamberRecipe::new, ReactionChamberRecipeMaps.INSTANCE, 48, 160);
	public static final RecipeRegistryObject<ComposterVatBlockEntity> COMPOSTER_VAT =
			register("composter_vat", BlockInit.COMPOSTER_VAT, ComposterVatRecipe::new, ComposterVatRecipeMaps.INSTANCE, 64, 160);
	//@formatter:on

	public static class RecipeRegistryObject<C extends Container> {

		private final ResourceLocation id;
		private final RegistryObject<MachinaRecipeType<C>> type;
		private final RecipeFactory<MachinaRecipe<C>> factory;
		private final RegistryObject<MachinaRecipeSerializer<C>> serializer;
		private final MachinaRecipeMaps<C> mapInstance;
		private final RegistryObject<? extends Block> block;
		private final JeiRecipeRegistrar<C> jei;

		public RecipeRegistryObject(ResourceLocation id, RegistryObject<MachinaRecipeType<C>> type,
				RecipeFactory<MachinaRecipe<C>> factory, RegistryObject<MachinaRecipeSerializer<C>> serializer,
				MachinaRecipeMaps<C> mapInstance, RegistryObject<? extends Block> block, int x, int y) {
			this.id = id;
			this.type = type;
			this.factory = factory;
			this.serializer = serializer;
			this.mapInstance = mapInstance;
			this.block = block;
			this.jei = new JeiRecipeRegistrar<C>(this, block, x, y);
		}

		public ResourceLocation id() {
			return id;
		}

		public RegistryObject<MachinaRecipeType<C>> type() {
			return type;
		}

		public RecipeFactory<MachinaRecipe<C>> factory() {
			return factory;
		}

		public RegistryObject<MachinaRecipeSerializer<C>> serializer() {
			return serializer;
		}

		public MachinaRecipeMaps<C> maps() {
			return mapInstance;
		}

		public RegistryObject<? extends Block> block() {
			return block;
		}

		public JeiRecipeRegistrar<C> jei() {
			return jei;
		}

		public String getTranslationKey() {
			return id.getNamespace() + ".recipe." + id.getPath();
		}
	}

	private static <C extends Container> RecipeRegistryObject<C> register(String name,
			RegistryObject<? extends Block> block, RecipeFactory<MachinaRecipe<C>> factory,
			MachinaRecipeMaps<C> mapInstance, int x, int y) {
		ResourceLocation id = new MachinaRL(name);
		RegistryObject<MachinaRecipeType<C>> type = RECIPE_TYPES.register(name,
				() -> new MachinaRecipeType<>(id, mapInstance.getFlags()));
		RegistryObject<MachinaRecipeSerializer<C>> serializer = RECIPE_SERIALIZERS.register(name,
				() -> new MachinaRecipeSerializer<>(type, factory));
		RecipeRegistryObject<C> obj = new RecipeRegistryObject<>(id, type, factory, serializer, mapInstance, block, x,
				y);

		MAPS.add(mapInstance);
		RECIPES.add(obj);
		return obj;
	}
}
