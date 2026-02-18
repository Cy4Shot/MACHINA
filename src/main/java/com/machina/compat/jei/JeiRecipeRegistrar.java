package com.machina.compat.jei;

import com.machina.Machina;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.compat.jei.base.MachinaRecipeCategory;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class JeiRecipeRegistrar<C extends RecipeInput> {
    private final RecipeType<MachinaRecipe<C>> type;
    private final Function<IGuiHelper, MachinaRecipeCategory<C>> category;
    private final int flags;
    private final boolean hasInputs, hasOutputs;

    public final int x, y;

    @SuppressWarnings("unchecked")
    public JeiRecipeRegistrar(RecipeRegistryObject<C> obj, DeferredBlock<? extends Block> block, int x, int y) {
        this.type = (RecipeType<MachinaRecipe<C>>) (RecipeType<?>) RecipeType.create(Machina.MOD_ID, obj.id().getPath(), MachinaRecipe.class);
        this.category = gui -> new MachinaRecipeCategory<>(gui, obj, block);
        this.x = x;
        this.y = y;
        this.flags = obj.maps().getFlags();
        this.hasInputs = obj.maps().hasInputs();
        this.hasOutputs = obj.maps().hasOutputs();
    }

    public RecipeType<MachinaRecipe<C>> type() {
        return type;
    }

    public MachinaRecipeCategory<C> category(IGuiHelper gui) {
        return category.apply(gui);
    }

    public int flagCount() {
        return Integer.bitCount(flags);
    }

    public int height() {
        int h = 7;
        if (hasInputs) {
            h += 64;
        }
        if (hasOutputs) {
            h += 50;
        }
        h += (flagCount() + 1) / 2 * 14;
        return h + 2;
    }
}
