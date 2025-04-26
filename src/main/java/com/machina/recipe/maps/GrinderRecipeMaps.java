package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.recipe.GrinderRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class GrinderRecipeMaps extends MachinaRecipeMaps<GrinderBlockEntity> {

	public static final GrinderRecipeMaps INSTANCE = new GrinderRecipeMaps();

	@Override
	public boolean isValid(GrinderBlockEntity entity, MachinaRecipe<GrinderBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

	@Override
	protected RecipeRegistryObject<GrinderBlockEntity> getRegistryObject() {
		return RecipeInit.GRINDER;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	private void add(ITagManager<Item> items, String name, TagKey<Item> tag, String type, int energy, int time) {
		items.getTag(ci(type + "/" + name)).forEach(item -> {
			items.getTag(tag).forEach(x -> {
				ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
				ResourceLocation iloc = new ResourceLocation(key.getNamespace(),
						"grinder_" + key.getPath() + "_" + type);
				add(new GrinderRecipe(iloc, energy, time, 0, 0, List.of(Ingredient.of(item)), List.of(),
						List.of(new ItemStack(x, 1)), List.of()));
			});
		});
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {
		ITagManager<Item> items = ForgeRegistries.ITEMS.tags();
		items.getTagNames().forEach(tag -> {
			ResourceLocation loc = tag.location();
			if (loc.getNamespace().equals("c") && loc.getPath().startsWith("dusts/")) {
				String name = loc.getPath().replaceFirst("dusts/", "");
				add(items, name, tag, "ingots", 15000, 200);
				add(items, name, tag, "ores", 40000, 250);
				add(items, name, tag, "raw_materials", 15000, 200);
			}
		});
	}

	@Override
	public Class<? extends MachinaRecipe<GrinderBlockEntity>> getRecipeClass() {
		return GrinderRecipe.class;
	}
}