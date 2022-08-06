package com.machina.datagen.common.recipe;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;
import com.machina.util.server.TagHelper;

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

		addShaped(f, BlockInit.SHIP_CONSOLE.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('t', ItemInit.TRANSISTOR.get())
					.define('g', Items.GLOWSTONE_DUST)
					.define('c', BlockInit.STEEL_CHASSIS.get())
					.define('s', TagHelper.getForgeItemTag("ingots/steel"))
					.define('b', TagHelper.getForgeItemTag("storage_blocks/steel"))
					.define('p', ItemInit.PROCESSOR.get())
					.pattern("gpg")
					.pattern("tct")
					.pattern("sbs");
			//@formatter:on
		});

		addShaped(f, BlockInit.COMPONENT_ANALYZER.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('t', ItemInit.TRANSISTOR.get())
					.define('c', BlockInit.IRON_CHASSIS.get())
					.define('s', TagHelper.getForgeItemTag("ingots/aluminum"))
					.define('b', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
					.define('r', Items.REDSTONE)
					.define('p', ItemInit.PROCESSOR.get())
					.pattern("rpr")
					.pattern("tct")
					.pattern("sbs");
			//@formatter:on
		});

		addShaped(f, BlockInit.IRON_CHASSIS.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('r', ItemInit.REINFORCED_STICK.get())
					.define('s', Blocks.SCAFFOLDING)
					.define('g', Blocks.GLASS_PANE)
					.define('b', ItemInit.SILICON.get())
					.pattern("bgb")
					.pattern("rsr")
					.pattern("bgb");
			//@formatter:off
		});
		
		addShaped(f, BlockInit.STEEL_CHASSIS.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('c', BlockInit.IRON_CHASSIS.get())
					.define('s', TagHelper.getForgeItemTag("ingots/steel"))
					.pattern(" s ")
					.pattern("scs")
					.pattern(" s ");
			//@formatter:on
		});

		addShaped(f, ItemInit.SCANNER.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('c', ItemInit.PROCESSOR.get())
					.define('t', ItemInit.TRANSISTOR.get())
					.define('r', Items.REDSTONE)
					.define('s', TagHelper.getForgeItemTag("ingots/steel"))
					.define('g', Items.GLOWSTONE_DUST)
					.pattern("tgt")
					.pattern("rcr")
					.pattern(" s ");
			//@formatter:on
		});

		addShaped(f, BlockInit.CABLE.get(), 6, builder -> {
			//@formatter:off
			return builder
					.define('a', ItemInit.ALUMINUM_INGOT.get())
					.define('s', TagHelper.getForgeItemTag("ingots/steel"))
					.pattern("asa");
			//@formatter:on
		});

		addShaped(f, ItemInit.COPPER_COIL.get(), 1, builder -> {
			//@formatter:off
			return builder
					.define('i', ItemInit.COPPER_INGOT.get())
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
		Item i = a.key.values().stream().collect(Collectors.toList()).get(0).getItems()[0].getItem();
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