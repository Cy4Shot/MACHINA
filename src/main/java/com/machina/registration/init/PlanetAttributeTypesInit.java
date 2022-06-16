package com.machina.registration.init;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.registry.PlanetAttributeRegistry;
import com.machina.util.MachinaRL;
import com.machina.util.color.Color;
import com.machina.util.nbt.BaseNBTList;
import com.machina.util.reflection.ClassHelper;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetNameGenerator;
import com.machina.world.gen.PlanetPaletteGenerator;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.event.RegistryEvent;

public final class PlanetAttributeTypesInit {

	//@formatter:off
	public static final PlanetAttributeType<Float> DISTANCE = new PlanetAttributeType<>("AU", FloatNBT::valueOf, floatDeserializer(1.0f), random(0.5f, 10f));
	public static final PlanetAttributeType<Float> GRAVITY = new PlanetAttributeType<>("N", FloatNBT::valueOf, floatDeserializer(9.80655f), random(1.0f, 18.0f));
	public static final PlanetAttributeType<String> PLANET_NAME = new PlanetAttributeType<>("", StringNBT::valueOf, stringDeserializer("Planet"), PlanetNameGenerator::getName);
	public static final PlanetAttributeType<Float> FOG_DENSITY = new PlanetAttributeType<>("", FloatNBT::valueOf, floatDeserializer(0.5f), random(0f, 1.0f));
	public static final PlanetAttributeType<Color[]> PALETTE = new PlanetAttributeType<>("", colorListSerializer(), colorListDeserializer(PlanetPaletteGenerator.DEFAULT_PALETTE, new Color(0), 5), PlanetPaletteGenerator::genPlanetPalette);
	public static final PlanetAttributeType<Float> ATMOSPHERIC_PRESSURE = new PlanetAttributeType<>("atm", FloatNBT::valueOf, floatDeserializer(1.0f), random(0.1f, 2.0f));
	public static final PlanetAttributeType<Float> TEMPERATURE = new PlanetAttributeType<>("K", FloatNBT::valueOf, floatDeserializer(350.0f), random(100f, 1000f));
	public static final PlanetAttributeType<Integer> BASE_BLOCKS = new PlanetAttributeType<>("", IntNBT::valueOf, intDeserializer(0), PlanetBlocksGenerator::getRandomBase);
	public static final PlanetAttributeType<Integer> SURF_BLOCKS = new PlanetAttributeType<>("", IntNBT::valueOf, intDeserializer(0), PlanetBlocksGenerator::getRandomSurf);
	public static final PlanetAttributeType<Integer> FLUID_BLOCKS = new PlanetAttributeType<>("", IntNBT::valueOf, intDeserializer(0), PlanetBlocksGenerator::getRandomFluid);
	public static final PlanetAttributeType<Integer> CAVES_EXIST = new PlanetAttributeType<>("", IntNBT::valueOf, intDeserializer(0), random(0, 1));
	public static final PlanetAttributeType<Float> CAVE_CHANCE = new PlanetAttributeType<>("", FloatNBT::valueOf, floatDeserializer(0.01f), random(0f, 0.02f));
	public static final PlanetAttributeType<Integer> CAVE_LENGTH = new PlanetAttributeType<>("", IntNBT::valueOf, intDeserializer(3), random(1, 5));
	public static final PlanetAttributeType<Float> CAVE_THICKNESS = new PlanetAttributeType<>("", FloatNBT::valueOf, floatDeserializer(0.03f), random(0.01f, 0.06f, 1.7f));
	public static final PlanetAttributeType<Float> ISLAND_DENSITY = new PlanetAttributeType<>("", FloatNBT::valueOf, floatDeserializer(0.5f), random(0.3f, 0.7f));
	//@formatter:on

	public static void register(final RegistryEvent.Register<PlanetAttributeType<?>> event) {
//		register("distance", DISTANCE);
//		register("gravity", GRAVITY);
//		register("planet_name", PLANET_NAME);
//		register("fog_density", FOG_DENSITY);
//		register("palette", PALETTE);
//		register("atmospheric_pressure", ATMOSPHERIC_PRESSURE);
//		register("temperature", TEMPERATURE);
//		register("base_blocks", BASE_BLOCKS);
//		register("surf_blocks", SURF_BLOCKS);
//		register("fluid_blocks", FLUID_BLOCKS);
//		register("caves_exist", CAVES_EXIST);
//		register("cave_chance", CAVE_CHANCE);
//		register("cave_length", CAVE_LENGTH);
//		register("cave_thickness", CAVE_THICKNESS);
//		register("island_density", ISLAND_DENSITY);
		
		ClassHelper.<PlanetAttributeType<?>>doWithStatics(PlanetAttributeTypesInit.class,
				(name, data) -> register(name.toLowerCase(), data));
	}

	private static <T> PlanetAttributeType<T> register(String name, PlanetAttributeType<T> attribute) {
		attribute.setRegistryName(new MachinaRL(name));
		PlanetAttributeRegistry.REGISTRY.register(attribute);
		return attribute;

	}

	// Deserializers
	public static Function<INBT, Color[]> colorListDeserializer(Color[] defaultVal, Color defaultCol, int size) {
		return nbt -> {
			if (nbt instanceof CompoundNBT) {
				BaseNBTList<Color, INBT> colors = new BaseNBTList<>(colorSerializer(), colorDeserializer(defaultCol));
				colors.deserializeNBT((CompoundNBT) nbt);
				return colors.toArray(new Color[size]);
			}
			return defaultVal;
		};
	}

	public static Function<INBT, Float> floatDeserializer(float defaultVal) {
		return nbt -> {
			if (nbt instanceof FloatNBT) {
				return ((FloatNBT) nbt).getAsFloat();
			}
			return defaultVal;
		};
	}

	public static Function<INBT, Integer> intDeserializer(int defaultVal) {
		return nbt -> {
			if (nbt instanceof IntNBT) {
				return ((IntNBT) nbt).getAsInt();
			}
			return defaultVal;
		};
	}

	public static Function<INBT, String> stringDeserializer(String defaultVal) {
		return nbt -> {
			if (nbt instanceof StringNBT) {
				return ((StringNBT) nbt).getAsString();
			}
			return defaultVal;
		};
	}

	public static Function<INBT, Color> colorDeserializer(Color defaultVal) {
		return nbt -> {
			if (nbt instanceof IntNBT) {
				return new Color(((IntNBT) nbt).getAsInt());
			}
			return defaultVal;
		};
	}

	// Serializers
	public static Function<Color, INBT> colorSerializer() {
		return color -> IntNBT.valueOf(color.getRGB());
	}

	public static Function<Color[], INBT> colorListSerializer() {
		return colors -> {
			BaseNBTList<Color, INBT> c = new BaseNBTList<>(colorSerializer(), colorDeserializer(new Color(0)));
			c.addAll(Arrays.asList(colors));
			return c.serializeNBT();
		};
	}

	// Random
	public static Function<Random, Float> random(float min, float max) {
		return r -> min + r.nextFloat() * (max - min);
	}

	public static Function<Random, Float> random(float min, float max, float exp) {
		return r -> min + (float) Math.pow(r.nextFloat(), exp) * (max - min);
	}

	public static Function<Random, Integer> random(int min, int max) {
		return r -> r.nextInt((max - min) + 1) + min;
	}
}
