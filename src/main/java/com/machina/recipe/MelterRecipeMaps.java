package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class MelterRecipeMaps extends MachinaRecipeMaps<MelterBlockEntity> {

	public static final MelterRecipeMaps INSTANCE = new MelterRecipeMaps();

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
				fluids.getTag(cf("molten_" + name)).forEach(fluid -> {
					if (fluid instanceof ForgeFlowingFluid f) {
						items.getTag(tag).forEach(item -> {
							ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
							ResourceLocation iloc = new ResourceLocation(key.getNamespace(), "melt_" + key.getPath());
							builder().energy(1820).time(70).in(item).out(f.getSource(), 144).save(iloc, this::add);
						});
					}
				});
			}
		});
	}
}