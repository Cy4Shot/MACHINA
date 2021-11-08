package com.cy4.machina.firesTesting;

import com.cy4.machina.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class MachinaItemGroup extends ItemGroup {

    public MachinaItemGroup(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() { return BlockInit.ROCKET_PLATFORM_BLOCK.get().asItem().getDefaultInstance(); }

    //This is used to apply a specific order to your creative tab based on what you enter first.
    @Override
    public void fillItemList(NonNullList<ItemStack> items)
    {
        //items.add(ItemInit.TEST_ITEM.getDefaultInstance());

        items.add(BlockInit.ROCKET_PLATFORM_BLOCK.get().asItem().getDefaultInstance());

        items.add(BlockInit.ANIMATED_BUILDER_MOUNT.get().asItem().getDefaultInstance());
        items.add(BlockInit.ANIMATED_BUILDER.get().asItem().getDefaultInstance());
        items.add(BlockInit.PAD_SIZE_RELAY.get().asItem().getDefaultInstance());
        items.add(BlockInit.CONSOLE.get().asItem().getDefaultInstance());

        items.add(BlockInit.ROCKET.get().asItem().getDefaultInstance());
    }
}