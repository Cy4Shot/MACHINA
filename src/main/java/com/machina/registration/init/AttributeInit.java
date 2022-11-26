package com.machina.registration.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;

import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.attribute.serializers.AttributeSerializer;
import com.machina.planet.attribute.serializers.ChanceSerializer;
import com.machina.planet.attribute.serializers.ColorListSerializer;
import com.machina.planet.attribute.serializers.DoubleListSerializer;
import com.machina.planet.attribute.serializers.FloatSerializer;
import com.machina.planet.attribute.serializers.IntSerializer;
import com.machina.planet.attribute.serializers.StringSerializer;
import com.machina.registration.registry.PlanetAttributeRegistry;
import com.machina.util.Color;
import com.machina.util.math.MathUtil;
import com.machina.util.reflection.ClassHelper;
import com.machina.util.text.MachinaRL;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetNameGenerator;
import com.machina.world.gen.PlanetPaletteGenerator;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;

public final class AttributeInit {

	//@formatter:off
	public static final PlanetAttributeType<Float> GRAVITY = create("GRAVITY", 1f, 0.3f, 1.7f, "G");
	public static final PlanetAttributeType<String> PLANET_NAME = create("PLANET_NAME", "Planet", PlanetNameGenerator::getName);
	public static final PlanetAttributeType<Integer> PLANET_ICON = create("PLANET_ICON", 0, 0, 14);
	public static final PlanetAttributeType<Float> DISTANCE = create("DISTANCE", 0f, 1f, 20f, "AU");
	public static final PlanetAttributeType<Float> ORBITAL_SPEED = create("ORBITAL_SPEED", 1f, 0.3f, 1f);
	public static final PlanetAttributeType<Float> PHASE_SHIFT = create("PHASE_SHIFT", 0f, 0f, 360f);
	public static final PlanetAttributeType<Float> FOG_DENSITY = create("FOG_DENSITY", 0.5f, 0.0f, 1f, 100, "%");
	public static final PlanetAttributeType<Color[]> PALETTE = create("PALETTE", PlanetPaletteGenerator.DEFAULT_PALETTE, PlanetPaletteGenerator::genPlanetPalette, 5);
	public static final PlanetAttributeType<Float> ATMOSPHERIC_PRESSURE = create("ATMOSPHERIC_PRESSURE", 1f, 0.1f, 2f, "atm");
	public static final PlanetAttributeType<Float> TEMPERATURE = create("TEMPERATURE", 287.05f, 100f, 1000f, "K");
	public static final PlanetAttributeType<Float> SURFACE_SCALE = create("SURFACE_SCALE", 2f, 0.5f, 5f);
	public static final PlanetAttributeType<Float> SURFACE_DETAIL = create("SURFACE_DETAIL", 2f, 0.5f, 5f);
	public static final PlanetAttributeType<Float> SURFACE_ROUGHNESS = create("SURFACE_ROUGHNESS", 0.5f, 0f, 0.7f);
	public static final PlanetAttributeType<Float> SURFACE_DISTORTION = create("SURFACE_DISTORTION", 0f, -0.7f, 0.7f);
	public static final PlanetAttributeType<Float> SURFACE_MODIFIER = create("SURFACE_MODIFIER", 0.5f, 0f, 1f);
	public static final PlanetAttributeType<Integer> SURFACE_SHAPE = create("SURFACE_SHAPE", 1, 0, 1);
	public static final PlanetAttributeType<Integer> BASE_BLOCKS = create("BASE_BLOCKS", 0, PlanetBlocksGenerator::getRandomBase);
	public static final PlanetAttributeType<Integer> SURF_BLOCKS = create("SURF_BLOCKS", 0, PlanetBlocksGenerator::getRandomSurf);
	public static final PlanetAttributeType<Integer> FLUID_BLOCKS = create("FLUID_BLOCKS", 0, PlanetBlocksGenerator::getRandomFluid);
	public static final PlanetAttributeType<Integer> TREE_BLOCKS = create("TREE_BLOCKS", 0, PlanetBlocksGenerator::getRandomTree);
	public static final PlanetAttributeType<Integer> CAVES_EXIST = create("CAVES_EXIST", 0, 0, 1);
	public static final PlanetAttributeType<Float> CAVE_CHANCE = create("CAVE_CHANCE", 0.01f, 0f, 0.02f, 5000, "%");
	public static final PlanetAttributeType<Integer> CAVE_LENGTH = create("CAVE_LENGTH", 3, 1, 5);
	public static final PlanetAttributeType<Float> CAVE_THICKNESS = create("CAVE_THICKNESS", 0.03f, 0.01f, 0.06f, 250, "m");
	public static final PlanetAttributeType<Float> ISLAND_DENSITY = create("ISLAND_DENSITY", 0.5f, 0.3f, 0.7f, 100, "%");
	public static final PlanetAttributeType<Float> TRUNK_MIN_HEIGHT = create("TRUNK_MIN_HEIGHT", 15f, 5f, 20f, "m");
	public static final PlanetAttributeType<Float> TRUNK_MAX_HEIGHT = create("TRUNK_MAX_HEIGHT", 25f, 20f, 30f, "m");
	public static final PlanetAttributeType<Float> TRUNK_WIDTH_RAND = create("TRUNK_WIDTH_RAND", 10f, 5f, 20f, "m");
	public static final PlanetAttributeType<Float> TRUNK_MIN_RADIUS = create("TRUNK_MIN_RADIUS", 2f, 1f, 2f, "m");
	public static final PlanetAttributeType<Float> TRUNK_MAX_RADIUS = create("TRUNK_MAX_RADIUS", 3f, 2f, 4f, "m");
	public static final PlanetAttributeType<Float> TRUNK_NOISE_SCAL = create("TRUNK_NOISE_SCAL", 0.5f, 0f, 1f);
	public static final PlanetAttributeType<Integer> TRUNK_RNDMNESS_W = create("TRUNK_RNDMNESS_W", 5, 2, 10, "m");
	public static final PlanetAttributeType<Integer> TRUNK_RNDMNESS_H = create("TRUNK_RNDMNESS_H", 8, 2, 15, "m");
	public static final PlanetAttributeType<Float> LEAF_NOISE_SCALE = create("LEAF_NOISE_SCALE", 0.5f, 0f, 1f, 100, "%");
	public static final PlanetAttributeType<Float> LEAF_START_PRCNT = create("LEAF_START_PRCNT", 0.5f, 0.3f, 0.5f, 100, "%");
	public static final PlanetAttributeType<Float> LEAF_SPWN_CHANCE = create("LEAF_SPWN_CHANCE", 0.8f, 0.6f, 1f, 100, "%");
	public static final PlanetAttributeType<Float> LEAF_LOG_DIS_MIN = create("LEAF_LOG_DIS_MIN", 2f, 1f, 4f, "m");
	public static final PlanetAttributeType<Float> LEAF_LOG_DIS_MAX = create("LEAF_LOG_DIS_MAX", 5f, 4f, 8f, "m");
	public static final PlanetAttributeType<Float> LEAF_LOG_DIS_DRP = create("LEAF_LOG_DIS_DRP", 0.7f, 0.5f, 0.9f, 100, "%");
	public static final PlanetAttributeType<Integer> LEAF_LAYER_INTER = create("LEAF_LAYER_INTER", 4, 1, 6, "m");
	public static final PlanetAttributeType<Integer> LEAF_LAYER_COUNT = create("LEAF_LAYER_COUNT", 3, 1, 5, "m");
	public static final PlanetAttributeType<Integer> LEAF_LAYER_WIDTH = create("LEAF_LAYER_WIDTH", 2, 1, 3, "m");
	public static final PlanetAttributeType<Integer> TREE_TYPE = create("TREE_TYPE", 0, 0, 1);
	public static final PlanetAttributeType<Double[]> ATMOSPHERE = create(new DoubleListSerializer(FluidInit::atmForDim, dirichlet(FluidInit.ATMOSPHERE.size()), FluidInit.ATMOSPHERE.size()));
	//@formatter:on
	
