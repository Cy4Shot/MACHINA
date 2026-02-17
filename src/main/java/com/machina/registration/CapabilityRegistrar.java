package com.machina.registration;

import com.machina.Machina;
import com.machina.api.cap.energy.EnergyItemWrapper;
import com.machina.api.item.EnergyItem;
import com.machina.api.item.MachinaBucket;
import com.machina.registration.init.ItemInit;

import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class CapabilityRegistrar {

    @SubscribeEvent
    public static void addReloadListeners(RegisterCapabilitiesEvent event) {
        // Block
        // TODO

        // Item
        ItemInit.ITEMS.getEntries().forEach(holder -> {
            if (holder.get() instanceof EnergyItem item) {
                energyItem(event, item);
            }
            if (holder.get() instanceof MachinaBucket item) {
                fluidItem(event, item);
            }
        });
        energyItem(event, ItemInit.ADVANCED_CAPACITOR);
        energyItem(event, ItemInit.SUPREME_CAPACITOR);
    }

    private static final void energyItem(final RegisterCapabilitiesEvent event, ItemLike like) {
        event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ctx) -> new EnergyItemWrapper(stack), like);
    }

    private static final void fluidItem(final RegisterCapabilitiesEvent event, ItemLike like) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), like);
    }
}
