package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.SawmillBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class SawmillRecipeMaps extends MachinaRecipeMaps<SawmillBlockEntity> {

	public static final SawmillRecipeMaps INSTANCE = new SawmillRecipeMaps();

	@Override
	protected RecipeRegistryObject<SawmillBlockEntity> getRegistryObject() {
		return RecipeInit.SAWMILL;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {
		ITagManager<Item> items = ForgeRegistries.ITEMS.tags();

		// Log -> Planks
		items.getTagNames().forEach(tag -> {
			ResourceLocation loc = tag.location();
			if (loc.getNamespace().equals("c") && loc.getPath().startsWith("logs/")) {
				String name = loc.getPath().replaceFirst("logs/", "");
				items.getTag(ci("planks/" + name)).forEach(small -> {
					items.getTag(tag).forEach(big -> {
						ResourceLocation key = ForgeRegistries.ITEMS.getKey(big);
						ResourceLocation iloc = new ResourceLocation(key.getNamespace(), "saw_" + key.getPath());
						builder().energy(2000).time(100).in(big, 1).out(small, 6).save(iloc, this::add);
					});
				});
			}
		});

		// Planks -> Sticks
		items.getTagNames().forEach(tag -> {
			ResourceLocation loc = tag.location();
			if (loc.getNamespace().equals("c") && loc.getPath().startsWith("planks/")) {
				items.getTag(tag).forEach(big -> {
					ResourceLocation key = ForgeRegistries.ITEMS.getKey(big);
					ResourceLocation iloc = new ResourceLocation(key.getNamespace(), "saw_" + key.getPath());
					builder().energy(2000).time(100).in(big, 1).out(Items.STICK, 4).save(iloc, this::add);
				});
			}
		});
	}
}