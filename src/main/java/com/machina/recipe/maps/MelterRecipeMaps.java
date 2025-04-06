package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.recipe.MelterRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class MelterRecipeMaps extends MachinaRecipeMaps<MelterBlockEntity> {

	public static final MelterRecipeMaps INSTANCE = new MelterRecipeMaps();

	@Override
	public boolean isValid(MelterBlockEntity entity, MachinaRecipe<MelterBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

	@Override
	protected RecipeRegistryObject<MelterBlockEntity> getRegistryObject() {
		return RecipeInit.MELTER;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {
		ITagManager<Item> items = ForgeRegistries.ITEMS.tags();
		ITagManager<Fluid> fluids = ForgeRegistries.FLUIDS.tags();
		items.getTagNames().forEach(tag -> {
			ResourceLocation loc = tag.location();
			if (loc.getNamespace().equals("c") && loc.getPath().startsWith("ingots/")) {
				String name = loc.getPath().replaceFirst("ingots/", "");
				fluids.getTag(TagKey.create(Registries.FLUID, new ResourceLocation("c", "molten_" + name)))
						.forEach(fluid -> {
							if (fluid instanceof ForgeFlowingFluid f) {
								items.getTag(tag).forEach(item -> {
									ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
									ResourceLocation iloc = new ResourceLocation(key.getNamespace(),
											"melt_" + key.getPath());
									add(new MelterRecipe(iloc, 20000, 200, 0, 0, List.of(Ingredient.of(item)),
											List.of(), List.of(), List.of(new FluidStack(f.getSource(), 144))));
								});
							}
						});
			}
		});
	}

	@Override
	public Class<? extends MachinaRecipe<MelterBlockEntity>> getRecipeClass() {
		return MelterRecipe.class;
	}
}