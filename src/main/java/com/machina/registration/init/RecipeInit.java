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
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.compat.jei.JeiRecipeRegistrar;
import com.machina.recipe.GrinderRecipe;
import com.machina.recipe.maps.GrinderRecipeMaps;

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
			register("grinder", BlockInit.GRINDER, GrinderRecipe::new, GrinderRecipeMaps.INSTANCE);
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
				MachinaRecipeMaps<C> mapInstance, RegistryObject<? extends Block> block) {
			this.id = id;
			this.type = type;
			this.factory = factory;
			this.serializer = serializer;
			this.mapInstance = mapInstance;
			this.block = block;
			this.jei = new JeiRecipeRegistrar<C>(this, block);
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
			MachinaRecipeMaps<C> mapInstance) {
		ResourceLocation id = new MachinaRL(name);
		RegistryObject<MachinaRecipeType<C>> type = RECIPE_TYPES.register(name,
				() -> new MachinaRecipeType<>(id, mapInstance.getFlags()));
		RegistryObject<MachinaRecipeSerializer<C>> serializer = RECIPE_SERIALIZERS.register(name,
				() -> new MachinaRecipeSerializer<>(type, factory));
		RecipeRegistryObject<C> obj = new RecipeRegistryObject<>(id, type, factory, serializer, mapInstance, block);

		MAPS.add(mapInstance);
		RECIPES.add(obj);
		return obj;
	}
}
