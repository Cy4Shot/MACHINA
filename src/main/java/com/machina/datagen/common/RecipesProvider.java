package com.machina.datagen.common;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.machina.datagen.builder.CustomCookingRecipeBuilder;
import com.machina.datagen.builder.CustomShapedRecipeBuilder;
import com.machina.datagen.builder.CustomShapelessRecipeBuilder;
import com.machina.datagen.builder.CustomSmithingRecipeBuilder;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
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
		addSmithing(f, Items.IRON_INGOT, ItemInit.SILICON.get(), ItemInit.TRANSISTOR.get());
		
		addShapeless(f, ItemInit.STEEL_INGOT.get(), 1, Items.IRON_INGOT, Items.COAL, Items.COAL);
		
		addBlasting(f, Blocks.GRAVEL, ItemInit.SILICON.get(), 0.5f, 100);
		
		addShaped(f, ItemInit.PROCESSOR.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('t', ItemInit.TRANSISTOR.get())
					.define('d', Items.DIAMOND)
					.define('r', Items.REDSTONE_TORCH)
					.pattern("trt")
					.pattern("rdr")
					.pattern("trt");
			//@formatter:on
		});

		addShaped(f, BlockInit.IRON_CHASSIS.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('r', ItemInit.REINFORCED_STICK.get())
					.define('s', Blocks.SCAFFOLDING)
					.pattern("rrr")
					.pattern("rsr")
					.pattern("rrr");
			//@formatter:off
		});
		
		addShaped(f, BlockInit.STEEL_CHASSIS.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('c', BlockInit.IRON_CHASSIS.get())
					.define('s', ItemInit.STEEL_INGOT.get())
					.pattern("sss")
					.pattern("scs")
					.pattern("sss");
			//@formatter:on
		});
	}

	public void addShaped(Consumer<IFinishedRecipe> f, IItemProvider out, int count,
			Function<CustomShapedRecipeBuilder, CustomShapedRecipeBuilder> apply) {
		CustomShapedRecipeBuilder b = CustomShapedRecipeBuilder.shaped(out, count);
		CustomShapedRecipeBuilder a = apply.apply(b);
		Item i = a.key.values().stream().collect(Collectors.toList()).get(0).getItems()[0].getItem();
		String p = out.asItem().getRegistryName().getPath();
		a.group(p).unlockedBy("has_" + i, has(i)).save(f, p);
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

	public void addSmelting(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out, float exp, int time) {
		String i = in.asItem().getRegistryName().getPath();
		CustomCookingRecipeBuilder.smelting(Ingredient.of(in), out, exp, time).unlockedBy("has_" + i, has(in)).save(f,
				out.asItem().getRegistryName().getPath() + "_from_" + i);
	}

	public void addBlasting(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out, float exp, int time) {
		String i = in.asItem().getRegistryName().getPath();
		CustomCookingRecipeBuilder.blasting(Ingredient.of(in), out, exp, time).unlockedBy("has_" + i, has(in)).save(f,
				out.asItem().getRegistryName().getPath() + "_from_" + i);
	}
}