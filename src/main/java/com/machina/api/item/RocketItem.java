package com.machina.api.item;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.bewlr.RocketBEWLR;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.registration.init.RocketPartInit;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RocketItem extends Item {

    public RocketItem(Properties props) {
        super(props.stacksTo(1));
    }

    public static RocketPart<?> get(ItemStack stack, RocketPartType type) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains(type.getNBTName()))
            return RocketPart.fromNBT(nbt.getCompound(type.getNBTName()));
        return null;
    }

    public static void set(ItemStack stack, RocketPartType type, RocketPart<?> part) {
        stack.getOrCreateTag().put(type.getNBTName(), part.toNBT());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag) {

        for (RocketPartType type : RocketPartType.values()) {
            if (get(stack, type) != null) {
                tooltip.add(Component.literal(type.name() + ": ").append(get(stack, type).getName()));
            } else {
                tooltip.add(Component.literal("NO " + type.name()));
            }
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        set(stack, RocketPartType.CHASSIS, RocketPartInit.SIMPLE_CHASSIS.get());
        set(stack, RocketPartType.FUEL_TANK, RocketPartInit.SIMPLE_FUEL_TANK.get());
        set(stack, RocketPartType.LIFE_SUPPORT, RocketPartInit.SIMPLE_LIFE_SUPPORT.get());
        set(stack, RocketPartType.SHIELD, RocketPartInit.SIMPLE_SHIELD.get());
        set(stack, RocketPartType.THRUSTER, RocketPartInit.SIMPLE_THRUSTER.get());
        return stack;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return RocketBEWLR.INSTANCE;
            }
        });
    }
}
