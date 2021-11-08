package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterBlock;
import com.cy4.machina.api.annotation.registries.RegisterBlockItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

@RegistryHolder
public class BlockItemInit {
    @RegisterBlockItem
    public static final BlockItem ROCKET_PLATFORM_BLOCKITEM = new BlockItem(BlockInit.ROCKET_PLATFORM_BLOCK, new Item.Properties());

    @RegisterBlockItem
    public static final BlockItem ANIMATED_BUILDER_MOUNT_BLOCKITEM = new BlockItem(BlockInit.ANIMATED_BUILDER_MOUNT, new Item.Properties());

    @RegisterBlockItem
    public static final BlockItem ANIMATED_BUILDER_BLOCKITEM = new BlockItem(BlockInit.ANIMATED_BUILDER, new Item.Properties());

    @RegisterBlockItem
    public static final BlockItem PAD_SIZE_RELAY_BLOCKITEM = new BlockItem(BlockInit.PAD_SIZE_RELAY, new Item.Properties());

    @RegisterBlockItem
    public static final BlockItem CONSOLE_BLOCKITEM = new BlockItem(BlockInit.CONSOLE, new Item.Properties());

    @RegisterBlockItem
    public static final BlockItem ROCKET_BLOCKITEM = new BlockItem(BlockInit.ROCKET, new Item.Properties());
}
