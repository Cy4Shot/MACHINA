package com.machina.recipe.maps;

import java.util.List;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.SolidifierBlockEntity;
import com.machina.recipe.SolidifierRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class SolidifierRecipeMaps extends MachinaRecipeMaps<SolidifierBlockEntity> {

	public static final SolidifierRecipeMaps INSTANCE = new SolidifierRecipeMaps();

	@Override
	protected RecipeRegistryObject<SolidifierBlockEntity> getRegistryObject() {
		return RecipeInit.SOLIDIFIER;
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
				fluids.getTag(cf("molten_" + name)).forEach(fluid -> {
					if (fluid instanceof ForgeFlowingFluid f) {
						items.getTag(tag).forEach(item -> {
							ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
							ResourceLocation iloc = new ResourceLocation(key.getNamespace(), "melt_" + key.getPath());
							add(new SolidifierRecipe(iloc, 1820, 70, 0, 0, List.of(),
									List.of(new FluidStack(f.getSource(), 144)), List.of(new ItemStack(item, 1)),
									List.of()));
						});
					}
				});
			}
		});
	}

	@Override
	public Class<? extends MachinaRecipe<SolidifierBlockEntity>> getRecipeClass() {
		return SolidifierRecipe.class;
	}
}