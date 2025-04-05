package com.machina.recipe;

import java.util.List;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.block.entity.machine.MixerBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class MixerRecipe extends MachinaRecipe<MixerBlockEntity> {

	public MixerRecipe(ResourceLocation loc, int energy, int time, float pressure, float temperature, float xp,
			List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems,
			List<FluidStack> outputFluids) {
		super(loc, energy, time, pressure, temperature, xp, inputItems, inputFluids, outputItems, outputFluids);
	}

	@Override
	protected RecipeRegistryObject<MixerBlockEntity> getRegistryObject() {
		return RecipeInit.MIXER;
	}

	@Override
	public RecipeFactory<MachinaRecipe<MixerBlockEntity>> getFactory() {
		return MixerRecipe::new;
	}
}
