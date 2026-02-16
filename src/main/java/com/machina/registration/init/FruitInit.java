package com.machina.registration.init;

import com.machina.api.item.FruitItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;

public class FruitInit {

    public record Fruit(DeferredHolder<Block, Block> block, DeferredHolder<Item, FruitItem> item) {
    }

    public static final List<Fruit> FRUITS = new ArrayList<>();

    public static void register() {
        // This method is left intentionally blank.
    }

    public static final Fruit TAMA_SPORE = register("tama_spore", 4, 0f, 0);
    public static final Fruit STRAPPLE = register("strapple", 4, 0.3f, 0);
    public static final Fruit ARGO_BERRY = register("argo_berry", 2, 0.5f, 0);
    public static final Fruit Y2_NANA = register("y2_nana", 2, 0.15f, 0);
    public static final Fruit AVA_FRUIT = register("ava_fruit", 3, 0.3f, 0);
    public static final Fruit GRELP_BERRY = register("grelp_berry", 0, 0.1f, 3);
    public static final Fruit SPARR_BALL = register("sparr_ball", 1, 0.6f, 0);
    public static final Fruit ERBI_POD = register("erbi_pod", 6, 0.1f, 0);

    private static FoodProperties food(int nut, float sat, int flags) {
        FoodProperties.Builder b = (new FoodProperties.Builder()).nutrition(nut).saturationModifier(sat);
        if ((flags & 1) == 1) {
            b = b.fast();
        }
        return b.build();
    }

    private static Fruit register(String name, int nut, float sat, int flags) {
        DeferredHolder<Block, Block> rb = BlockInit.BLOCKS.register(name,
                () -> new Block(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.CROP)));
        DeferredHolder<Item, FruitItem> ri = ItemInit.ITEMS.register(name,
                () -> new FruitItem(rb.get(), new Item.Properties().food(food(nut, sat, flags))));

        Fruit f = new Fruit(rb, ri);
        FRUITS.add(f);
        return f;
    }
}
