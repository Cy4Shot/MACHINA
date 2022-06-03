package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.container.ComponentAnalyzerContainer;
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

	public static final RegistryObject<ContainerType<ShipConsoleContainer>> SHIP_CONSOLE_CONTAINER_TYPE = CONTAINER_TYPES
			.register("ship_console", () -> IForgeContainerType.create(ShipConsoleContainer::new));

	public static final RegistryObject<ContainerType<ComponentAnalyzerContainer>> COMPONENT_ANALYZER_CONTAINER_TYPE = CONTAINER_TYPES
			.register("component_analyzer", () -> IForgeContainerType.create(ComponentAnalyzerContainer::new));

	public static final RegistryObject<ContainerType<ScannerContainer>> SCANNER_CONTAINER = CONTAINER_TYPES
			.register("scanner_container", () -> createContainerType((windowId, inventory, buffer) -> {
				return new ScannerContainer(windowId, inventory.player.level.dimension());
			}));

	private static <T extends Container> ContainerType<T> createContainerType(IContainerFactory<T> factory) {
		return new ContainerType<T>(factory);
	}
}
