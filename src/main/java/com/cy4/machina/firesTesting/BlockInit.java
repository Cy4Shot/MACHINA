package com.cy4.machina.firesTesting;

import com.cy4.machina.Machina;
import com.cy4.machina.firesTesting.blocks.*;
import com.cy4.machina.init.ItemInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockInit {
    /**
     * Register Stuff (Such as making BlockItems an easier way than an event class.)
     **/
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Machina.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return BlockInit.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        ItemInit.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties()));
        return ret;
    }



    /**
     * Actual Registry Objects (Since I have so many blocks I might make two separate BlockInits, one for repetitive blocks and another for "outliers")
     **/

    public static final RegistryObject<Block> ROCKET_PLATFORM_BLOCK = register("rocket_platform_block", () ->
            new Block(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE)));


    public static final RegistryObject<Block> ANIMATED_BUILDER_MOUNT = register("animated_builder_mount", () ->
            new AnimatedBuilderMount(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE)));

    public static final RegistryObject<Block> ANIMATED_BUILDER = register("animated_builder", () ->
            new AnimatedBuilder(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE)));

    public static final RegistryObject<Block> PAD_SIZE_RELAY = register("pad_size_relay", () ->
            new PadSizeRelay(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE)));

    public static final RegistryObject<Block> CONSOLE = register("pad_console", () ->
            new ConsoleBlock(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE)));



    public static final RegistryObject<Block> ROCKET = register("rocket", () ->
            new RocketBlock(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE)));
}
