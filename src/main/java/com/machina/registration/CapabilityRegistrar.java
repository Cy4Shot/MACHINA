package com.machina.registration;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.energy.EnergyItemWrapper;
import com.machina.api.item.EnergyItem;
import com.machina.api.item.MachinaBucket;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.EntityTypeInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class CapabilityRegistrar {

	@SubscribeEvent
	public static void addReloadListeners(RegisterCapabilitiesEvent event) {
		// Block
		machinaBlock(event, BlockEntityInit.ATMOSPHERIC_SEPARATOR);
		machinaBlock(event, BlockEntityInit.BATTERY);
		machinaBlock(event, BlockEntityInit.CHEMICAL_GENERATOR);
		machinaBlock(event, BlockEntityInit.CREATIVE_BATTERY);
		machinaBlock(event, BlockEntityInit.ELECTRIC_PUMP);
		machinaBlock(event, BlockEntityInit.ELECTRIC_SMELTER);
		machinaBlock(event, BlockEntityInit.FURNACE_GENERATOR);
		machinaBlock(event, BlockEntityInit.ROCKET_ASSEMBLY_STATION);
		machinaBlock(event, BlockEntityInit.ROCKET_REFUELING_STATION);
		machinaBlock(event, BlockEntityInit.ROCKET_PART_BENCH);
		machinaBlock(event, BlockEntityInit.TANK);
		machinaBlock(event, BlockEntityInit.COMPOSTER_VAT);
		machinaBlock(event, BlockEntityInit.COMPRESSOR);
		machinaBlock(event, BlockEntityInit.ELECTROLYZER);
		machinaBlock(event, BlockEntityInit.GRINDER);
		machinaBlock(event, BlockEntityInit.MELTER);
		machinaBlock(event, BlockEntityInit.REACTION_CHAMBER);
		machinaBlock(event, BlockEntityInit.SAWMILL);
		machinaBlock(event, BlockEntityInit.SOLIDIFIER);
		machinaBlock(event, BlockEntityInit.GEOTHERMAL_GENERATOR_CONTROLLER);

		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BlockEntityInit.ENERGY_CABLE.get(),
				(be, side) -> be.createStorage(side));
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockEntityInit.FLUID_PIPE.get(),
				(be, side) -> be.createStorage(side));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityInit.ITEM_CONDUIT.get(),
				(be, side) -> be.createStorage(side));

		// Item
		ItemInit.ITEMS.getEntries().forEach(holder -> {
			if (holder.get() instanceof EnergyItem item) {
				energyItem(event, item);
			}
			if (holder.get() instanceof MachinaBucket item) {
				fluidItem(event, item);
			}
		});

		// Entity
		event.registerEntity(Capabilities.ItemHandler.ENTITY, EntityTypeInit.ROCKET.get(),
				(entity, ctx) -> new InvWrapper(entity));
	}

	public static <T extends MachinaBlockEntity> void machinaBlock(RegisterCapabilitiesEvent event,
			Supplier<BlockEntityType<T>> type) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(),
				(be, side) -> be.getInvCap(side));
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type.get(),
				(be, side) -> be.getEnergyStorage(side));
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type.get(),
				(be, side) -> be.getFluidCap(side));
	}

	private static final void energyItem(final RegisterCapabilitiesEvent event, ItemLike like) {
		event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ctx) -> new EnergyItemWrapper(stack), like);
	}

	private static final void fluidItem(final RegisterCapabilitiesEvent event, ItemLike like) {
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), like);
	}
}
