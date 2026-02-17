package com.machina.recipe;

import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.item.MouldItem;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.registries.DeferredItem;

public class CompressorRecipeMaps extends MachinaRecipeMaps<CompressorBlockEntity> {

    public static final CompressorRecipeMaps INSTANCE = new CompressorRecipeMaps();

    @Override
    protected RecipeRegistryObject<CompressorBlockEntity> getRegistryObject() {
        return RecipeInit.COMPRESSOR;
    }

    @Override
    public int getFlags() {
        return MachinaRecipe.HAS_ENERGY | MachinaRecipe.HAS_TIME;
    }

    private void add(Registry<Item> items, String name, TagKey<Item> tag, String type, DeferredItem<MouldItem> mould,
            int quant) {
        items.getTag(ci(type + "/" + name))
                .ifPresent(x -> x.forEach(item -> items.getTag(tag).ifPresent(y -> y.forEach(ingot -> {
                    ResourceLocation key = items.getKey(ingot.value());
                    if (key == null) {
                        return;
                    }

                    ResourceLocation iloc = ResourceLocation.fromNamespaceAndPath(key.getNamespace(),
                            "compress_" + key.getPath() + "_" + type);
                    builder().energy(10000).time(200).in(ingot.value()).in(mould.get()).out(item.value(), quant)
                            .save(iloc, this::add);
                }))));
    }

    @Override
    protected void addExtraRecipes(RecipeManager man) {
        BuiltInRegistries.ITEM.getTagNames().forEach(tag -> {
            ResourceLocation loc = tag.location();
            if (loc.getNamespace().equals("c") && loc.getPath().startsWith("ingots/")) {
                String name = loc.getPath().replaceFirst("ingots/", "");
                add(BuiltInRegistries.ITEM, name, tag, "plates", ItemInit.MOULD_PLATE, 1);
                add(BuiltInRegistries.ITEM, name, tag, "rods", ItemInit.MOULD_ROD, 1);
                add(BuiltInRegistries.ITEM, name, tag, "wires", ItemInit.MOULD_WIRE, 2);
            }
        });
    }
}