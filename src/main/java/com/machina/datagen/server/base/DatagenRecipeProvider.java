package com.machina.datagen.server.base;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.api.item.RocketPartItem;
import com.machina.api.recipe.MachinaRecipeBuilder;
import com.machina.api.rocket.part.RocketPart;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.RecipeInit;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public abstract class DatagenRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public DatagenRecipeProvider(PackOutput po, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(po, lookupProvider);
	}

	protected static void stair(RecipeOutput gen, ItemLike base, ItemLike stair) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stair, 4)
			.pattern("B  ")
			.pattern("BB ")
			.pattern("BBB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(stair));
		//@formatter:on
	}

	protected static void slab(RecipeOutput gen, ItemLike base, ItemLike slab) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
			.pattern("BBB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(slab));
		//@formatter:on
	}

	protected static void door(RecipeOutput gen, ItemLike base, ItemLike door) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
			.pattern("BB")
			.pattern("BB")
			.pattern("BB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(door));
		//@formatter:on
	}

	protected static void trapdoor(RecipeOutput gen, ItemLike base, ItemLike door) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 2)
			.pattern("BBB")
			.pattern("BBB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(door));
		//@formatter:on
	}

	protected static void pressure_plate(RecipeOutput gen, ItemLike base, ItemLike plate) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, plate)
			.pattern("BB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(plate));
		//@formatter:on
	}

	protected static void button(RecipeOutput gen, ItemLike base, ItemLike button) {
		//@formatter:off
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button, 1)
			.requires(base)
			.unlockedBy(getHasName(base), has(base))
			.save(gen, Machina.MOD_ID + ":" + getItemName(button) + "_from_" + getItemName(base));
		//@formatter:on
	}

	protected static void compact(RecipeOutput gen, ItemLike big, ItemLike small) {
		recompact(gen, big, small);
		decompact(gen, big, small);
	}

	protected static void recompact(RecipeOutput gen, ItemLike big, ItemLike small) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, big)
	        .pattern("SSS")
	        .pattern("SSS")
	        .pattern("SSS")
	        .define('S', small)
	        .unlockedBy(getHasName(small), has(small))
	        .showNotification(false)
	        .save(gen, Machina.MOD_ID + ":" + getItemName(big) + "_from_" + getItemName(small));
		//@formatter:on
	}

	protected static void decompact(RecipeOutput gen, ItemLike big, ItemLike small) {
		//@formatter:off
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, small, 9)
	        .requires(big)
	        .unlockedBy(getHasName(big), has(big))
	        .save(gen, Machina.MOD_ID + ":" + getItemName(small) + "_from_" + getItemName(big));
		//@formatter:on
	}

	protected static void ore(RecipeOutput gen, List<ItemLike> ing, ItemLike res, float exp, int duration,
			String group) {
		oreSmelting(gen, ing, res, exp, duration, group);
		oreBlasting(gen, ing, res, exp, duration / 2, group);
	}

	protected static void oreSmelting(@NotNull RecipeOutput gen, List<ItemLike> ing, @NotNull ItemLike res, float exp,
			int pCookingTIme, @NotNull String group) {
		oreCooking(gen, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ing, res, exp, pCookingTIme, group,
				"_from_smelting");
	}

	protected static void oreBlasting(@NotNull RecipeOutput gen, List<ItemLike> ing, @NotNull ItemLike res, float exp,
			int time, @NotNull String group) {
		oreCooking(gen, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ing, res, exp, time, group,
				"_from_blasting");
	}

	protected static <T extends AbstractCookingRecipe> void oreCooking(@NotNull RecipeOutput gen,
			@NotNull RecipeSerializer<T> ser, AbstractCookingRecipe.Factory<T> factory, List<ItemLike> ing,
			@NotNull ItemLike res, float exp, int time, @NotNull String group, String name) {
		for (ItemLike itemlike : ing) {
			//@formatter:off
			SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), RecipeCategory.MISC, res, exp, time, ser, factory)
					.group(group)
					.unlockedBy(getHasName(itemlike), has(itemlike))
					.save(gen, Machina.MOD_ID + ":" + getItemName(res) + name + "_" + getItemName(itemlike));
			//@formatter:on
		}
	}

	// Machina Recipe Builders
	protected static void reactff_f(@NotNull RecipeOutput gen, FluidObject input1, int amount1, FluidObject input2,
			int amount2, FluidObject output, int amount, int energy) {
		//@formatter:off
		MachinaRecipeBuilder.create(RecipeInit.REACTION_CHAMBER)
			.in(new FluidStack(input1.fluid(), amount1))
			.in(new FluidStack(input2.fluid(), amount2))
			.out(new FluidStack(output.fluid(), amount))
			.energy(energy)
			.save(gen, "reacting_ff_f_" + output.name());
		//@formatter:on
	}

	protected static void reactfi_fi(@NotNull RecipeOutput gen, FluidObject i1, int a1, DeferredItem<? extends Item> i2,
			int a2, FluidObject o1, int b1, DeferredItem<? extends Item> o2, int b2, int energy, int periodicity) {
		//@formatter:off
		MachinaRecipeBuilder.create(RecipeInit.REACTION_CHAMBER)
			.in(new FluidStack(i1.fluid(), a1))
			.in(i2.get(), a2)
			.out(new FluidStack(o1.fluid(), b1))
			.out(o2.get(), b2)
			.energy(energy)
			.period(periodicity)
			.save(gen, "reacting_fi_fi_" + o1.name());
		//@formatter:on
	}

	protected static void reactff_i(@NotNull RecipeOutput gen, FluidObject i1, int a1, FluidObject i2, int a2,
			DeferredItem<? extends Item> o, int b, int energy, int periodicity) {
		//@formatter:off
		MachinaRecipeBuilder.create(RecipeInit.REACTION_CHAMBER)
			.in(new FluidStack(i1.fluid(), a1))
			.in(new FluidStack(i2.fluid(), a2))
			.out(o.get(), b)
			.energy(energy)
			.period(periodicity)
			.save(gen, "reacting_ff_i_" + getItemName(o.get()));
		//@formatter:on
	}

	protected static void electrolysis_f_ff(@NotNull RecipeOutput gen, FluidObject i, int a, FluidObject o1, int b1,
			FluidObject o2, int b2, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i.fluid(), a))
            .out(new FluidStack(o1.fluid(), b1))
            .out(new FluidStack(o2.fluid(), b2))
            .energy(energy)
            .save(gen, "electrolysis_f_ff_" + i.name());
        //@formatter:on
	}

	protected static void electrolysis_f_fff(@NotNull RecipeOutput gen, FluidObject i, int a, FluidObject o1, int b1,
			FluidObject o2, int b2, FluidObject o3, int b3, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i.fluid(), a))
            .out(new FluidStack(o1.fluid(), b1))
            .out(new FluidStack(o2.fluid(), b2))
            .out(new FluidStack(o3.fluid(), b3))
            .energy(energy)
            .save(gen, "electrolysis_f_fff_" + i.name());
        //@formatter:on
	}

	protected static void electrolysis_ff_ff(@NotNull RecipeOutput gen, FluidObject i1, int a1, FluidObject i2, int a2,
			FluidObject o1, int b1, FluidObject o2, int b2, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i1.fluid(), a1))
            .in(new FluidStack(i2.fluid(), a2))
            .out(new FluidStack(o1.fluid(), b1))
            .out(new FluidStack(o2.fluid(), b2))
            .energy(energy)
            .save(gen, "electrolysis_ff_ff_" + o1.name());
        //@formatter:on
	}

	protected static void electrolysis_ff_fff(@NotNull RecipeOutput gen, FluidObject i1, int a1, FluidObject i2, int a2,
			FluidObject o1, int b1, FluidObject o2, int b2, FluidObject o3, int b3, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i1.fluid(), a1))
            .in(new FluidStack(i2.fluid(), a2))
            .out(new FluidStack(o1.fluid(), b1))
            .out(new FluidStack(o2.fluid(), b2))
            .out(new FluidStack(o3.fluid(), b3))
            .energy(energy)
            .save(gen, "electrolysis_ff_fff_" + o1.name());
        //@formatter:on
	}

	protected static void electrolysis_ff_ffi(@NotNull RecipeOutput gen, FluidObject i1, int a1, FluidObject i2, int a2,
			FluidObject o1, int b1, FluidObject o2, int b2, DeferredItem<? extends Item> o3, int p, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i1.fluid(), a1))
            .in(new FluidStack(i2.fluid(), a2))
            .out(new FluidStack(o1.fluid(), b1))
            .out(new FluidStack(o2.fluid(), b2))
            .out(o3.get(), 1)
            .period(p)
            .energy(energy)
            .save(gen, "electrolysis_ff_ffi_" + getItemName(o3.get()));
        //@formatter:on
	}

	protected static void electrolysis_fi_f(@NotNull RecipeOutput gen, FluidObject i, int a,
			DeferredItem<? extends Item> o1, int a2, FluidObject o2, int b2, int period, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i.fluid(), a))
            .in(o1.get(), a2)
            .out(new FluidStack(o2.fluid(), b2))
            .period(period)
            .energy(energy)
            .save(gen, "electrolysis_fi_f_" + getItemName(o1.get()));
        //@formatter:on
	}

	protected static void electrolysis_fi_fi(@NotNull RecipeOutput gen, FluidObject i, int a,
			DeferredItem<? extends Item> o1, int a2, FluidObject o2, int b2, DeferredItem<? extends Item> o3, int p,
			int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i.fluid(), a))
            .in(o1.get(), a2)
            .out(new FluidStack(o2.fluid(), b2))
            .out(o3.get(), 1)
            .period(p)
            .energy(energy)
            .save(gen, "electrolysis_fi_fi_" + getItemName(o1.get()));
        //@formatter:on
	}

	protected static void electrolysis_f_fi(@NotNull RecipeOutput gen, FluidObject i, int a, FluidObject o1, int b1,
			DeferredItem<? extends Item> o2, int p, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(new FluidStack(i.fluid(), a))
            .out(new FluidStack(o1.fluid(), b1))
            .out(o2.get(), 1)
            .period(p)
            .energy(energy)
            .save(gen, "electrolysis_f_fi_" + getItemName(o2.get()));
        //@formatter:on
	}

	protected static void electrolysis_i_ff_c(@NotNull RecipeOutput gen, DeferredItem<? extends Item> i, int a,
			FluidObject o1, int b1, FluidObject o2, int b2, DeferredItem<? extends Item> c, int p, int energy) {
		//@formatter:off
        MachinaRecipeBuilder.create(RecipeInit.ELECTROLYZER)
            .in(i.get(), a)
            .in(c.get(), 1)
            .out(new FluidStack(o1.fluid(), b1))
            .out(new FluidStack(o2.fluid(), b2))
            .period(p)
            .energy(energy)
            .save(gen, "electrolysis_i_ff_c_" + getItemName(i.get()));
        //@formatter:on
	}

	protected static void rocket_part(@NotNull RecipeOutput gen,
			DeferredHolder<RocketPart, ? extends RocketPart> reg, int energy, ItemStack... in) {
		MachinaRecipeBuilder<?> builder = MachinaRecipeBuilder.create(RecipeInit.ROCKET_PART_BENCH);
		RocketPartItem item = reg.get().getItem();
		for (ItemStack stack : in) {
			builder.in(stack);
		}
		builder.out(item).energy(energy).save(gen, getItemName(item));
	}
}
