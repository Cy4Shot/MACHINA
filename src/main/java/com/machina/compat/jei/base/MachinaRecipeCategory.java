package com.machina.compat.jei.base;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public abstract class MachinaRecipeCategory<C extends Container> implements IRecipeCategory<MachinaRecipe<C>> {

	private final RecipeRegistryObject<C> obj;
	private final IDrawable icon, bg;

	public MachinaRecipeCategory(IGuiHelper gui, RecipeRegistryObject<C> obj, RegistryObject<? extends Block> block) {
		this.obj = obj;
		this.icon = gui.createDrawableItemStack(block.get().asItem().getDefaultInstance());
		this.bg = gui.drawableBuilder(MachinaMenuScreen.COMMON_UI, 179, 94, 235, 139).setTextureSize(512, 512).build();
	}

	@Override
	public @NotNull RecipeType<MachinaRecipe<C>> getRecipeType() {
		return obj.jeiType();
	}

	@Override
	public @NotNull Component getTitle() {
		return Component.translatable(obj.getTranslationKey());
	}

	@Override
	public @NotNull IDrawable getBackground() {
		return this.bg;
	}

	@Override
	public @NotNull IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void draw(MachinaRecipe<C> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
			double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull MachinaRecipe<C> recipe,
			@NotNull IFocusGroup focuses) {

		builder.addSlot(RecipeIngredientRole.INPUT, 10, 10).addItemStack(Items.DIAMOND.getDefaultInstance())
				.setSlotName("diamond");

//		List<Ingredient> iitems = recipe.getInputItems();
//		List<ItemStack> oitems = recipe.getOutputItems();
//		List<FluidStack> ifluids = recipe.getInputFluids();
//		List<FluidStack> ofluids = recipe.getOutputFluids();
//		
//		int total_inputs = iitems.size() + ifluids.size();
//		int total_outputs = oitems.size() + ofluids.size();
	}

}
