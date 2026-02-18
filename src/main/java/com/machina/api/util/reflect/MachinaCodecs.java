package com.machina.api.util.reflect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;

public class MachinaCodecs {
    public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> enumClass) {
        return Codec.STRING.flatXmap(name -> {
            try {
                T e = Enum.valueOf(enumClass, name);
                return DataResult.success(e);
            } catch (IllegalArgumentException ignored) {
                return DataResult.error(() -> "Unknown enum name: '" + name + "' for enum class: " + enumClass);
            }
        }, e -> {
            String name = e.name();
            return DataResult.success(name);
        });
    }

    public static final <T> Codec<T> registryCodec(Registry<T> registry) {
        return ResourceLocation.CODEC.comapFlatMap(loc -> {
            T val = registry.get(loc);
            if (val == null) {
                return DataResult.error(() -> "Unknown RocketPart: " + loc);
            }
            return DataResult.success(val);
        }, registry::getKey);
    }

    public static final Codec<AABB> AABB = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("minX").forGetter(x -> x.minX), Codec.DOUBLE.fieldOf("minY").forGetter(x -> x.minY),
            Codec.DOUBLE.fieldOf("minZ").forGetter(x -> x.minZ), Codec.DOUBLE.fieldOf("maxX").forGetter(x -> x.maxX),
            Codec.DOUBLE.fieldOf("maxY").forGetter(x -> x.maxY), Codec.DOUBLE.fieldOf("maxZ").forGetter(x -> x.maxZ))
            .apply(instance, AABB::new));

    public static final Codec<Item> ITEM = registryCodec(BuiltInRegistries.ITEM);
    public static final Codec<Fluid> FLUID = registryCodec(BuiltInRegistries.FLUID);
}
