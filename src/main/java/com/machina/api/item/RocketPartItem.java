package com.machina.api.item;

import com.machina.api.rocket.part.RocketPart;
import com.machina.client.bewlr.RocketPartBEWLR;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;


import java.util.function.Consumer;
import java.util.function.Supplier;

public class RocketPartItem extends Item {

    private final Supplier<RocketPart<?>> part;

    public RocketPartItem(Properties props, Supplier<RocketPart<?>> part) {
        super(props.stacksTo(1));
        this.part = part;
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
