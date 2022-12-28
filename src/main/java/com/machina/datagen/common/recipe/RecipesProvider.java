package com.machina.datagen.common.recipe;

import java.util.function.Consumer;
import java.util.function.Function;

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
		add9x9AndBack(f, ItemInit.ALUMINUM_NUGGET.get(), ItemInit.ALUMINUM_INGOT.get());
		add9x9AndBack(f, ItemInit.ALUMINUM_INGOT.get(), BlockInit.ALUMINUM_BLOCK.get());
		add9x9AndBack(f, ItemInit.COPPER_NUGGET.get(), ItemInit.COPPER_INGOT.get());
		add9x9AndBack(f, ItemInit.COPPER_INGOT.get(), BlockInit.COPPER_BLOCK.get());

		addSmelting(f, BlockInit.ALUMINUM_ORE.get(), ItemInit.ALUMINUM_INGOT.get(), 1f, 200);
		addSmelting(f, BlockInit.COPPER_ORE.get(), ItemInit.COPPER_INGOT.get(), 1f, 200);

		addBlasting(f, BlockInit.ALUMINUM_ORE.get(), ItemInit.ALUMINUM_INGOT.get(), 1f, 100);
		addBlasting(f, BlockInit.COPPER_ORE.get(), ItemInit.COPPER_INGOT.get(), 1f, 100);
		addBlasting(f, Blocks.GRAVEL, ItemInit.SILICON.get(), 0.5f, 100);

		addSmithing(f, Items.STICK, Items.IRON_INGOT, ItemInit.REINFORCED_STICK.get());

		addShapeless(f, ItemInit.STEEL_INGOT.get(), 1, Items.IRON_INGOT, Items.IRON_INGOT, Items.COAL);

		addShaped(f, ItemInit.BLUEPRINT.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('d', "forge:dyes/blue")
					.define('p', Items.PAPER)
					.pattern(" d ")
					.pattern("dpd")
					.pattern(" d ");
			//@formatter:on
		});

		addShaped(f, BlockInit.IRON_SCAFFOLDING.get(), 16, builder -> {
			//@formatter:off
			return builder
					.define('i', Items.IRON_INGOT)
					.define('s', Blocks.SCAFFOLDING)
					.pattern(" i ")
					.pattern("isi")
					.pattern(" i ");
			//@formatter:on
		});

		addShaped(f, BlockInit.STEEL_SCAFFOLDING.get(), 16, builder -> {
			//@formatter:off
			return builder
					.define('i', "forge:ingots/steel")
					.define('s', Blocks.SCAFFOLDING)
					.pattern(" i ")
					.pattern("isi")
					.pattern(" i ");
			//@formatter:on
		});

		addShaped(f, BlockInit.ALUMINUM_SCAFFOLDING.get(), 16, builder -> {
			//@formatter:off
			return builder
					.define('i', "forge:ingots/aluminum")
					.define('s', Blocks.SCAFFOLDING)
					.pattern(" i ")
					.pattern("isi")
					.pattern(" i ");
			//@formatter:on
		});

		addShaped(f, BlockInit.COPPER_SCAFFOLDING.get(), 16, builder -> {
			//@formatter:off
			return builder
					.define('i', "forge:ingots/copper")
					.define('s', Blocks.SCAFFOLDING)
					.pattern(" i ")
					.pattern("isi")
					.pattern(" i ");
			//@formatter:on
		});

		addShaped(f, ItemInit.TRANSISTOR.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('n', Items.IRON_NUGGET)
					.define('s', ItemInit.SILICON.get())
					.define('r', Items.REDSTONE)
					.pattern(" s ")
					.pattern("nrn")
					.pattern(" s ");
			//@formatter:on
		});

		addShaped(f, ItemInit.IRON_CATALYST.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('i', Items.IRON_INGOT)
					.define('s', ItemInit.SILICON.get())
					.pattern(" i ")
					.pattern("isi")
					.pattern(" i ");
			//@formatter:on
		});

		addShaped(f, ItemInit.PROCESSOR.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('t', ItemInit.TRANSISTOR.get())
					.define('d', Items.DIAMOND)
					.define('r', Items.REDSTONE_TORCH)
					.define('c', ItemInit.COPPER_COIL.get())
					.pattern("tct")
					.pattern("rdr")
					.pattern("tct");
			//@formatter:on
		});

		addShaped(f, ItemInit.COPPER_COIL.get(), 2, builder -> {
			//@formatter:off
			return builder
					.define('i', "forge:ingots/copper")
					.define('s', Items.STICK)
					.pattern("iii")
					.pattern("isi")
					.pattern("iii");
			//@formatter:on
		});
	}

	public void addShaped(Consumer<IFinishedRecipe> f, IItemProvider out, int count,
			Function<MachinaShapedRecipeBuilder, MachinaShapedRecipeBuilder> apply) {
		MachinaShapedRecipeBuilder b = MachinaShapedRecipeBuilder.shaped(out, count);
		MachinaShapedRecipeBuilder a = apply.apply(b);
		Item i = a.has.getItems()[0].getItem();
		String p = "shaped_" + out.asItem().getRegistryName().getPath();
		a.group(p).unlockedBy("has_" + i, has(i)).save(f, p);
	}

	public void addSmithing(Consumer<IFinishedRecipe> f, IItemProvider base, IItemProvider add, IItemProvider out) {
		MachinaSmithingRecipeBuilder.smithing(Ingredient.of(base), Ingredient.of(add), out.asItem())
				.unlocks("has_" + base.asItem().getRegistryName().getPath(), has(base))
				.save(f, "smithing_" + out.asItem().getRegistryName().getPath() + "_smithing");
	}

	public void add9x9AndBack(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out) {
		add9x9(f, in, out);
		addShapelessDecompose(f, out, in, 9);
	}

	public void add9x9(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out) {
		String path = out.asItem().getRegistryName().getPath();
		MachinaShapedRecipeBuilder.shaped(out).define('#', in).pattern("###").pattern("###").pattern("###").group(path)
				.unlockedBy("has_" + in.asItem().getRegistryName().getPath(), has(in))
				.save(f, "9x9_" + path + "_from_" + in.asItem().getRegistryName().getPath());
	}

	public void addShapelessDecompose(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out, int count) {
		MachinaShapelessRecipeBuilder.shapeless(out, count).requires(in)
				.unlockedBy("has_" + in.asItem().getRegistryName().getPath(), has(in))
				.save(f, "decompose_" + out.asItem().getRegistryName().getPath() + "_from_"
						+ in.asItem().getRegistryName().getPath());
	}

	public void addShapeless(Consumer<IFinishedRecipe> f, IItemProvider out, int count, IItemProvider... in) {
		MachinaShapelessRecipeBuilder.shapeless(out, count).requires(in)
				.unlockedBy("has_" + in[0].asItem().getRegistryName().getPath(), has(in[0]))
				.save(f, "shapeless_" + out.asItem().getRegistryName().getPath());
	}

	public void addSmelting(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out, float exp, int time) {
		String i = in.asItem().getRegistryName().getPath();
		MachinaCookingRecipeBuilder.smelting(Ingredient.of(in), out, exp, time).unlockedBy("has_" + i, has(in)).save(f,
				"smelting_" + out.asItem().getRegistryName().getPath() + "_from_" + i);
	}

	public void addBlasting(Consumer<IFinishedRecipe> f, IItemProvider in, IItemProvider out, float exp, int time) {
		String i = in.asItem().getRegistryName().getPath();
		MachinaCookingRecipeBuilder.blasting(Ingredient.of(in), out, exp, time).unlockedBy("has_" + i, has(in)).save(f,
				"blasting_" + out.asItem().getRegistryName().getPath() + "_from_" + i);
	}
}