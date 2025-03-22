package com.machina.compat.jei.base;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.util.MachinaRL;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.util.TickTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

public class MachinaRecipeCategory<C extends Container> implements IRecipeCategory<MachinaRecipe<C>> {

	private static final Minecraft mc = Minecraft.getInstance();
	private static final ResourceLocation JEI_UI = new MachinaRL("textures/gui/jei_ui.png");

	private final RecipeRegistryObject<C> obj;
	private final IDrawable icon, bg;
	private final ITickTimer ticktimer;

	public MachinaRecipeCategory(IGuiHelper gui, RecipeRegistryObject<C> obj, RegistryObject<? extends Block> block) {
		this.obj = obj;
		this.icon = gui.createDrawableItemStack(block.get().asItem().getDefaultInstance());
		this.bg = gui.createDrawable(JEI_UI, 0, 0, 129, 160);
		this.ticktimer = new TickTimer(20, 3, false);
	}

	@Override
	public @NotNull RecipeType<MachinaRecipe<C>> getRecipeType() {
		return obj.jei().type();
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

	public static void blitJei(GuiGraphics gui, int x, int y, int u, int v, int w, int h) {
		gui.blit(JEI_UI, x, y, u, v, w, h, 256, 256);
	}

	@Override
	public void draw(MachinaRecipe<C> recipe, IRecipeSlotsView view, GuiGraphics gui, double mx, double my) {

		List<Ingredient> iitems = recipe.getInputItems();
		List<ItemStack> oitems = recipe.getOutputItems();
		List<FluidStack> ifluids = recipe.getInputFluids();
		List<FluidStack> ofluids = recipe.getOutputFluids();

		int total_inputs = iitems.size() + ifluids.size();
		int total_outputs = oitems.size() + ofluids.size();

		int rolling = 7;

		// Input Slots
		if (total_inputs > 0) {
			Component c = MUI.uistr("jei.input");
			int w = mc.font.width(c) / 2 + 2;
			MUI.drawCenteredString(gui, c, 64, rolling, 0x00FEFE);
			MUI.blitCommon(gui, 64 + w, rolling + 2, 388, 80, 19, 8);
			MUI.blitCommon(gui, 64 - w - 20, rolling + 2, 369, 80, 19, 8);

			blitJei(gui, 10, rolling + 12, 148, 0, 108, 28);

			int start = 64 - total_inputs * 10;

			for (int x = 0; x < total_inputs; x++) {
				int i = start + x * 20;
				int j = rolling + 17;
				int h = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
				MUI.blitCommon(gui, i, j, 466, h, 19, 19);
			}
			
			MUI.blitCommon(gui, 14, rolling + 40, 485, 101, 6, 24);
			MUI.blitCommon(gui, 108, rolling + 40, 485, 101, 6, 24);
			
			rolling += 64;
		}

		// Output Slots
		if (total_outputs > 0) {
			Component c = MUI.uistr("jei.output");
			int w = mc.font.width(c) / 2 + 2;
			MUI.drawCenteredString(gui, c, 64, rolling + 32, 0x00FEFE);
			MUI.blitCommon(gui, 64 + w, rolling + 30, 418, 5, 19, 8);
			MUI.blitCommon(gui, 64 - w - 20, rolling + 30, 399, 5, 19, 8);

			blitJei(gui, 10, rolling, 148, 0, 108, 28);

			int start = 64 - total_outputs * 10;

			for (int x = 0; x < total_outputs; x++) {
				int i = start + x * 20;
				int j = rolling + 5;
				int h = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
				MUI.blitCommon(gui, i, j, 466, h, 19, 19);
			}
			rolling += 34;
		}

		// Overlay
		int k = this.ticktimer.getValue();
		MUI.blitOverlay(gui, 0, k, getWidth(), getHeight() - k);
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull MachinaRecipe<C> recipe,
			@NotNull IFocusGroup focuses) {

		List<Ingredient> iitems = recipe.getInputItems();
		List<ItemStack> oitems = recipe.getOutputItems();
		List<FluidStack> ifluids = recipe.getInputFluids();
		List<FluidStack> ofluids = recipe.getOutputFluids();
		int total_inputs = iitems.size() + ifluids.size();
//		int total_outputs = oitems.size() + ofluids.size();

		int rolling = 24;

		int i = 0;
		for (Ingredient ingredient : iitems) {
			builder.addSlot(RecipeIngredientRole.INPUT, 55 + i * 20, rolling + 6).addIngredients(ingredient);
			i++;
		}
		for (FluidStack fluid : ifluids) {
			builder.addSlot(RecipeIngredientRole.INPUT, 55 + i * 20, rolling + 6).setFluidRenderer(1, false, 16, 16)
					.addFluidStack(fluid.getFluid(), 1);
			i++;
		}

		if (total_inputs > 0) {

		}

		int j = 0;
		for (ItemStack item : oitems) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 21 + j * 20).addItemStack(item);
			j++;
		}
		for (FluidStack fluid : ofluids) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 21 + j * 20).setFluidRenderer(1, false, 16, 16)
					.addFluidStack(fluid.getFluid(), 1);
			j++;
		}
	}

}