	public static PlanetAttributeType<Float> create(String name, float def, float min, float max) {
		return create(new FloatSerializer(name, def, min, max));
	}
	
	public static PlanetAttributeType<Float> create(String name, float def, float min, float max, String unit) {
		return create(new FloatSerializer(name, def, min, max), unit);
	}
	
	public static PlanetAttributeType<Float> create(String name, float def, float min, float max, int s) {
		return create(new ChanceSerializer(name, def, min, max, s));
	}
	
	public static PlanetAttributeType<Float> create(String name, float def, float min, float max, int s, String unit) {
		return create(new ChanceSerializer(name, def, min, max, s), unit);
	}
	
	public static PlanetAttributeType<Integer> create(String name, int def,  Function<Random, Integer> gen) {
		return create(new IntSerializer(name, def, gen));
	}
	
	public static PlanetAttributeType<Integer> create(String name, int def, int min, int max) {
		return create(new IntSerializer(name, def, min, max));
	}
	
	public static PlanetAttributeType<Integer> create(String name, int def, int min, int max, String unit) {
		return create(new IntSerializer(name, def, min, max), unit);
	}
	
	public static PlanetAttributeType<String> create(String name, String def,  Function<Random, String> gen) {
		return create(new StringSerializer(name, def, gen));
	}
	
	public static PlanetAttributeType<Color[]> create(String name, Color[] def,  Function<Random, Color[]> gen, int size) {
		return create(new ColorListSerializer(name, def, gen, size));
	}


	public static <T> PlanetAttributeType<T> create(AttributeSerializer<T> ser) {
		return create(ser, "");
	}

	public static <T> PlanetAttributeType<T> create(AttributeSerializer<T> ser, String unit) {
		return new PlanetAttributeType<T>(unit, ser);
	}

	public static void register(final RegistryEvent.Register<PlanetAttributeType<?>> event) {
		ClassHelper.<PlanetAttributeType<?>>doWithStatics(AttributeInit.class,
				(name, data) -> register(name.toLowerCase(), data));
	}

	private static <T> PlanetAttributeType<T> register(String name, PlanetAttributeType<T> attribute) {
		attribute.setRegistryName(new MachinaRL(name));
		PlanetAttributeRegistry.REGISTRY.register(attribute);
		return attribute;

	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Map<String, ForgeConfigSpec.ConfigValue>> generateConfig(
			ForgeConfigSpec.Builder builder) {
		Map<String, Map<String, ForgeConfigSpec.ConfigValue>> out = new HashMap<>();
		ClassHelper.<PlanetAttributeType<?>>doWithStatics(AttributeInit.class, (name, data) -> {
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
