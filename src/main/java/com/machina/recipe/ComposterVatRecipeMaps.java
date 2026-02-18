package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.api.util.MachinaRL;
import com.machina.block.entity.machine.ComposterVatBlockEntity;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class ComposterVatRecipeMaps extends MachinaRecipeMaps<ComposterVatBlockEntity> {

	public static final ComposterVatRecipeMaps INSTANCE = new ComposterVatRecipeMaps();

	@Override
	protected RecipeRegistryObject<ComposterVatBlockEntity> getRegistryObject() {
		return RecipeInit.COMPOSTER_VAT;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man, RegistryAccess access) {
		BuiltInRegistries.ITEM.getDataMap(NeoForgeDataMaps.COMPOSTABLES).forEach((item, compost) -> {
			ResourceLocation loc = MachinaRL.create("compost_vat_" + item.location().getPath());
			builder().energy(900).time(30).in(BuiltInRegistries.ITEM.get(item))
					.out(FluidInit.AMMONIA.fluid(), (int) (1000 * compost.chance())).save(loc, this::add);
		});
	}
}