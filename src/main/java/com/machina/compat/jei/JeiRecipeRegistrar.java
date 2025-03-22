package com.machina.compat.jei;

import java.util.function.Function;

import com.machina.Machina;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.compat.jei.base.MachinaRecipeCategory;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class JeiRecipeRegistrar<C extends Container> {
	private final RecipeType<MachinaRecipe<C>> type;
	private final Function<IGuiHelper, MachinaRecipeCategory<C>> category;

	public JeiRecipeRegistrar(RecipeRegistryObject<C> obj, RegistryObject<? extends Block> block) {
		this.type = RecipeType.create(Machina.MOD_ID, obj.id().getPath(), obj.maps().getRecipeClass());
		this.category = gui -> new MachinaRecipeCategory<C>(gui, obj, block);
	}

	public RecipeType<MachinaRecipe<C>> type() {
		return type;
	}
	
	public MachinaRecipeCategory<C> category(IGuiHelper gui) {
		return category.apply(gui);
	}
}
