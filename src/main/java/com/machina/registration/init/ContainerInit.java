package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.container.AtmosphericSeperatorContainer;
import com.machina.block.container.BatteryContainer;
import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.block.container.PuzzleContainer;
import com.machina.block.container.ShipConstructContainer;
import com.machina.block.container.ShipLaunchContainer;
import com.machina.item.container.ScannerContainer;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<ContainerType<ShipConstructContainer>> SHIP_CONSTRUCT = register("ship_construct", ShipConstructContainer::new);
	public static final RegistryObject<ContainerType<ShipLaunchContainer>> SHIP_LAUNCH = register("ship_launch", ShipLaunchContainer::new);
	public static final RegistryObject<ContainerType<AtmosphericSeperatorContainer>> ATMOSPHERIC_SEPERATOR = register("atmospheric_seperator", AtmosphericSeperatorContainer::new);
	public static final RegistryObject<ContainerType<ComponentAnalyzerContainer>> COMPONENT_ANALYZER = register("component_analyzer", ComponentAnalyzerContainer::new);
	public static final RegistryObject<ContainerType<BatteryContainer>> BATTERY = register("battery", BatteryContainer::new);
	public static final RegistryObject<ContainerType<PuzzleContainer>> PUZZLE = register("puzzle", PuzzleContainer::new);
	public static final RegistryObject<ContainerType<ScannerContainer>> SCANNER = register("scanner", (w, i, b) -> new ScannerContainer(w, i.player.level.dimension()));
	//@formatter:on
	private static <T extends Container> RegistryObject<ContainerType<T>> register(String name,
			IContainerFactory<T> factory) {
		return CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
	}
}
