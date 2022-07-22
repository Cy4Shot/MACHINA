package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.container.AtmosphericSeparatorContainer;
import com.machina.block.container.BatteryContainer;
import com.machina.block.container.ComponentAnalyzerContainer;
import com.machina.block.container.FuelStorageUnitContainer;
import com.machina.block.container.FurnaceGeneratorContainer;
import com.machina.block.container.PressurizedChamberContainer;
import com.machina.block.container.PuzzleContainer;
import com.machina.block.container.ShipConstructContainer;
import com.machina.block.container.ShipLaunchContainer;
import com.machina.block.container.TankContainer;
import com.machina.block.container.TemperatureRegulatorContainer;
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
	public static final RegistryObject<ContainerType<AtmosphericSeparatorContainer>> ATMOSPHERIC_SEPARATOR = register("atmospheric_separator", AtmosphericSeparatorContainer::new);
	public static final RegistryObject<ContainerType<TemperatureRegulatorContainer>> TEMPERATURE_REGULATOR = register("temperature_regulator", TemperatureRegulatorContainer::new);
	public static final RegistryObject<ContainerType<ComponentAnalyzerContainer>> COMPONENT_ANALYZER = register("component_analyzer", ComponentAnalyzerContainer::new);
	public static final RegistryObject<ContainerType<FuelStorageUnitContainer>> FUEL_STORAGE_UNIT = register("fuel_storage_unit", FuelStorageUnitContainer::new);
	public static final RegistryObject<ContainerType<FurnaceGeneratorContainer>> FURNACE_GENERATOR = register("furnace_generator", FurnaceGeneratorContainer::new);
	public static final RegistryObject<ContainerType<PressurizedChamberContainer>> PRESSURIZED_CHAMBER = register("pressurized_chamber", PressurizedChamberContainer::new);
	public static final RegistryObject<ContainerType<BatteryContainer>> BATTERY = register("battery", BatteryContainer::new);
	public static final RegistryObject<ContainerType<TankContainer>> TANK = register("tank", TankContainer::new);
	public static final RegistryObject<ContainerType<PuzzleContainer>> PUZZLE = register("puzzle", PuzzleContainer::new);
	public static final RegistryObject<ContainerType<ScannerContainer>> SCANNER = register("scanner", (w, i, b) -> new ScannerContainer(w, i.player.level.dimension()));
	//@formatter:on
	private static <T extends Container> RegistryObject<ContainerType<T>> register(String name,
			IContainerFactory<T> factory) {
		return CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
	}
}
