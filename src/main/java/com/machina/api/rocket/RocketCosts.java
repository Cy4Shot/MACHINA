package com.machina.api.rocket;

import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.PlanetHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record RocketCosts(boolean possible, float distance, int fuelRequired, int coolantRequired, float maxTemp,
        float maxPres) {

    public static final EntityDataSerializer<RocketCosts> SERIALIZER = new EntityDataSerializer.ForValueType<RocketCosts>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, RocketCosts> codec() {
            return StreamCodec.composite(// @formatter:off
                    ByteBufCodecs.BOOL, RocketCosts::possible,
                    ByteBufCodecs.FLOAT, RocketCosts::distance,
                    ByteBufCodecs.INT, RocketCosts::fuelRequired,
                    ByteBufCodecs.INT, RocketCosts::coolantRequired,
                    ByteBufCodecs.FLOAT, RocketCosts::maxTemp,
                    ByteBufCodecs.FLOAT, RocketCosts::maxPres,
                    RocketCosts::new
            ); // @formatter:on
        }
    };

    public static final RocketCosts NULL = new RocketCosts(false, 0, 0, 0, 0, 0);

    private static final String PROPERTY_POSSIBLE = "possible";
    private static final String PROPERTY_DISTANCE = "distance";
    private static final String PROPERTY_FUEL = "fuel";
    private static final String PROPERTY_COOLANT = "coolant";
    private static final String PROPERTY_MAX_TEMP = "temp";
    private static final String PROPERTY_MAX_PRES = "pres";

    private static Integer extractID(ResourceKey<Level> src) {
        if (src.equals(Level.OVERWORLD)) {
            return -1;
        }
        return PlanetHelper.getIdLevelOr(src, null);
    }

    private static void calculateAtmCosts(RocketProps props, Planet planet, boolean entry, float[] output) {
        double radius = planet != null ? planet.radius() : 6371; // TODO Const
        double sugrav = planet != null ? planet.surf_grav() : 1;

        if (planet != null && planet.gas_giant()) {
            double radiusMeters = planet.radius() * 1000; // radius at 1-bar pressure
            double massKg = planet.mass() * 5.972e24; // Earth-masses -> kg // TODO Const
            double G = 6.6743e-11; // TODO Const

            sugrav = G * massKg / (radiusMeters * radiusMeters);
        }

        double planetRadiusMeters = radius * 1000; // TODO Const
        double gravity = sugrav * 9.80665; // TODO Const
        double pathLength = Math.min(0.05 * planetRadiusMeters, 200_000); // TODO Const
        double impulse;
        if (entry) {
            double v = Math.sqrt(2 * gravity * pathLength);
            impulse = props.mass() / 10 * v; // TODO: Define mass downscale as config option
        } else {
            impulse = props.mass() / 10 * gravity * pathLength;
        }

        if (entry) {
            double rho;
            if (planet.gas_giant()) {
                rho = 0.16; // kg/m³, Jupiter-like 1-bar level, tune per planet
            } else {
                rho = 1.2; // Earth sea-level
            }
            double v = Math.min(Math.sqrt(2 * gravity * pathLength), planet.esc_velocity() / 100.0);
            double maxPressure = 0.5 * rho * v * v; // Pa
            double k = 1.83e-4; // TODO Const
            double maxHeatFlux = k * Math.sqrt(rho) * Math.pow(v, 3); // W/m²

            double emissivity = 0.9; // TODO: Const
            double sigma = 5.670374419e-8; // TODO: Const
            double T_inf = 200.0;

            double maxTemperature = Math.pow((maxHeatFlux / (emissivity * sigma)) + Math.pow(T_inf, 4), 0.25);

            output[1] = Math.max(output[1], (float) maxTemperature);
            output[2] = Math.max(output[2], (float) maxPressure);
        }
        output[0] += (float) (impulse / 10_000 / props.fuelEfficiency());
    }

    public static RocketCosts from(SolarSystem system, RocketProps rocket, ResourceKey<Level> source,
            ResourceKey<Level> destination) {
        Integer src = extractID(source);
        Integer dst = extractID(destination);
        if (src == null || dst == null || src == dst) {
            return NULL;
        }

        Planet srcPlanet = src != -1 ? system.planets().get(src) : null;
        Planet dstPlanet = dst != -1 ? system.planets().get(dst) : null;
        double srcDist = src != -1 ? srcPlanet.a() : 0;
        double dstDist = dst != -1 ? dstPlanet.a() : 0;

        float distance = (float) Math.abs(dstDist - srcDist); // Weird Approximation

        float[] stats = new float[] { distance * 5_000, 0, 0 }; // 5 Bucket per AU
        calculateAtmCosts(rocket, srcPlanet, false, stats);
        calculateAtmCosts(rocket, dstPlanet, true, stats);

        float fuel = stats[0];
        float temperature = stats[1];
        float coolant = (stats[1] - 273) / rocket.coolantEfficiency();
        float pressure = stats[2];

        return new RocketCosts(true, distance, (int) fuel, (int) coolant, temperature, pressure);
    }

    public static RocketCosts fromNBT(CompoundTag tag) {
        if (!tag.getBoolean(PROPERTY_POSSIBLE)) {
            return NULL;
        }

        return new RocketCosts(true, tag.getFloat(PROPERTY_DISTANCE), tag.getInt(PROPERTY_FUEL),
                tag.getInt(PROPERTY_COOLANT), tag.getFloat(PROPERTY_MAX_TEMP), tag.getFloat(PROPERTY_MAX_PRES));
    }

    public CompoundTag toNBT() {
        CompoundTag props = new CompoundTag();
        props.putBoolean(PROPERTY_POSSIBLE, possible);
        if (!possible) {
            return props;
        }
        props.putFloat(PROPERTY_DISTANCE, distance);
        props.putInt(PROPERTY_FUEL, fuelRequired);
        props.putInt(PROPERTY_COOLANT, coolantRequired);
        props.putFloat(PROPERTY_MAX_TEMP, maxTemp);
        props.putFloat(PROPERTY_MAX_PRES, maxPres);
        return props;
    }
}
