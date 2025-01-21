package com.machina.recipe;

import java.util.List;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class GrinderRecipe extends MachinaRecipe<GrinderBlockEntity> {

	public GrinderRecipe(ResourceLocation loc, int energy, int time, float pressure, float temperature, float xp,
			List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems,
			List<FluidStack> outputFluids) {
		super(loc, energy, time, pressure, temperature, xp, inputItems, inputFluids, outputItems, outputFluids);
	}

	@Override
	protected RecipeRegistryObject<GrinderBlockEntity> getRegistryObject() {
		return RecipeInit.GRINDER;
	}

	@Override
	public RecipeFactory<MachinaRecipe<GrinderBlockEntity>> getFactory() {
		return GrinderRecipe::new;
	}
}
