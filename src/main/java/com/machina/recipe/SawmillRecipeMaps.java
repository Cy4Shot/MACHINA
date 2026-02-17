package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.SawmillBlockEntity;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;

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
        // Log -> Planks
        BuiltInRegistries.ITEM.getTagNames().forEach(tag -> {
            ResourceLocation loc = tag.location();
            if (loc.getNamespace().equals("c") && loc.getPath().startsWith("logs/")) {
                String name = loc.getPath().replaceFirst("logs/", "");
                BuiltInRegistries.ITEM.getTag(ci("planks/" + name)).ifPresent(
                        x -> x.forEach(small -> BuiltInRegistries.ITEM.getTag(tag).ifPresent(y -> y.forEach(big -> {
                            ResourceLocation key = BuiltInRegistries.ITEM.getKey(big.value());
                            if (key == null) {
                                return;
                            }

                            ResourceLocation iloc = ResourceLocation.fromNamespaceAndPath(key.getNamespace(),
                                    "saw_" + key.getPath());
                            builder().energy(2000).time(100).in(big.value(), 1).out(small.value(), 6).save(iloc,
                                    this::add);
                        }))));
            }
        });

        // Planks -> Sticks
        BuiltInRegistries.ITEM.getTagNames().forEach(tag -> {
            ResourceLocation loc = tag.location();
            if (loc.getNamespace().equals("c") && loc.getPath().startsWith("planks/")) {
                BuiltInRegistries.ITEM.getTag(tag).ifPresent(x -> x.forEach(big -> {
                    ResourceLocation key = BuiltInRegistries.ITEM.getKey(big.value());
                    if (key == null) {
                        return;
                    }

                    ResourceLocation iloc = ResourceLocation.fromNamespaceAndPath(key.getNamespace(),
                            "saw_" + key.getPath());
                    builder().energy(2000).time(100).in(big.value(), 1).out(Items.STICK, 4).save(iloc, this::add);
                }));
            }
        });
    }
}