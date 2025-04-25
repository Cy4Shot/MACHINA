package com.machina.compat.jei.base;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.util.StringUtils;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;
import com.mojang.datafixers.util.Pair;

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
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

public class MachinaRecipeCategory<C extends Container> implements IRecipeCategory<MachinaRecipe<C>> {

	private static final Minecraft mc = Minecraft.getInstance();

	private final RecipeRegistryObject<C> obj;
	private final IDrawable icon, bg;
	private final ITickTimer ticktimer;

	public MachinaRecipeCategory(IGuiHelper gui, RecipeRegistryObject<C> obj, RegistryObject<? extends Block> block) {
		this.obj = obj;
		this.icon = gui.createDrawableItemStack(block.get().asItem().getDefaultInstance());
		this.bg = new IDrawable() {
			@Override
			public int getWidth() {
				return 129;
			}

			@Override
			public int getHeight() {
				return obj.jei().height() + 16;
			}

			@Override
			public void draw(@NotNull GuiGraphics gui, int x, int y) {
			}
		};
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

	@Override
	public void draw(MachinaRecipe<C> recipe, IRecipeSlotsView view, GuiGraphics gui, double mx, double my) {

		List<Ingredient> iitems = recipe.getInputItems();
		List<ItemStack> oitems = recipe.getOutputItems();
		List<FluidStack> ifluids = recipe.getInputFluids();
		List<FluidStack> ofluids = recipe.getOutputFluids();

		int total_inputs = iitems.size() + ifluids.size();
		int total_outputs = oitems.size() + ofluids.size();

		int rolling = 7;

		// Draw Background
		int h = obj.jei().height();
		MUI.blitJei(gui, 0, 0, 0, 0, 129, 19);
		for (int i = 0; i < (h - 19) / 50; i++) {
			MUI.blitJei(gui, 0, 19 + i * 50, 0, 19, 129, 50);
		}
		// Draw remainder
		int r = (h - 19) % 50;
		int tot = 19 + (h - 19) / 50 * 50;
		MUI.blitJei(gui, 0, tot, 0, 19, 129, r);

		MUI.blitJei(gui, 0, h, 0, 144, 129, 16);

		// Input Slots
		if (total_inputs > 0) {
			Component c = MUI.uistr("jei.input");
			int w = mc.font.width(c) / 2 + 2;
			MUI.drawCenteredString(gui, c, 64, rolling);
			MUI.blitCommon(gui, 64 + w, rolling + 2, 388, 80, 19, 8);
			MUI.blitCommon(gui, 64 - w - 20, rolling + 2, 369, 80, 19, 8);

			MUI.blitJei(gui, 10, rolling + 12, 148, 0, 108, 28);

			int start = 64 - total_inputs * 10;

			for (int x = 0; x < total_inputs; x++) {
				int i = start + x * 20;
				int j = rolling + 17;
				int m = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
				MUI.blitCommon(gui, i, j, 466, m, 19, 19);
			}

			MUI.blitCommon(gui, 14, rolling + 40, 485, 101, 6, 24);
			MUI.blitCommon(gui, 108, rolling + 40, 485, 101, 6, 24);

			MUI.blitJei(gui, 56, rolling + 44, obj.jei().x, obj.jei().y, 16, 16);
			MUI.blitCommon(gui, 48, rolling + 44, 387, 0, 3, 16);
			MUI.blitCommon(gui, 77, rolling + 44, 390, 0, 3, 16);

			rolling += 64;
		}

		// Output Slots
		if (total_outputs > 0) {
			Component c = MUI.uistr("jei.output");
			int w = mc.font.width(c) / 2 + 2;
			MUI.drawCenteredString(gui, c, 64, rolling + 32);
			MUI.blitCommon(gui, 64 + w, rolling + 30, 418, 5, 19, 8);
			MUI.blitCommon(gui, 64 - w - 20, rolling + 30, 399, 5, 19, 8);

			MUI.blitJei(gui, 10, rolling, 148, 0, 108, 28);

			int start = 64 - total_outputs * 10;

			for (int x = 0; x < total_outputs; x++) {
				int i = start + x * 20;
				int j = rolling + 5;
				int m = mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
				MUI.blitCommon(gui, i, j, 466, m, 19, 19);
			}
			rolling += 50;
		}

		// Flags
		List<Pair<MuiSlot, String>> values = new ArrayList<>();
		if (recipe.hasEnergy()) {
			values.add(Pair.of(MuiSlot.ENERGY, StringUtils.formatPower(recipe.getEnergy())));
		}
		if (recipe.hasTemperature()) {
			values.add(Pair.of(MuiSlot.TEMP, StringUtils.formatTemp(recipe.getTemperature())));
		}
		if (recipe.hasTime()) {
			values.add(Pair.of(MuiSlot.TIME, StringUtils.formatTicks(recipe.getTime())));
		}

		for (int i = 0; i < values.size(); i++) {
			int x = 6 + (i % 2) * 58;
			int y = rolling + (i / 2) * 14;
			Pair<MuiSlot, String> value = values.get(i);
			value.getFirst().draw(gui, x, y);
			MUI.drawString(gui, Component.literal(value.getSecond()), x + 14, y + 1);
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
		int total_outputs = oitems.size() + ofluids.size();

		int rolling = 7;
		int starti = 65 - total_inputs * 10;

		int i = 0;
		for (Ingredient ingredient : iitems) {
			int x = starti + i * 20;
			int y = rolling + 18;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredient);
			i++;
		}
		for (FluidStack fluid : ifluids) {
			int x = starti + i * 20;
			int y = rolling + 18;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).setFluidRenderer(1, false, 16, 16)
					.addFluidStack(fluid.getFluid(), fluid.getAmount());
			i++;
		}

		if (total_inputs > 0) {
			rolling += 64;
		}

		int starto = 65 - total_outputs * 10;
		int j = 0;
		for (ItemStack item : oitems) {
			int x = starto + j * 20;
			int y = rolling + 6;
			builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(item);
			j++;
		}
		for (FluidStack fluid : ofluids) {
			int x = starto + j * 20;
			int y = rolling + 6;
			builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).setFluidRenderer(1, false, 16, 16)
					.addFluidStack(fluid.getFluid(), fluid.getAmount());
			j++;
		}
	}

}
