package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.container.BatteryContainer;
import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.block.container.PuzzleContainer;
import com.machina.block.container.ShipConsoleContainer;
import com.machina.item.container.ScannerContainer;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {
	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, Machina.MOD_ID);

	public static final RegistryObject<ContainerType<ShipConsoleContainer>> SHIP_CONSOLE_CONTAINER_TYPE = register(
			"ship_console", ShipConsoleContainer::new);

	public static final RegistryObject<ContainerType<ComponentAnalyzerContainer>> COMPONENT_ANALYZER_CONTAINER_TYPE = register(
			"component_analyzer", ComponentAnalyzerContainer::new);

	public static final RegistryObject<ContainerType<BatteryContainer>> BATTERY_CONTAINER_TYPE = register("battery",
			BatteryContainer::new);

	public static final RegistryObject<ContainerType<PuzzleContainer>> PUZZLE_CONTAINER_TYPE = register("puzzle",
			PuzzleContainer::new);

	public static final RegistryObject<ContainerType<ScannerContainer>> SCANNER_CONTAINER_TYPE = register(
			"scanner_container", (windowId, inventory, buffer) -> {
				return new ScannerContainer(windowId, inventory.player.level.dimension());
			});

	private static <T extends Container> RegistryObject<ContainerType<T>> register(String name,
			IContainerFactory<T> factory) {
		return CONTAINER_TYPES.register(name, () -> IForgeContainerType.create(factory));
	}
}
