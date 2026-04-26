package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.machina.Machina;
import com.machina.api.item.RocketPartItem;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.MachinaRL;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RocketPartInit {

	public static final Map<ResourceKey<RocketPart>, ResourceLocation> ITEM_MAP = new HashMap<>();
	public static final Map<ResourceKey<RocketPart>, DeferredHolder<RocketPart, ChassisPart>> CHASSIS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart>, DeferredHolder<RocketPart, FuelTankPart>> FUEL_TANKS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart>, DeferredHolder<RocketPart, LifeSupportPart>> LIFE_SUPPORTS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart>, DeferredHolder<RocketPart, ShieldPart>> SHIELDS = new HashMap<>();
	public static final Map<ResourceKey<RocketPart>, DeferredHolder<RocketPart, ThrusterPart>> THRUSTERS = new HashMap<>();
	public static final DeferredRegister<RocketPart> ROCKET_PARTS = DeferredRegister.create(RegistryInit.ROCKET_PART,
			Machina.MOD_ID);

	//@formatter:off
	public static final DeferredHolder<RocketPart, ChassisPart> SIMPLE_CHASSIS = chassis("simple", 1f, 0f, 1f, 100, FluidObject.WATER, 1f);
	public static final DeferredHolder<RocketPart, ChassisPart> ADVANCED_CHASSIS = chassis("advanced", 1f, 0f, 1f, 150, FluidInit.LEAD_BISMUTH_EUTECTIC, 0.5f);

	public static final DeferredHolder<RocketPart, FuelTankPart> SIMPLE_FUEL_TANK = fuel_tank("simple", 1f, 0f, 0.9f, 400, 16_000, 36_000);
	public static final DeferredHolder<RocketPart, FuelTankPart> PRESSURIZED_FUEL_TANK = fuel_tank("pressurized", 1f, 0f, 1f, 600, 100_000, 100_000);

	public static final DeferredHolder<RocketPart, LifeSupportPart> SIMPLE_LIFE_SUPPORT = life_support("simple", 2f, 0f, 1f, 100, 0);
	public static final DeferredHolder<RocketPart, LifeSupportPart> REINFORCED_LIFE_SUPPORT = life_support("reinforced", 2f, 0f, 1f, 200, 27);

	public static final DeferredHolder<RocketPart, ShieldPart> SIMPLE_SHIELD = shield("cone", 0.75f, 0f, 0.9f, 30, 32_000_000);
	public static final DeferredHolder<RocketPart, ShieldPart> BREAKER_SHIELD = shield("breaker", 0.6875f, 0f, 1f, 200, 10_000_000);

	public static final DeferredHolder<RocketPart, ThrusterPart> SIMPLE_THRUSTER = thruster("simple", 0.5625f, 0f, .5f, 100, FluidInit.AMMONIA, 1f);
	public static final DeferredHolder<RocketPart, ThrusterPart> TRI_TALL_THRUSTER = thruster("tri_tall", 1.625f, -0.875f, 1f, 150, FluidInit.AMMONIA, 2f);
	//@formatter:on

	private static DeferredHolder<RocketPart, ChassisPart> chassis(String name, float height, float offset,
			float guiScale, float mass, FluidObject coolant, float coolantEfficiency) {
		DeferredHolder<RocketPart, ChassisPart> ro = register(name + "_chassis",
				(t) -> new ChassisPart(t, height, mass, offset, guiScale, coolant, coolantEfficiency));
		CHASSIS.put(ro.getKey(), ro);
		return ro;
	}

	private static DeferredHolder<RocketPart, FuelTankPart> fuel_tank(String name, float height, float offset,
			float guiScale, float mass, int fuelStorage, int coolantStorage) {
		DeferredHolder<RocketPart, FuelTankPart> ro = register(name + "_fuel_tank",
				(t) -> new FuelTankPart(t, height, mass, offset, guiScale, fuelStorage, coolantStorage));
		FUEL_TANKS.put(ro.getKey(), ro);
		return ro;
	}

	private static DeferredHolder<RocketPart, LifeSupportPart> life_support(String name, float height, float offset,
			float guiScale, float mass, int slots) {
		DeferredHolder<RocketPart, LifeSupportPart> ro = register(name + "_life_support",
				(t) -> new LifeSupportPart(t, height, mass, offset, guiScale, slots));
		LIFE_SUPPORTS.put(ro.getKey(), ro);
		return ro;
	}

	private static DeferredHolder<RocketPart, ShieldPart> shield(String name, float height, float offset,
			float guiScale, float mass, float maxAtmPressure) {
		DeferredHolder<RocketPart, ShieldPart> ro = register(name + "_shield",
				(t) -> new ShieldPart(t, height, mass, offset, guiScale, maxAtmPressure));
		SHIELDS.put(ro.getKey(), ro);
		return ro;
	}

	private static DeferredHolder<RocketPart, ThrusterPart> thruster(String name, float height, float offset,
			float guiScale, float mass, FluidObject fuel, float fuelEfficiency) {
		DeferredHolder<RocketPart, ThrusterPart> ro = register(name + "_thruster",
				(t) -> new ThrusterPart(t, height, mass, offset, guiScale, fuel, fuelEfficiency));
		THRUSTERS.put(ro.getKey(), ro);
		return ro;
	}

	private static <T extends RocketPart> DeferredHolder<RocketPart, T> register(String name,
			Function<ResourceLocation, T> part) {
		DeferredHolder<RocketPart, T> ro = ROCKET_PARTS.register(name, () -> part.apply(MachinaRL.create(name)));
		DeferredHolder<Item, RocketPartItem> item = ItemInit.ITEMS.register("rocket_part_" + name,
				() -> new RocketPartItem(new Item.Properties(), ro::get));
		ITEM_MAP.put(ro.getKey(), item.getId());
		return ro;
	}

}
