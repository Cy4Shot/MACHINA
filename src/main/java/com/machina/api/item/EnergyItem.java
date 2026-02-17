package com.machina.api.item;

import com.machina.api.cap.energy.EnergyItemWrapper;
import com.machina.api.client.screen.MUI;
import com.machina.api.util.StringUtils;
import com.machina.registration.init.DataComponentsInit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class EnergyItem extends Item {

    private static final float BAR = (float) Item.MAX_BAR_WIDTH;

    public EnergyItem(Properties props) {
        super(props);
    }

    public abstract int getMaxEnergy();

    private float getEnergyProp(ItemStack stack) {
        float max = (float) getMaxEnergy();
        float stored = (float) stack.get(DataComponentsInit.ENERGY);
        return stored / max;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnergyItemWrapper(stack);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(DataComponentsInit.ENERGY, 0);
        return stack;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, List<Component> tooltip,
                                @NotNull TooltipFlag flag) {
        tooltip.add(Component
                .literal(StringUtils.formatPower(stack.get(DataComponentsInit.ENERGY)) + " / " + StringUtils.formatPower(getMaxEnergy()))
                .setStyle(Style.EMPTY.withColor(MUI.CYAN)));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.round(BAR - BAR * (1f - getEnergyProp(stack)));
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return MUI.CYAN;
    }
}
