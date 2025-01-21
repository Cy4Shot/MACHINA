package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.api.util.MachinaRL;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.recipe.GrinderRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;

public class GrinderRecipeMaps extends MachinaRecipeMaps<GrinderBlockEntity> {

	public static final GrinderRecipeMaps INSTANCE = new GrinderRecipeMaps();

	@Override
	public boolean isValid(GrinderBlockEntity entity, MachinaRecipe<GrinderBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		System.out.println("Valid recipe: " + recipe.getId());

		return true;
	}

	@Override
	protected RecipeRegistryObject<GrinderBlockEntity> getRegistryObject() {
		return RecipeInit.GRINDER;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {
		for (BlastingRecipe recipe : man.getAllRecipesFor(RecipeType.BLASTING)) {
			if (recipe.isSpecial()) {
				continue;
			}

			ItemStack ingot = recipe.result;
			if (!ingot.is(Tags.Items.INGOTS))
				continue;

			Ingredient iingot = Ingredient.of(ingot);
			ItemStack dust = null;
			ItemStack ore = null;
			ItemStack raw = null;

			Ingredient input = recipe.getIngredients().get(0);
			for (ItemStack i : input.getItems()) {
				if (i.is(Tags.Items.ORES)) {
					ore = i;
				} else if (i.is(Tags.Items.DUSTS)) {
					dust = i;
				} else if (i.is(Tags.Items.RAW_MATERIALS)) {
					raw = i;
				}
			}

			if (dust == null)
				continue;

			add(ingot(iingot, dust));

			if (ore != null) {
				add(ore(Ingredient.of(ore), dust));
			}

			if (raw != null) {
				add(raw(Ingredient.of(raw), dust));
			}
		}
	}

	private static final int DEFAULT_TIME = 200;
	private static final int DEFAULT_ENERGY = 10000;

	private Pair<ResourceLocation, MachinaRecipe<GrinderBlockEntity>> ingot(Ingredient input, ItemStack dust) {
		ResourceLocation loc = new MachinaRL("grinder_ingot_" + input.hashCode());
		return Pair.of(loc,
				new GrinderRecipe(loc, DEFAULT_ENERGY, DEFAULT_TIME, 0, 0, 0.0F, Collections.singletonList(input),
						Collections.emptyList(), Collections.singletonList(new ItemStack(dust.getItem(), 1)),
						Collections.emptyList()));
	}

	private Pair<ResourceLocation, MachinaRecipe<GrinderBlockEntity>> ore(Ingredient input, ItemStack dust) {
		ResourceLocation loc = new MachinaRL("grinder_ore_" + input.hashCode());
		return Pair.of(loc,
				new GrinderRecipe(loc, DEFAULT_ENERGY, DEFAULT_TIME, 0, 0, 0.2F, Collections.singletonList(input),
						Collections.emptyList(), Collections.singletonList(new ItemStack(dust.getItem(), 1)),
						Collections.emptyList()));
	}

	private Pair<ResourceLocation, MachinaRecipe<GrinderBlockEntity>> raw(Ingredient input, ItemStack dust) {
		ResourceLocation loc = new MachinaRL("grinder_raw_" + input.hashCode());
		return Pair.of(loc,
				new GrinderRecipe(loc, DEFAULT_ENERGY, DEFAULT_TIME, 0, 0, 0.1F, Collections.singletonList(input),
						Collections.emptyList(), Collections.singletonList(new ItemStack(dust.getItem(), 1)),
						Collections.emptyList()));
	}

	@Override
	public Class<? extends MachinaRecipe<GrinderBlockEntity>> getRecipeClass() {
		return GrinderRecipe.class;
	}
}