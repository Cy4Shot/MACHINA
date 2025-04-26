package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.api.util.MachinaRL;
import com.machina.block.entity.machine.ComposterVatBlockEntity;
import com.machina.recipe.ComposterVatRecipe;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.fluids.FluidStack;

public class ComposterVatRecipeMaps extends MachinaRecipeMaps<ComposterVatBlockEntity> {

	public static final ComposterVatRecipeMaps INSTANCE = new ComposterVatRecipeMaps();

	@Override
	public boolean isValid(ComposterVatBlockEntity entity, MachinaRecipe<ComposterVatBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

	@Override
	protected RecipeRegistryObject<ComposterVatBlockEntity> getRegistryObject() {
		return RecipeInit.COMPOSTER_VAT;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {
		ComposterBlock.COMPOSTABLES.forEach((item, val) -> {
			ResourceLocation loc = new MachinaRL(
					"compost_vat_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath());
			add(new ComposterVatRecipe(loc, 900, 30, 0, 0, List.of(Ingredient.of(item)), List.of(), List.of(),
					List.of(new FluidStack(FluidInit.AMMONIA.fluid(), (int) (1000 * val)))));
		});
	}

	@Override
	public Class<? extends MachinaRecipe<ComposterVatBlockEntity>> getRecipeClass() {
		return ComposterVatRecipe.class;
	}
}