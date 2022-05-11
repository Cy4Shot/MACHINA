package com.machina.datagen.common;

import java.util.function.Consumer;

import com.machina.datagen.builder.CustomShapedRecipeBuilder;
import com.machina.datagen.builder.CustomShapelessRecipeBuilder;
import com.machina.datagen.builder.CustomSmithingRecipeBuilder;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

public class RecipesProvider extends RecipeProvider {

	public RecipesProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> f) {
		add9x9AndBack(f, ItemInit.STEEL_NUGGET.get(), ItemInit.STEEL_INGOT.get());
		add9x9AndBack(f, ItemInit.STEEL_INGOT.get(), BlockInit.STEEL_BLOCK.get());
		addSmithing(f, Items.STICK, Items.IRON_BLOCK, ItemInit.REINFORCED_STICK.get());
		addShapeless(f, ItemInit.STEEL_INGOT.get(), 1, Items.IRON_INGOT, Items.COAL, Items.COAL);
	}

	public void addSmithing(Consumer<IFinishedRecipe> f, IItemProvider base, IItemProvider add, IItemProvider out) {
		CustomSmithingRecipeBuilder.smithing(Ingredient.of(base), Ingredient.of(add), out.asItem())
				.unlocks("has_" + base.asItem().getRegistryName().getPath(), has(base))
				.save(f, out.asItem().getRegistryName().getPath() + "_smithing");
	}

	public void add9x9AndBack(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out) {
		add9x9(f, in, out);
		addShapelessDecompose(f, out, in, 9);
	}

	public void add9x9(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out) {
		String path = out.asItem().getRegistryName().getPath();
		CustomShapedRecipeBuilder.shaped(out).define('#', in).pattern("###").pattern("###").pattern("###").group(path)
				.unlockedBy("has_" + in.asItem().getRegistryName().getPath(), has(in))
				.save(f, path + "_from_" + in.asItem().getRegistryName().getPath());
	}

	public void addShapelessDecompose(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out, int count) {
		CustomShapelessRecipeBuilder.shapeless(out, count).requires(in)
				.unlockedBy("has_" + in.asItem().getRegistryName().getPath(), has(in))
				.save(f, out.asItem().getRegistryName().getPath() + "_from_" + in.asItem().getRegistryName().getPath());
	}

	public void addShapeless(Consumer<IFinishedRecipe> f, IItemProvider out, int count, IItemProvider... in) {
		CustomShapelessRecipeBuilder.shapeless(out, count).requires(in)
				.unlockedBy("has_" + in[0].asItem().getRegistryName().getPath(), has(in[0]))
				.save(f, out.asItem().getRegistryName().getPath());
	}

}
