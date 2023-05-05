package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;

import com.machina.planet.attribute.AttributeType;
import com.machina.planet.attribute.serializers.AttributeSerializer;
import com.machina.planet.attribute.serializers.ChanceSerializer;
import com.machina.planet.attribute.serializers.ColorListSerializer;
import com.machina.planet.attribute.serializers.DoubleListSerializer;
import com.machina.planet.attribute.serializers.FloatSerializer;
import com.machina.planet.attribute.serializers.IntSerializer;
import com.machina.planet.attribute.serializers.StringSerializer;
import com.machina.registration.registry.AttributeRegistry;
import com.machina.util.Color;
import com.machina.util.MachinaRL;
import com.machina.util.java.ClassHelper;
import com.machina.util.math.MathUtil;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetNameGenerator;
import com.machina.world.gen.PlanetPaletteGenerator;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;

public final class AttributeInit {

	//@formatter:off
	public static final AttributeType<Float> GRAVITY = create("GRAVITY", 1f, 0.3f, 1.7f, "G");
	public static final AttributeType<String> PLANET_NAME = create("PLANET_NAME", "Planet", PlanetNameGenerator::getName);
	public static final AttributeType<Integer> PLANET_ICON = create("PLANET_ICON", 0, 0, 14);
	public static final AttributeType<Float> DISTANCE = create("DISTANCE", 0f, 1f, 20f, "AU");
	public static final AttributeType<Float> ORBITAL_SPEED = create("ORBITAL_SPEED", 1f, 0.3f, 1f);
	public static final AttributeType<Float> PHASE_SHIFT = create("PHASE_SHIFT", 0f, 0f, 360f);
	public static final AttributeType<Float> FOG_DENSITY = create("FOG_DENSITY", 0.5f, 0.0f, 1f, 100, "%");
	public static final AttributeType<Integer> PALETTE = create("PALETTE", 0, 0, PlanetPaletteGenerator.count() - 1);
	public static final AttributeType<Float> ATMOSPHERIC_PRESSURE = create("ATMOSPHERIC_PRESSURE", 1f, 0.1f, 2f, "atm");
	public static final AttributeType<Float> TEMPERATURE = create("TEMPERATURE", 287.05f, 100f, 1000f, "K");
	public static final AttributeType<Float> SURFACE_SCALE = create("SURFACE_SCALE", 2f, 0.5f, 5f);
	public static final AttributeType<Float> SURFACE_DETAIL = create("SURFACE_DETAIL", 2f, 0.5f, 5f);
	public static final AttributeType<Float> SURFACE_ROUGHNESS = create("SURFACE_ROUGHNESS", 0.5f, 0f, 0.7f);
	public static final AttributeType<Float> SURFACE_DISTORTION = create("SURFACE_DISTORTION", 0f, -0.7f, 0.7f);
	public static final AttributeType<Float> SURFACE_MODIFIER = create("SURFACE_MODIFIER", 0.5f, 0f, 1f);
	public static final AttributeType<Integer> SURFACE_SHAPE = create("SURFACE_SHAPE", 1, 0, 1);
	public static final AttributeType<Integer> BASE_BLOCKS = create("BASE_BLOCKS", 0, PlanetBlocksGenerator::getRandomBase);
	public static final AttributeType<Integer> SURF_BLOCKS = create("SURF_BLOCKS", 0, PlanetBlocksGenerator::getRandomSurf);
	public static final AttributeType<Integer> FLUID_BLOCKS = create("FLUID_BLOCKS", 0, PlanetBlocksGenerator::getRandomFluid);
	public static final AttributeType<Integer> TREE_BLOCKS = create("TREE_BLOCKS", 0, PlanetBlocksGenerator::getRandomTree);
	public static final AttributeType<Integer> CAVES_EXIST = create("CAVES_EXIST", 0, 0, 1);
	public static final AttributeType<Float> CAVE_CHANCE = create("CAVE_CHANCE", 0.01f, 0f, 0.02f, 5000, "%");
	public static final AttributeType<Integer> CAVE_LENGTH = create("CAVE_LENGTH", 3, 1, 5);
	public static final AttributeType<Float> CAVE_THICKNESS = create("CAVE_THICKNESS", 0.03f, 0.01f, 0.06f, 250, "m");
	public static final AttributeType<Float> ISLAND_DENSITY = create("ISLAND_DENSITY", 0.5f, 0.3f, 0.7f, 100, "%");
	public static final AttributeType<Integer> TREE_CHANCE = create("TREE_CHANCE", 20, 1, 50);
	public static final AttributeType<Integer> TREE_COUNT = create("TREE_COUNT", 5, 3, 30);
	public static final AttributeType<Integer> TREE_TYPE_SEED = create("TREE_TYPE_SEED", 0, 0, 999999999);
	public static final AttributeType<Float> TRUNK_HEIGHT = create("TRUNK_HEIGHT", 0f, 0f, 1f);
	public static final AttributeType<Float> TRUNK_RADIUS = create("TRUNK_RADIUS", 0f, 0f, 1f);
	public static final AttributeType<Float> LEAVES_HEIGHT = create("LEAVES_HEIGHT", 0f, 0f, 1f);
	public static final AttributeType<Float> LEAVES_RADIUS = create("LEAVES_RADIUS", 0f, 0f, 1f);
	public static final AttributeType<Integer> ORE_COUNT = create("ORE_COUNT", 500, 100, 1000);
	public static final AttributeType<Double[]> ATMOSPHERE = create(new DoubleListSerializer(FluidInit::atmForDim, dirichlet(FluidInit.ATMOSPHERE.size()), FluidInit.ATMOSPHERE.size()));
	//@formatter:on

