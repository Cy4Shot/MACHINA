package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.SolidifierBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
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
							builder().energy(1820).time(70).in(f.getSource(), 144).out(item).save(iloc, this::add);
						});
					}
				});
			}
		});
	}
}