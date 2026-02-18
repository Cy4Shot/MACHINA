package com.machina.api.rocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.reflect.MachinaCodecs;
import com.machina.api.util.reflect.MachinaStreamCodecs;
import com.machina.registration.init.RegistryInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public record RocketProps(boolean empty, float mass, Fluid fuelType, int fuelStorage, float fuelEfficiency,
		Fluid coolantType, int coolantStorage, float coolantEfficiency, int slots, float maxPressure,
		List<RocketPart<?>> parts, AABB boundingBox) {

	public RocketProps(boolean empty, float mass, FluidStack fuelStack, float fuelEfficiency, FluidStack coolantStack,
			float coolantEfficiency, int slots, float maxPressure, List<RocketPart<?>> parts, AABB boundingBox) {
		this(empty, mass, fuelStack.getFluid(), fuelStack.getAmount(), fuelEfficiency, coolantStack.getFluid(),
				coolantStack.getAmount(), coolantEfficiency, slots, maxPressure, parts, boundingBox);
	}

	//@formatter:off
    public static final StreamCodec<RegistryFriendlyByteBuf, RocketProps> STREAM_CODEC = MachinaStreamCodecs.composite(
            ByteBufCodecs.BOOL, RocketProps::empty,
            ByteBufCodecs.FLOAT, RocketProps::mass,
            FluidStack.STREAM_CODEC, RocketProps::fuelStack,
            ByteBufCodecs.FLOAT, RocketProps::fuelEfficiency,
            FluidStack.STREAM_CODEC, RocketProps::coolantStack,
            ByteBufCodecs.FLOAT, RocketProps::coolantEfficiency,
            ByteBufCodecs.VAR_INT, RocketProps::slots,
            ByteBufCodecs.FLOAT, RocketProps::maxPressure,
            ByteBufCodecs.registry(RegistryInit.ROCKET_PART_REGISTRY.key()).apply(ByteBufCodecs.list(5)), RocketProps::parts,
            MachinaStreamCodecs.AABB_CODEC, RocketProps::boundingBox,
            RocketProps::new);
    
    public static final Codec<RocketProps> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Codec.BOOL.fieldOf("empty").forGetter(RocketProps::empty),
                Codec.FLOAT.fieldOf("mass").forGetter(RocketProps::mass),
                FluidStack.CODEC.fieldOf("fuelStack").forGetter(RocketProps::fuelStack),
                Codec.FLOAT.fieldOf("fuelEfficiency").forGetter(RocketProps::fuelEfficiency),
                FluidStack.CODEC.fieldOf("coolantStack").forGetter(RocketProps::coolantStack),
                Codec.FLOAT.fieldOf("coolantEfficiency").forGetter(RocketProps::coolantEfficiency),
                Codec.INT.fieldOf("slots").forGetter(RocketProps::slots),
                Codec.FLOAT.fieldOf("maxPressure").forGetter(RocketProps::maxPressure),
                RocketPart.CODEC.listOf(5, 5).fieldOf("parts").forGetter(RocketProps::parts),
                MachinaCodecs.AABB.fieldOf("boundingBox").forGetter(RocketProps::boundingBox)
        ).apply(instance, RocketProps::new));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<RocketPartType, RocketPart<?>>> PARTMAP_STREAM_CODEC = ByteBufCodecs.map(
            HashMap::new,
            MachinaStreamCodecs.enumCodec(RocketPartType.class),
            RocketPart.STREAM_CODEC,
            5);
    
    public static final Codec<Map<RocketPartType, RocketPart<?>>> PARTMAP_CODEC = Codec.unboundedMap(
            MachinaCodecs.enumCodec(RocketPartType.class), 
            RocketPart.CODEC);
    //@formatter:on

	public static final EntityDataSerializer<RocketProps> SERIALIZER = new EntityDataSerializer.ForValueType<RocketProps>() {
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RocketProps> codec() {
			return STREAM_CODEC;
		}
	};

	public static final RocketProps NULL = new RocketProps(true, 0, null, 0, 0, null, 0, 0, 0, 0, List.of(),
			new AABB(0, 0, 0, 1, 1, 1));

	private static final AABB calculateAABB(List<RocketPart<?>> parts) {
		float maxY = 0;
		for (RocketPart<?> part : parts) {
			maxY += part.getModelHeight();
			maxY += part.getModelOffset();
		}
		return new AABB(-0.5, 0, -0.5, 0.5, maxY, 0.5);
	}

	private static final String PROPERTY_EMPTY = "nully";
	private static final String PROPERTY_MASS = "mass";
	private static final String PROPERTY_FUEL_TYPE = "fuel_type";
	private static final String PROPERTY_FUEL_STORAGE = "fuel_storage";
	private static final String PROPERTY_FUEL_EFFICIENCY = "fuel_efficiency";
	private static final String PROPERTY_COOLANT_TYPE = "coolant_type";
	private static final String PROPERTY_COOLANT_STORAGE = "coolant_storage";
	private static final String PROPERTY_COOLANT_EFFICIENCY = "coolant_efficiency";
	private static final String PROPERTY_SLOTS = "slots";
	private static final String PROPERTY_MAX_PRESSURE = "max_pressure";
	private static final String PROPERTY_PARTS = "parts";

	public static RocketProps fromParts(ThrusterPart<?> thruster, FuelTankPart<?> fuelTank, ChassisPart<?> chassis,
			LifeSupportPart<?> lifeSupport, ShieldPart<?> shield) {
		float mass = thruster.getMass() + fuelTank.getMass() + chassis.getMass() + lifeSupport.getMass()
				+ shield.getMass();
		List<RocketPart<?>> parts = List.of(thruster, fuelTank, chassis, lifeSupport, shield);
		return new RocketProps(false, mass, thruster.getFuel().fluid(), fuelTank.getFuelStorage(),
				thruster.getFuelEfficiency(), chassis.getCoolant().fluid(), fuelTank.getCoolantStorage(),
				chassis.getCoolantEfficiency(), lifeSupport.getSlots(), shield.getMaxAtmPressure(), parts,
				calculateAABB(parts));
	}

	public static RocketProps fromNBT(CompoundTag tag) {
		if (tag.getBoolean(PROPERTY_EMPTY)) {
			return NULL;
		}

		Fluid fuel = BuiltInRegistries.FLUID.get(ResourceLocation.parse(tag.getString(PROPERTY_FUEL_TYPE)));
		Fluid coolant = BuiltInRegistries.FLUID.get(ResourceLocation.parse(tag.getString(PROPERTY_COOLANT_TYPE)));
		List<RocketPart<?>> parts = new ArrayList<RocketPart<?>>();
		ListTag rocket_parts = tag.getList(PROPERTY_PARTS, Tag.TAG_COMPOUND);
		for (int i = 0; i < rocket_parts.size(); i++) {
			parts.add(RocketPart.fromNBT(rocket_parts.getCompound(i)));
		}

		return new RocketProps(false, tag.getFloat(PROPERTY_MASS), fuel, tag.getInt(PROPERTY_FUEL_STORAGE),
				tag.getFloat(PROPERTY_FUEL_EFFICIENCY), coolant, tag.getInt(PROPERTY_COOLANT_STORAGE),
				tag.getFloat(PROPERTY_COOLANT_EFFICIENCY), tag.getInt(PROPERTY_SLOTS),
				tag.getFloat(PROPERTY_MAX_PRESSURE), parts, calculateAABB(parts));
	}

	public CompoundTag toNBT() {
		CompoundTag props = new CompoundTag();
		props.putBoolean(PROPERTY_EMPTY, empty);
		if (empty) {
			return props;
		}

		String fuel = Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(fuelType)).toString();
		String coolant = Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(coolantType)).toString();
		props.putFloat(PROPERTY_MASS, mass);
		props.putString(PROPERTY_FUEL_TYPE, fuel);
		props.putInt(PROPERTY_FUEL_STORAGE, fuelStorage);
		props.putFloat(PROPERTY_FUEL_EFFICIENCY, fuelEfficiency);
		props.putString(PROPERTY_COOLANT_TYPE, coolant);
		props.putInt(PROPERTY_COOLANT_STORAGE, coolantStorage);
		props.putFloat(PROPERTY_COOLANT_EFFICIENCY, coolantEfficiency);
		props.putInt(PROPERTY_SLOTS, slots);
		props.putFloat(PROPERTY_MAX_PRESSURE, maxPressure);

		ListTag rocket_parts = new ListTag();
		for (RocketPart<?> part : this.parts) {
			rocket_parts.add(part.toNBT());
		}
		props.put(PROPERTY_PARTS, rocket_parts);

		return props;
	}

	public FluidStack fuelStack() {
		return new FluidStack(fuelType, fuelStorage);
	}

	public FluidStack coolantStack() {
		return new FluidStack(coolantType, coolantStorage);
	}

}
