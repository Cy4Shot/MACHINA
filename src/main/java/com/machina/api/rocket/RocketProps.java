package com.machina.api.rocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.registration.init.RegistryInit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public record RocketProps(boolean empty, float mass, Fluid fuelType, int fuelStorage, float fuelEfficiency,
        Fluid coolantType, int coolantStorage, float coolantEfficiency, int slots, float maxPressure,
        List<RocketPart<?>> parts) {

    public static final EntityDataSerializer<RocketProps> SERIALIZER = new EntityDataSerializer.ForValueType<RocketProps>() {
        @Override
        public void write(FriendlyByteBuf buf, RocketProps props) {
            buf.writeBoolean(props.empty());
            if (props.empty())
                return;
            buf.writeFloat(props.mass());
            buf.writeFluidStack(props.fuelStack());
            buf.writeFloat(props.fuelEfficiency());
            buf.writeFluidStack(props.coolantStack());
            buf.writeFloat(props.coolantEfficiency());
            buf.writeInt(props.slots());
            buf.writeFloat(props.maxPressure());
            for (RocketPart<?> part : props.parts()) {
                buf.writeResourceLocation(part.getLoc());
            }
        }

        @Override
        public RocketProps read(FriendlyByteBuf buf) {
            if (buf.readBoolean())
                return RocketProps.NULL;

            float mass = buf.readFloat();
            FluidStack fuel = buf.readFluidStack();
            float fuelEfficiency = buf.readFloat();
            FluidStack coolant = buf.readFluidStack();
            float coolantEfficiency = buf.readFloat();
            int slots = buf.readInt();
            float maxPressure = buf.readFloat();
            List<RocketPart<?>> parts = new ArrayList<RocketPart<?>>();
            for (int i = 0; i < RocketPartType.values().length; i++) {
                parts.add(RegistryInit.ROCKET_PARTS_REGISTRY.get().getValue(buf.readResourceLocation()));
            }
            return new RocketProps(false, mass, fuel.getFluid(), fuel.getAmount(), fuelEfficiency, coolant.getFluid(),
                    coolant.getAmount(), coolantEfficiency, slots, maxPressure, parts);
        }
    };

    public static final RocketProps NULL = new RocketProps(true, 0, null, 0, 0, null, 0, 0, 0, 0, List.of());

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
        return new RocketProps(false, mass, thruster.getFuel().fluid(), fuelTank.getFuelStorage(),
                thruster.getFuelEfficiency(), chassis.getCoolant().fluid(), fuelTank.getCoolantStorage(),
                chassis.getCoolantEfficiency(), lifeSupport.getSlots(), shield.getMaxAtmPressure(),
                List.of(thruster, fuelTank, chassis, lifeSupport, shield));
    }

    public static RocketProps fromNBT(CompoundTag tag) {
        if (tag.getBoolean(PROPERTY_EMPTY)) {
            return NULL;
        }

        Fluid fuel = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString(PROPERTY_FUEL_TYPE)));
        Fluid coolant = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString(PROPERTY_COOLANT_TYPE)));
        List<RocketPart<?>> parts = new ArrayList<RocketPart<?>>();
        ListTag rocket_parts = tag.getList(PROPERTY_PARTS, Tag.TAG_COMPOUND);
        for (int i = 0; i < rocket_parts.size(); i++) {
            parts.add(RocketPart.fromNBT(rocket_parts.getCompound(i)));
        }

        return new RocketProps(false, tag.getFloat(PROPERTY_MASS), fuel, tag.getInt(PROPERTY_FUEL_STORAGE),
                tag.getFloat(PROPERTY_FUEL_EFFICIENCY), coolant, tag.getInt(PROPERTY_COOLANT_STORAGE),
                tag.getFloat(PROPERTY_COOLANT_EFFICIENCY), tag.getInt(PROPERTY_SLOTS),
                tag.getFloat(PROPERTY_MAX_PRESSURE), parts);
    }

    public CompoundTag toNBT() {
        CompoundTag props = new CompoundTag();
        props.putBoolean(PROPERTY_EMPTY, empty);
        if (empty) {
            return props;
        }

        String fuel = Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fuelType)).toString();
        String coolant = Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(coolantType)).toString();
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
