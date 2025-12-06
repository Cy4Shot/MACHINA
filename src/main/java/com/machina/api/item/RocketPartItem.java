package com.machina.api.item;

import com.machina.api.rocket.part.RocketPart;
import com.machina.client.bewlr.RocketPartBEWLR;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RocketPartItem extends Item {

    private final Supplier<RocketPart<?>> part;

    public RocketPartItem(Properties props, Supplier<RocketPart<?>> part) {
        super(props.stacksTo(1));
        this.part = part;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack s, Level l, @NotNull List<Component> def, @NotNull TooltipFlag flag) {
        super.appendHoverText(s, l, def, flag);
    }

    public RocketPart<?> getRocketPart() {
        return part.get();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return RocketPartBEWLR.INSTANCE;
            }
        });
    }

}