	public static AttributeType<Float> create(String name, float def, float min, float max) {
		return create(new FloatSerializer(name, def, min, max));
	}

	public static AttributeType<Float> create(String name, float def, float min, float max, String unit) {
		return create(new FloatSerializer(name, def, min, max), unit);
	}

	public static AttributeType<Float> create(String name, float def, float min, float max, int s) {
		return create(new ChanceSerializer(name, def, min, max, s));
	}

	public static AttributeType<Float> create(String name, float def, float min, float max, int s, String unit) {
		return create(new ChanceSerializer(name, def, min, max, s), unit);
	}

	public static AttributeType<Integer> create(String name, int def, Function<Random, Integer> gen) {
		return create(new IntSerializer(name, def, gen));
	}

	public static AttributeType<Integer> create(String name, int def, int min, int max) {
		return create(new IntSerializer(name, def, min, max));
	}

	public static AttributeType<Integer> create(String name, int def, int min, int max, String unit) {
		return create(new IntSerializer(name, def, min, max), unit);
	}

	public static AttributeType<String> create(String name, String def, Function<Random, String> gen) {
		return create(new StringSerializer(name, def, gen));
	}

	public static AttributeType<Color[]> create(String name, Color[] def, Function<Random, Color[]> gen,
			int size) {
		return create(new ColorListSerializer(name, def, gen, size));
	}

	public static <T> AttributeType<T> create(AttributeSerializer<T> ser) {
		return create(ser, "");
	}

	public static <T> AttributeType<T> create(AttributeSerializer<T> ser, String unit) {
		return new AttributeType<T>(unit, ser);
	}

	public static void register(final RegistryEvent.Register<AttributeType<?>> event) {
		ClassHelper.<AttributeType<?>>doWithStatics(AttributeInit.class,
				(name, data) -> register(name.toLowerCase(), data));
	}

	private static <T> AttributeType<T> register(String name, AttributeType<T> attribute) {
		attribute.setRegistryName(new MachinaRL(name));
		AttributeRegistry.REGISTRY.register(attribute);
		return attribute;

	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Map<String, ForgeConfigSpec.ConfigValue>> generateConfig(
			ForgeConfigSpec.Builder builder) {
		Map<String, Map<String, ForgeConfigSpec.ConfigValue>> out = new HashMap<>();
		ClassHelper.<AttributeType<?>>doWithStatics(AttributeInit.class, (name, data) -> {
			builder.push(data.ser.name);
			out.put(data.ser.name, data.ser.generateConf(builder));
			builder.pop();
		});
		return out;
	}

	public static Function<Random, Double[]> dirichlet(int size) {
		return r -> ArrayUtils.toObject(MathUtil.dirichlet(size, r.nextInt()));
	}
}
