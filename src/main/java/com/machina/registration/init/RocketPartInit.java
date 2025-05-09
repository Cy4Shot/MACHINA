package com.machina.registration.init;

import org.joml.Vector3d;

import com.machina.api.rocket.RocketPart;
import com.machina.api.rocket.RocketPartType;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RocketPartInit {

	public static final DeferredRegister<RocketPart> ROCKET_PARTS = RegistryInit.ROCKET_PARTS;

	public static final RegistryObject<RocketPart> SIMPLE_CHASSIS = chassis("simple", 0.5);
	public static final RegistryObject<RocketPart> ADVANCED_CHASSIS = chassis("advanced", 0.5);

	public static final RegistryObject<RocketPart> SIMPLE_FUEL_TANK = fuel_tank("simple", 0.5);
	public static final RegistryObject<RocketPart> PRESSURIZED_FUEL_TANK = fuel_tank("pressurized", 0.5);

	public static final RegistryObject<RocketPart> SIMPLE_LIFE_SUPPORT = life_support("simple", 0.5);
	public static final RegistryObject<RocketPart> REINFORCED_LIFE_SUPPORT = life_support("reinforced", 0.5);

	public static final RegistryObject<RocketPart> SIMPLE_SHIELD = shield("simple", 0.5);
	public static final RegistryObject<RocketPart> CONE_SHIELD = shield("cone", 0.5);

	public static final RegistryObject<RocketPart> SIMPLE_THRUSTER = thruster("simple", 0.5);
	public static final RegistryObject<RocketPart> TRI_TALL_THRUSTER = thruster("tri_tall", 0.5);

	private static final RegistryObject<RocketPart> chassis(String name, double offset) {
		return register(name + "_chassis", RocketPartType.CHASSIS, offset);
	}

	private static final RegistryObject<RocketPart> fuel_tank(String name, double offset) {
		return register(name + "_fuel_tank", RocketPartType.FUEL_TANK, offset);
	}

	private static final RegistryObject<RocketPart> life_support(String name, double offset) {
		return register(name + "_life_support", RocketPartType.LIFE_SUPPORT, offset);
	}

	private static final RegistryObject<RocketPart> shield(String name, double offset) {
		return register(name + "_shield", RocketPartType.SHIELD, offset);
	}

	private static final RegistryObject<RocketPart> thruster(String name, double offset) {
		return register(name + "_thruster", RocketPartType.THRUSTER, offset);
	}

	private static final RegistryObject<RocketPart> register(String name, RocketPartType type, double offset) {
		return ROCKET_PARTS.register(name,
				() -> new RocketPart(type, new Vector3d(0, 0, 0), new Vector3d(0, offset, 0)));
	}

}
