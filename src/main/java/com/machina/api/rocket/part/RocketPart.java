package com.machina.api.rocket.part;

import java.util.Map;
import java.util.function.Supplier;

import com.machina.api.item.RocketPartItem;
import com.machina.client.model.rocket.RocketPartModel;
import com.machina.registration.init.RegistryInit;
import com.machina.registration.init.RegistryInit.RocketPartCallbacks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

public class RocketPart<T extends RocketPartModel> {

    public static final StreamCodec<RegistryFriendlyByteBuf, RocketPart<?>> STREAM_CODEC = ByteBufCodecs
            .registry(RegistryInit.ROCKET_PART_REGISTRY.key());

    public static final Codec<RocketPart<?>> CODEC = ResourceLocation.CODEC.comapFlatMap(loc -> {
        RocketPart<?> part = RegistryInit.ROCKET_PART_REGISTRY.get(loc);
        if (part == null) {
            return DataResult.error(() -> "Unknown RocketPart: " + loc);
        }
        return DataResult.success(part);
    }, RocketPart::getLoc);

    private final ResourceLocation loc;
    private final RocketPartType type;
    private final float height, offset, guiScale;
    private final Supplier<T> model;

    private final float mass;

    private RocketPartItem item;

    public RocketPart(ResourceLocation loc, RocketPartType type, float height, float offset, float guiScale,
            Supplier<T> model, float mass) {
        this.loc = loc;
        this.type = type;
        this.height = height;
        this.offset = offset;
        this.guiScale = guiScale;
        this.model = model;
        this.mass = mass;
    }

    public MutableComponent getName() {
        return Component.translatable("rocket_part." + loc.getNamespace() + "." + loc.getPath());
    }

    @OnlyIn(Dist.CLIENT)
    public T bake() {
        return model.get();
    }

    public ResourceLocation getLoc() {
        return loc;
    }

    public float getMass() {
        return mass;
    }

    public RocketPartType getType() {
        return type;
    }

    public float getGUIScale() {
        return (1f / height) * guiScale;
    }

    public float getModelHeight() {
        return height;
    }

    public float getModelOffset() {
        return offset;
    }

    @SuppressWarnings("unchecked")
    public RocketPartItem getItem() {
        if (item != null) {
            return item;
        }
        Map<RocketPart<?>, ResourceLocation> map = (Map<RocketPart<?>, ResourceLocation>) RegistryInit.ROCKET_PART_REGISTRY
                .getSlaveMap(RocketPartCallbacks.ROCKET_PART_TO_ITEM, Map.class);
        ResourceLocation itemLoc = map.get(this);
        if (BuiltInRegistries.ITEM.get(itemLoc) instanceof RocketPartItem rocketPartItem) {
            this.item = rocketPartItem;
            return rocketPartItem;
        }
        throw new IllegalStateException("Rocket part item not found for " + loc);
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("name", StringTag.valueOf(loc.toString()));
        return tag;
    }

    public static RocketPart<?> fromNBT(CompoundTag tag) {
        ResourceLocation loc = ResourceLocation.parse(tag.get("name").getAsString());
        return RegistryInit.ROCKET_PART_REGISTRY.get(loc);
    }
}