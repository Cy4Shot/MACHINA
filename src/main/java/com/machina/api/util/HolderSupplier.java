package com.machina.api.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record HolderSupplier<T>(Supplier<T> val) implements Holder<T> {
    public boolean isBound() {
        return true;
    }

    public boolean is(@NotNull ResourceLocation p_205727_) {
        return false;
    }

    public boolean is(@NotNull ResourceKey<T> p_205725_) {
        return false;
    }

    public boolean is(@NotNull TagKey<T> p_205719_) {
        return false;
    }

    public boolean is(@NotNull Predicate<ResourceKey<T>> p_205723_) {
        return false;
    }

    public boolean is(Holder<T> holder) {
        return false;
    }

    public @NotNull Either<ResourceKey<T>, T> unwrap() {
        return Either.right(this.val.get());
    }

    public @NotNull Optional<ResourceKey<T>> unwrapKey() {
        return Optional.empty();
    }

    public Holder.@NotNull Kind kind() {
        return Holder.Kind.DIRECT;
    }

    public String toString() {
        return "Direct{" + this.val.get() + "}";
    }

    public boolean canSerializeIn(@NotNull HolderOwner<T> p_256328_) {
        return true;
    }

    public @NotNull Stream<TagKey<T>> tags() {
        return Stream.of();
    }

    public @NotNull T value() {
        return this.val.get();
    }
}