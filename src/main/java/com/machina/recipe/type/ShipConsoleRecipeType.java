package com.machina.recipe.type;

import com.machina.Machina;
import com.machina.recipe.ShipConsoleRecipe;

import net.minecraft.item.crafting.IRecipeType;

public class ShipConsoleRecipeType implements IRecipeType<ShipConsoleRecipe> {
	
	@Override
	public String toString() {
		return Machina.MOD_ID + ":ship_console";
	}

}
