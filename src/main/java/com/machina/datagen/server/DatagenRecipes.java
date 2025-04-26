package com.machina.datagen.server;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.datagen.server.base.DatagenRecipeProvider;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FamiliesInit.StoneFamily;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.ItemInit;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class DatagenRecipes extends DatagenRecipeProvider implements IConditionBuilder {

	public DatagenRecipes(PackOutput po) {
		super(po);
	}

	@Override
	protected void buildRecipes(@NotNull Consumer<FinishedRecipe> gen) {
		ore(gen, List.of(BlockInit.ANTHRACITE.get()), ItemInit.COAL_CHUNK.get(), 0.05f, 40, "anthracite");

		//@formatter:off
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockInit.ANTHRACITE.get())
			.requires(Blocks.STONE)
			.requires(ItemInit.COAL_CHUNK.get())
			.unlockedBy(getHasName(BlockInit.ANTHRACITE.get()), has(BlockInit.ANTHRACITE.get()))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ANTHRACITE.get()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockInit.MIGMATITE.get())
			.pattern("SG")
			.pattern("GS")
			.define('S', Blocks.STONE)
			.define('G', Blocks.GRANITE)
			.unlockedBy(getHasName(BlockInit.MIGMATITE.get()), has(BlockInit.MIGMATITE.get()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.MIGMATITE.get()));
		
		reactff_f(gen, FluidInit.MOLTEN_LEAD, 1, FluidInit.MOLTEN_BISMUTH, 1, FluidInit.LEAD_BISMUTH_EUTECTIC, 2, 10);
		reactff_f(gen, FluidInit.SULPHUR_DIOXIDE, 2, FluidInit.OXYGEN, 1, FluidInit.SULPHUR_TRIOXIDE, 2, 10);
		reactff_f(gen, FluidInit.SULPHUR_TRIOXIDE, 1, FluidObject.WATER, 1, FluidInit.SULPHURIC_ACID, 1, 7);
		
		reactfi_fi(gen, FluidInit.SULPHURIC_ACID, 1, ItemInit.NITER, 1, FluidInit.NITRIC_ACID, 1, ItemInit.POTASSIUM_BISULFATE, 1, 10, 144);
		
		reactff_i(gen, FluidInit.AMMONIA, 1, FluidInit.NITRIC_ACID, 1, ItemInit.AMMONIUM_NITRATE, 1, 17, 144);
		
		//@formatter:on

		FamiliesInit.ORES.forEach(x -> oreFamily(gen, x));
		FamiliesInit.STONES.forEach(x -> stoneFamily(gen, x));
		FamiliesInit.WOODS.forEach(x -> woodFamily(gen, x));
	}

	protected static void oreFamily(Consumer<FinishedRecipe> gen, OreFamily family) {
		// Crafting ingot
		family.getIngot().ifPresent(ingot -> {
			family.ore().ifPresent(ore -> ore(gen, List.of(ore), ingot, 0.7f, 200, family.name()));
			family.raw().ifPresent(raw -> {
				ore(gen, List.of(raw), ingot, 0.7f, 200, family.name());
			});
		});

		// Crafting block
		family.getBlock().ifPresent(block -> {
			family.ingot().ifPresent(ingot -> compact(gen, block, ingot));
			family.rawBlock().ifPresent(raw -> ore(gen, List.of(raw), block, 2.7f, 200, family.name()));
		});

		// Crafting nugget
		family.getNugget().ifPresent(nugget -> family.ingot().ifPresent(ingot -> compact(gen, ingot, nugget)));
	}

	protected static void woodFamily(Consumer<FinishedRecipe> gen, WoodFamily family) {
		//@formatter:off
		log_and_plank(gen, family.log(), family.wood(), family.planks());
		log_and_plank(gen, family.stripped_log(), family.stripped_wood(), family.planks());
		slab(gen, family.planks(), family.slab());
		stair(gen, family.planks(), family.stairs());
		door(gen, family.planks(), family.door());
		trapdoor(gen, family.planks(), family.trapdoor());
		pressure_plate(gen, family.planks(), family.pressure_plate());
		button(gen, family.planks(), family.button());
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.fence(), 3)
			.pattern("BSB")
			.pattern("BSB")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.fence()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, family.fencegate())
			.pattern("SBS")
			.pattern("SBS")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.fencegate()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.sign(), 3)
			.pattern("BBB")
			.pattern("BBB")
			.pattern(" S ")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.sign()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.hangingsign(), 6)
			.pattern("C C")
			.pattern("BBB")
			.pattern("BBB")
			.define('B', family.stripped_log())
			.define('C', Items.CHAIN)
			.unlockedBy(getHasName(family.stripped_log()), has(family.stripped_log()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.hangingsign()));
		//@formatter:on
	}

	protected static void log_and_plank(Consumer<FinishedRecipe> gen, ItemLike log, ItemLike wood, ItemLike planks) {
		//@formatter:off
		// LOG -> WOOD
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
			.pattern("BB")
			.pattern("BB")
			.define('B', log)
			.unlockedBy(getHasName(log), has(log))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(wood));
		
		// LOG -> PLANKS
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
			.requires(log)
			.unlockedBy(getHasName(log), has(log))
			.save(gen, Machina.MOD_ID + ":" + getItemName(planks) + "_from_" + getItemName(log));
		
		// WOOD -> PLANKS
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
			.requires(wood)
			.unlockedBy(getHasName(wood), has(wood))
			.save(gen, Machina.MOD_ID + ":" + getItemName(planks) + "_from_" + getItemName(wood));
		//@formatter:on
	}

	protected static void stoneFamily(Consumer<FinishedRecipe> gen, StoneFamily family) {
		//@formatter:off
		slab(gen, family.base(), family.slab());
		stair(gen, family.base(), family.stairs());
		pressure_plate(gen, family.base(), family.pressure_plate());
		button(gen, family.base(), family.button());
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, family.wall(), 6)
			.pattern("BBB")
			.pattern("BBB")
			.define('B', family.base())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.wall()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, family.base(), 1)
			.pattern("PP")
			.pattern("PP")
			.define('P', family.pebbles())
			.unlockedBy(getHasName(family.pebbles()), has(family.pebbles()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_base_from_" + getItemName(family.pebbles()));
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.slab(), 2)
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.slab()));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.stairs())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.stairs()));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.wall())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.wall()));
		//@formatter:on
	}
}
