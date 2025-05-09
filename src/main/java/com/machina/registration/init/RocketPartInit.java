package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.joml.Vector3d;

import com.machina.api.rocket.RocketPart;
import com.machina.api.rocket.RocketPartType;
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

import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RocketPartInit {

	public static final Map<ResourceKey<RocketPart<?>>, RegistryObject<RocketPart<?>>> CHASSIS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart<?>>, RegistryObject<RocketPart<?>>> FUEL_TANKS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart<?>>, RegistryObject<RocketPart<?>>> LIFE_SUPPORTS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart<?>>, RegistryObject<RocketPart<?>>> SHIELDS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart<?>>, RegistryObject<RocketPart<?>>> THRUSTERS = new HashMap<>();
	public static final DeferredRegister<RocketPart<?>> ROCKET_PARTS = RegistryInit.ROCKET_PARTS;

	//@formatter:off
	public static final RegistryObject<RocketPart<?>> SIMPLE_CHASSIS = chassis("simple", 0.5, SimpleChassisModel::new);
	public static final RegistryObject<RocketPart<?>> ADVANCED_CHASSIS = chassis("advanced", 0.5, AdvancedChassisModel::new);

	public static final RegistryObject<RocketPart<?>> SIMPLE_FUEL_TANK = fuel_tank("simple", 0.5, SimpleFuelTankModel::new);
	public static final RegistryObject<RocketPart<?>> PRESSURIZED_FUEL_TANK = fuel_tank("pressurized", 0.5, PressurizedTankModel::new);

	public static final RegistryObject<RocketPart<?>> SIMPLE_LIFE_SUPPORT = life_support("simple", 0.5, SimpleLifeSupportModel::new);
	public static final RegistryObject<RocketPart<?>> REINFORCED_LIFE_SUPPORT = life_support("reinforced", 0.5, ReinforcedLifeSupportModel::new);

	public static final RegistryObject<RocketPart<?>> SIMPLE_SHIELD = shield("simple", 0.5, SimpleShieldModel::new);
	public static final RegistryObject<RocketPart<?>> CONE_SHIELD = shield("cone", 0.5, ConeShieldModel::new);

	public static final RegistryObject<RocketPart<?>> SIMPLE_THRUSTER = thruster("simple", 0.5, SimpleThrusterModel::new);
	public static final RegistryObject<RocketPart<?>> TRI_TALL_THRUSTER = thruster("tri_tall", 0.5, TriTallThrusterModel::new);
	//@formatter:on

	private static final RegistryObject<RocketPart<?>> chassis(String name, double offset,
			Supplier<? extends RocketPartModel> model) {
		RegistryObject<RocketPart<?>> ro = register(name + "_chassis", RocketPartType.CHASSIS, offset, model);
		CHASSIS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<RocketPart<?>> fuel_tank(String name, double offset,
			Supplier<? extends RocketPartModel> model) {
		RegistryObject<RocketPart<?>> ro = register(name + "_fuel_tank", RocketPartType.FUEL_TANK, offset, model);
		FUEL_TANKS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<RocketPart<?>> life_support(String name, double offset,
			Supplier<? extends RocketPartModel> model) {
		RegistryObject<RocketPart<?>> ro = register(name + "_life_support", RocketPartType.LIFE_SUPPORT, offset, model);
		LIFE_SUPPORTS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<RocketPart<?>> shield(String name, double offset,
			Supplier<? extends RocketPartModel> model) {
		RegistryObject<RocketPart<?>> ro = register(name + "_shield", RocketPartType.SHIELD, offset, model);
		SHIELDS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<RocketPart<?>> thruster(String name, double offset,
			Supplier<? extends RocketPartModel> model) {
		RegistryObject<RocketPart<?>> ro = register(name + "_thruster", RocketPartType.THRUSTER, offset, model);
		THRUSTERS.put(ro.getKey(), ro);
		return ro;
	}

	private static final RegistryObject<RocketPart<?>> register(String name, RocketPartType type, double offset,
			Supplier<? extends RocketPartModel> model) {
		return ROCKET_PARTS.register(name,
				() -> new RocketPart<>(type, new Vector3d(0, 0, 0), new Vector3d(0, offset, 0), model));
	}

}
