package com.machina.datagen.common.recipe;

import java.util.function.Consumer;
import java.util.function.Function;

import com.machina.planet.OreType;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TagInit;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

public class RecipesProvider extends RecipeProvider {

	public RecipesProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> f) {
		add9x9AndBack(f, ItemInit.LOW_GRADE_STEEL_NUGGET.get(), ItemInit.LOW_GRADE_STEEL_INGOT.get());
		add9x9AndBack(f, ItemInit.LOW_GRADE_STEEL_INGOT.get(), BlockInit.LOW_GRADE_STEEL_BLOCK.get());
		add9x9AndBack(f, ItemInit.ALUMINUM_NUGGET.get(), ItemInit.ALUMINUM_INGOT.get());
		add9x9AndBack(f, ItemInit.ALUMINUM_INGOT.get(), BlockInit.ALUMINUM_BLOCK.get());
		add9x9AndBack(f, ItemInit.COPPER_NUGGET.get(), ItemInit.COPPER_INGOT.get());
		add9x9AndBack(f, ItemInit.COPPER_INGOT.get(), BlockInit.COPPER_BLOCK.get());
		add9x9AndBack(f, ItemInit.TRANSISTOR.get(), ItemInit.LOGIC_UNIT.get());
		add9x9AndBack(f, ItemInit.LOGIC_UNIT.get(), ItemInit.PROCESSOR_CORE.get());

		addSmelting(f, BlockInit.ALUMINUM_ORE.get(), ItemInit.ALUMINUM_INGOT.get(), 1f, 200);
		addSmelting(f, BlockInit.COPPER_ORE.get(), ItemInit.COPPER_INGOT.get(), 1f, 200);
		addSmelting(f, ItemInit.RAW_PIG_IRON.get(), ItemInit.LOW_GRADE_STEEL_INGOT.get(), 1f, 200);
		addSmelting(f, ItemInit.RAW_SILICON_BLEND.get(), ItemInit.SILICON.get(), 1f, 200);
		addSmelting(f, ItemInit.SILICON_BOLUS.get(), ItemInit.HIGH_PURITY_SILICON.get(), 1f, 200);

		addBlasting(f, BlockInit.ALUMINUM_ORE.get(), ItemInit.ALUMINUM_INGOT.get(), 1f, 100);
		addBlasting(f, BlockInit.COPPER_ORE.get(), ItemInit.COPPER_INGOT.get(), 1f, 100);
		addBlasting(f, ItemInit.RAW_PIG_IRON.get(), ItemInit.LOW_GRADE_STEEL_INGOT.get(), 1f, 100);
		addBlasting(f, ItemInit.RAW_SILICON_BLEND.get(), ItemInit.SILICON.get(), 1f, 100);

		// Ores
		for (OreType type : OreType.values()) {
			ITag.INamedTag<Item> in = TagInit.Items.ORE_TAGS.get(type);
			IItemProvider out = ItemInit.ore(type).get();
			addSmelting(f, in, out, 1f, 200);
			addBlasting(f, in, out, 1f, 100);
			add9x9AndBack(f, out, BlockInit.ORE_BLOCKS.get(type).get());
		}

		addSmithing(f, Items.STICK, Items.IRON_INGOT, ItemInit.REINFORCED_STICK.get());

		addShapeless(f, ItemInit.RAW_PIG_IRON.get(), 1, Items.IRON_INGOT, Items.IRON_INGOT, Items.COAL);
		addShapeless(f, ItemInit.TRANSISTOR.get(), 1, ItemInit.HIGH_PURITY_SILICON.get(), Items.REDSTONE_TORCH);

		addShaped(f, ItemInit.RAW_SILICON_BLEND.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('a', Items.QUARTZ)
					.define('b', Blocks.GRAVEL)
					.pattern("ab")
					.pattern("ba");
			//@formatter:on
		});

		addShaped(f, BlockInit.SILICA_SAND.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('a', ItemInit.SILICON.get())
					.define('b', Blocks.SAND)
					.pattern("ab")
					.pattern("ba");
			//@formatter:on
		});

		addShaped(f, ItemInit.PROCESSOR.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('a', Items.REDSTONE)
					.define('b', ItemInit.PROCESSOR_CORE.get())
					.define('c', ItemInit.LDPE.get())
					.pattern("aba")
					.pattern("bcb")
					.pattern("aba");
			//@formatter:on
		});

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

	public void addSmelting(Consumer<IFinishedRecipe> f, ITag.INamedTag<Item> in, IItemProvider out, float exp,
			int time) {
		String i = in.getName().getPath();
		MachinaCookingRecipeBuilder.smelting(Ingredient.of(in), out, exp, time).unlockedBy("has_" + i, has(in)).save(f,
				"smelting_" + out.asItem().getRegistryName().getPath() + "_from_" + i);
	}

	public void addBlasting(Consumer<IFinishedRecipe> f, ITag.INamedTag<Item> in, IItemProvider out, float exp,
			int time) {
		String i = in.getName().getPath();
		MachinaCookingRecipeBuilder.blasting(Ingredient.of(in), out, exp, time).unlockedBy("has_" + i, has(in)).save(f,
				"blasting_" + out.asItem().getRegistryName().getPath() + "_from_" + i);
	}
}