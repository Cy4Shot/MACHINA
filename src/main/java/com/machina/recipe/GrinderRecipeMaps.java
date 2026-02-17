package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.GrinderBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;

public class GrinderRecipeMaps extends MachinaRecipeMaps<GrinderBlockEntity> {

    public static final GrinderRecipeMaps INSTANCE = new GrinderRecipeMaps();

    @Override
    protected RecipeRegistryObject<GrinderBlockEntity> getRegistryObject() {
        return RecipeInit.GRINDER;
    }

    @Override
    public int getFlags() {
        return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
    }

    private void add(Registry<Item> items, String name, TagKey<Item> tag, String type, int energy, int time) {
        items.getTag(ci(type + "/" + name))
                .ifPresent(y -> y.forEach(item -> items.getTag(tag).ifPresent(z -> z.forEach(x -> {
                    ResourceLocation key = items.getKey(item.value());
                    if (key == null) {
                        return;
                    }
                    ResourceLocation iloc = ResourceLocation.fromNamespaceAndPath(key.getNamespace(),
                            "grinder_" + key.getPath() + "_" + type);
                    builder().energy(energy).time(time).in(item.value()).out(x.value()).save(iloc, this::add);
                }))));
    }

    @Override
    protected void addExtraRecipes(RecipeManager man) {
        BuiltInRegistries.ITEM.getTagNames().forEach(tag -> {
            ResourceLocation loc = tag.location();
            if (loc.getNamespace().equals("c") && loc.getPath().startsWith("dusts/")) {
                String name = loc.getPath().replaceFirst("dusts/", "");
                add(BuiltInRegistries.ITEM, name, tag, "ingots", 15000, 200);
                add(BuiltInRegistries.ITEM, name, tag, "ores", 40000, 250);
                add(BuiltInRegistries.ITEM, name, tag, "raw_materials", 15000, 200);
            }
        });
    }
}