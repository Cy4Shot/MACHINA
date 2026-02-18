package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

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
	protected void addExtraRecipes(RecipeManager man, RegistryAccess access) {
		BuiltInRegistries.ITEM.getTagNames().forEach(tag -> {
			ResourceLocation loc = tag.location();
			if (loc.getNamespace().equals("c") && loc.getPath().startsWith("ingots/")) {
				String name = loc.getPath().replaceFirst("ingots/", "");
				BuiltInRegistries.FLUID.getTag(cf("molten_" + name)).ifPresent(x -> x.forEach(fluid -> {
					if (fluid instanceof BaseFlowingFluid f) {
						BuiltInRegistries.ITEM.getTag(tag).ifPresent(y -> y.forEach(item -> {
							ResourceLocation key = BuiltInRegistries.ITEM.getKey(item.value());
							if (key == null) {
								return;
							}

							ResourceLocation iloc = ResourceLocation.fromNamespaceAndPath(key.getNamespace(),
									"melt_" + key.getPath());
							builder().energy(1820).time(70).in(item.value()).out(f.getSource(), 144).save(iloc,
									this::add);
						}));
					}
				}));
			}
		});
	}
}