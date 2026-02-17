package com.machina.api.item;

import com.machina.api.util.StringUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class MachinaBucket extends BucketItem {

    private final String chem;

    public MachinaBucket(Fluid pContent, Properties pProperties, String chemical) {
        super(pContent, pProperties);
        this.chem = chemical;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, TooltipContext ctx, List<Component> tip, @NotNull TooltipFlag flag) {
        tip.add(Component.translatable(StringUtils.chemical(chem))
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9D_AAAAAA))));
        super.appendHoverText(stack, ctx, tip, flag);
    }

    @Override
    public @NotNull ICapabilityProvider initCapabilities(@NotNull ItemStack stack, CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }
}