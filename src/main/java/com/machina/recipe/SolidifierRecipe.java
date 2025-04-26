package com.machina.recipe;

import java.util.List;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.block.entity.machine.SolidifierBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class SolidifierRecipe extends MachinaRecipe<SolidifierBlockEntity> {

	public SolidifierRecipe(ResourceLocation loc, int energy, int time, float pressure, float temperature,
			List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems,
			List<FluidStack> outputFluids) {
		super(loc, energy, time, pressure, temperature, inputItems, inputFluids, outputItems, outputFluids);
	}

	@Override
	protected RecipeRegistryObject<SolidifierBlockEntity> getRegistryObject() {
		return RecipeInit.SOLIDIFIER;
	}

	@Override
	public RecipeFactory<MachinaRecipe<SolidifierBlockEntity>> getFactory() {
		return SolidifierRecipe::new;
	}
}
