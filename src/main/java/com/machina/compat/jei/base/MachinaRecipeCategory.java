package com.machina.compat.jei.base;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public abstract class MachinaRecipeCategory<C extends Container> implements IRecipeCategory<MachinaRecipe<C>> {

	private final RecipeRegistryObject<C> obj;
	private final IDrawable icon;

	public MachinaRecipeCategory(IGuiHelper gui, RecipeRegistryObject<C> obj, RegistryObject<? extends Block> block) {
		this.obj = obj;
		this.icon = gui.createDrawableItemStack(block.get().asItem().getDefaultInstance());
	}

	@Override
	public @NotNull RecipeType<MachinaRecipe<C>> getRecipeType() {
		return new RecipeType<>(obj.id(), obj.maps().getRecipeClass());
	}

	@Override
	public @NotNull Component getTitle() {
		return Component.translatable(obj.getTranslationKey());
	}

	@Override
	public @NotNull IDrawable getBackground() {
		return null;
	}

	@Override
	public @NotNull IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull MachinaRecipe<C> recipe,
			@NotNull IFocusGroup focuses) {
//		List<Ingredient> iitems = recipe.getInputItems();
//		List<ItemStack> oitems = recipe.getOutputItems();
//		List<FluidStack> ifluids = recipe.getInputFluids();
//		List<FluidStack> ofluids = recipe.getOutputFluids();
//		
//		int total_inputs = iitems.size() + ifluids.size();
//		int total_outputs = oitems.size() + ofluids.size();
	}

}
