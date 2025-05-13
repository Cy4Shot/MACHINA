package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.MachinaRL;
import com.machina.client.rocket.model.RocketPartModel;
import com.machina.client.rocket.model.chassis.AdvancedChassisModel;
import com.machina.client.rocket.model.chassis.SimpleChassisModel;
import com.machina.client.rocket.model.fuel_tank.PressurizedTankModel;
import com.machina.client.rocket.model.fuel_tank.SimpleFuelTankModel;
import com.machina.client.rocket.model.life_support.ReinforcedLifeSupportModel;
import com.machina.client.rocket.model.life_support.SimpleLifeSupportModel;
import com.machina.client.rocket.model.shield.ConeShieldModel;
import com.machina.client.rocket.model.shield.SimpleShieldModel;
import com.machina.client.rocket.model.thrusters.SimpleThrusterModel;
import com.machina.client.rocket.model.thrusters.TriTallThrusterModel;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RocketPartInit {

	public static final Map<ResourceKey<ChassisPart<?>>, RegistryObject<ChassisPart<?>>> CHASSIS = new HashMap<>();
	public static final Map<ResourceKey<FuelTankPart<?>>, RegistryObject<FuelTankPart<?>>> FUEL_TANKS = new HashMap<>();
	public static final Map<ResourceKey<LifeSupportPart<?>>, RegistryObject<LifeSupportPart<?>>> LIFE_SUPPORTS = new HashMap<>();
	public static final Map<ResourceKey<ShieldPart<?>>, RegistryObject<ShieldPart<?>>> SHIELDS = new HashMap<>();
	public static final Map<ResourceKey<ThrusterPart<?>>, RegistryObject<ThrusterPart<?>>> THRUSTERS = new HashMap<>();
	public static final DeferredRegister<RocketPart<?>> ROCKET_PARTS = RegistryInit.ROCKET_PARTS;

	//@formatter:off
	public static final RegistryObject<ChassisPart<?>> SIMPLE_CHASSIS = chassis("simple", 1.5f, 0f, 10, FluidObject.WATER, 1f, SimpleChassisModel::new);
	public static final RegistryObject<ChassisPart<?>> ADVANCED_CHASSIS = chassis("advanced", 1f, 0f, 15, FluidInit.LEAD_BISMUTH_EUTECTIC, 0.5f, AdvancedChassisModel::new);

	public static final RegistryObject<FuelTankPart<?>> SIMPLE_FUEL_TANK = fuel_tank("simple", 3f, 0f, 40, 1000, 5000, SimpleFuelTankModel::new);
	public static final RegistryObject<FuelTankPart<?>> PRESSURIZED_FUEL_TANK = fuel_tank("pressurized", 1f, 0f, 60, 2000, 10000, PressurizedTankModel::new);

	public static final RegistryObject<LifeSupportPart<?>> SIMPLE_LIFE_SUPPORT = life_support("simple", 3f, 0f, 10, 0, SimpleLifeSupportModel::new);
	public static final RegistryObject<LifeSupportPart<?>> REINFORCED_LIFE_SUPPORT = life_support("reinforced", 2f, 0f, 20, 27, ReinforcedLifeSupportModel::new);

	public static final RegistryObject<ShieldPart<?>> SIMPLE_SHIELD = shield("simple", 0.6875f, 0f, 20, 4000, SimpleShieldModel::new);
	public static final RegistryObject<ShieldPart<?>> CONE_SHIELD = shield("cone", 1f, 0f, 30, 9000, ConeShieldModel::new);

	public static final RegistryObject<ThrusterPart<?>> SIMPLE_THRUSTER = thruster("simple", 0.75f, 0f, 10, FluidInit.AMMONIA, 1f, SimpleThrusterModel::new);
	public static final RegistryObject<ThrusterPart<?>> TRI_TALL_THRUSTER = thruster("tri_tall", 1.625f, -0.875f, 15, FluidInit.AMMONIA, 2f, TriTallThrusterModel::new);
	//@formatter:on

	private static final RegistryObject<ChassisPart<?>> chassis(String name, float height, float offset, float weight,
			FluidObject coolant, float coolantEfficiency, Supplier<? extends RocketPartModel> model) {
		RegistryObject<ChassisPart<?>> ro = register(name + "_chassis",
				(t) -> new ChassisPart<>(t, height, model, weight, offset, coolant, coolantEfficiency));
		CHASSIS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<FuelTankPart<?>> fuel_tank(String name, float height, float offset,
			float weight, int fuelStorage, int coolantStorage, Supplier<? extends RocketPartModel> model) {
		RegistryObject<FuelTankPart<?>> ro = register(name + "_fuel_tank",
				(t) -> new FuelTankPart<>(t, height, model, weight, offset, fuelStorage, coolantStorage));
		FUEL_TANKS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<LifeSupportPart<?>> life_support(String name, float height, float offset,
			float weight, int slots, Supplier<? extends RocketPartModel> model) {
		RegistryObject<LifeSupportPart<?>> ro = register(name + "_life_support",
				(t) -> new LifeSupportPart<>(t, height, model, weight, offset, slots));
		LIFE_SUPPORTS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<ShieldPart<?>> shield(String name, float height, float offset, float weight,
			float maxAtmPressure, Supplier<? extends RocketPartModel> model) {
		RegistryObject<ShieldPart<?>> ro = register(name + "_shield",
				(t) -> new ShieldPart<>(t, height, model, weight, offset, maxAtmPressure));
		SHIELDS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<ThrusterPart<?>> thruster(String name, float height, float offset, float weight,
			FluidObject fuel, float fuelEfficiency, Supplier<? extends RocketPartModel> model) {
		RegistryObject<ThrusterPart<?>> ro = register(name + "_thruster",
				(t) -> new ThrusterPart<>(t, height, model, weight, offset, fuel, fuelEfficiency));
		THRUSTERS.put(ro.getKey(), ro);
		return ro;
	}

	private static final <T extends RocketPart<?>> RegistryObject<T> register(String name,
			Function<ResourceLocation, T> part) {
		return ROCKET_PARTS.register(name, () -> part.apply(new MachinaRL(name)));
	}

}
