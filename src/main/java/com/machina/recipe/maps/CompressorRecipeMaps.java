package com.machina.recipe.maps;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.item.MouldItem;
import com.machina.recipe.CompressorRecipe;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.tags.ITagManager;

public class CompressorRecipeMaps extends MachinaRecipeMaps<CompressorBlockEntity> {

	public static final CompressorRecipeMaps INSTANCE = new CompressorRecipeMaps();

	@Override
	public boolean isValid(CompressorBlockEntity entity, MachinaRecipe<CompressorBlockEntity> recipe) {
		for (Ingredient i : recipe.getInputItems()) {
			if (!entity.hasAnyOf(Arrays.stream(i.getItems()).map(ItemStack::getItem).collect(Collectors.toSet())))
				return false;
		}

		return true;
	}

	@Override
	protected RecipeRegistryObject<CompressorBlockEntity> getRegistryObject() {
		return RecipeInit.COMPRESSOR;
	}

	@Override
	public int getFlags() {
		return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
	}

	private void add(ITagManager<Item> items, String name, TagKey<Item> tag, String type,
			RegistryObject<MouldItem> mould, int quant) {
		items.getTag(ci(type + "/" + name)).forEach(item -> {
			items.getTag(tag).forEach(ingot -> {
				ResourceLocation key = ForgeRegistries.ITEMS.getKey(ingot);
				ResourceLocation iloc = new ResourceLocation(key.getNamespace(),
						"compress_" + key.getPath() + "_" + type);
				add(new CompressorRecipe(iloc, 10000, 200, 0, 0,
						List.of(Ingredient.of(ingot), Ingredient.of(mould.get())), List.of(),
						List.of(new ItemStack(item, quant)), List.of()));
			});
		});
	}

	@Override
	protected void addExtraRecipes(RecipeManager man) {
		ITagManager<Item> items = ForgeRegistries.ITEMS.tags();
		items.getTagNames().forEach(tag -> {
			ResourceLocation loc = tag.location();
			if (loc.getNamespace().equals("c") && loc.getPath().startsWith("ingots/")) {
				String name = loc.getPath().replaceFirst("ingots/", "");
				add(items, name, tag, "plates", ItemInit.MOULD_PLATE, 1);
				add(items, name, tag, "rods", ItemInit.MOULD_ROD, 1);
				add(items, name, tag, "wires", ItemInit.MOULD_WIRE, 2);
			}
		});
	}

	@Override
	public Class<? extends MachinaRecipe<CompressorBlockEntity>> getRecipeClass() {
		return CompressorRecipe.class;
	}
}