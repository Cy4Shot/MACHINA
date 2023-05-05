package com.machina.client.jei.category;

import com.machina.util.MachinaRL;
import com.machina.util.StringUtils;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public abstract class BaseCategory<T> implements IRecipeCategory<T> {

	final Class<T> clazz;

	protected IDrawable icon;
	protected IDrawable back;

	public BaseCategory(IGuiHelper helper, Class<T> clazz, IItemProvider icon, int bgx,
			int bgy) {
		this.clazz = clazz;

		this.icon = helper.createDrawableIngredient(new ItemStack(icon));
		this.back = helper.createDrawable(bg(), 0, 0, bgx, bgy);
	}
	
	protected ResourceLocation bg() {
		return new MachinaRL("textures/gui/jei/" + name() + ".png");
	}

	public String name() {
		return getUid().getPath();
	}

	@Override
	public Class<? extends T> getRecipeClass() {
		return this.clazz;
	}

	@Override
	public String getTitle() {
		return StringUtils.translate("gui.jei.category." + name());
	}

	@Override
	public IDrawable getBackground() {
		return this.back;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

}
