package com.machina.api.util.reflect;

import java.util.function.Function;

import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function11;
import com.mojang.datafixers.util.Function12;
import com.mojang.datafixers.util.Function13;
import com.mojang.datafixers.util.Function14;
import com.mojang.datafixers.util.Function15;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MachinaStreamCodecs {

    public interface HasId {
        int getId();
    }

    public static final StreamCodec<ByteBuf, FriendlyByteBuf> FRIENDLY_BYTE_BUF = ByteBufCodecs.BYTE_ARRAY
            .map(x -> new FriendlyByteBuf(Unpooled.wrappedBuffer(x)), b -> b.readByteArray());

    public static final StreamCodec<ByteBuf, Vec3> VEC3 = ByteBufCodecs.VECTOR3F.map(Vec3::new, Vec3::toVector3f);

    public static final StreamCodec<ByteBuf, AABB> AABB_CODEC = StreamCodec.composite(VEC3, AABB::getMinPosition, VEC3,
            AABB::getMaxPosition, AABB::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, Item> ITEM = ByteBufCodecs
            .registry(BuiltInRegistries.ITEM.key());

    public static final StreamCodec<RegistryFriendlyByteBuf, Fluid> FLUID = ByteBufCodecs
            .registry(BuiltInRegistries.FLUID.key());

    public static <T extends Enum<T> & HasId> StreamCodec<ByteBuf, T> enumCodec(Class<T> enumClass) {
        return ByteBufCodecs.idMapper(
                ByIdMap.continuous(T::getId, enumClass.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.ZERO), T::getId);
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                T8 t8 = codec8.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9,
            final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                T8 t8 = codec8.decode(buffer);
                T9 t9 = codec9.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
                codec9.encode(buffer, getter9.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10,
            final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                T8 t8 = codec8.decode(buffer);
                T9 t9 = codec9.decode(buffer);
                T10 t10 = codec10.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
                codec9.encode(buffer, getter9.apply(value));
                codec10.encode(buffer, getter10.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11,
            final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                T8 t8 = codec8.decode(buffer);
                T9 t9 = codec9.decode(buffer);
                T10 t10 = codec10.decode(buffer);
                T11 t11 = codec11.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
                codec9.encode(buffer, getter9.apply(value));
                codec10.encode(buffer, getter10.apply(value));
                codec11.encode(buffer, getter11.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12, final Function<C, T12> getter12,
            final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                return factory.apply(codec1.decode(buffer), codec2.decode(buffer), codec3.decode(buffer),
                        codec4.decode(buffer), codec5.decode(buffer), codec6.decode(buffer), codec7.decode(buffer),
                        codec8.decode(buffer), codec9.decode(buffer), codec10.decode(buffer), codec11.decode(buffer),
                        codec12.decode(buffer));
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
                codec9.encode(buffer, getter9.apply(value));
                codec10.encode(buffer, getter10.apply(value));
                codec11.encode(buffer, getter11.apply(value));
                codec12.encode(buffer, getter12.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12, final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13, final Function<C, T13> getter13,
            final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                return factory.apply(codec1.decode(buffer), codec2.decode(buffer), codec3.decode(buffer),
                        codec4.decode(buffer), codec5.decode(buffer), codec6.decode(buffer), codec7.decode(buffer),
                        codec8.decode(buffer), codec9.decode(buffer), codec10.decode(buffer), codec11.decode(buffer),
                        codec12.decode(buffer), codec13.decode(buffer));
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
                codec9.encode(buffer, getter9.apply(value));
                codec10.encode(buffer, getter10.apply(value));
                codec11.encode(buffer, getter11.apply(value));
                codec12.encode(buffer, getter12.apply(value));
                codec13.encode(buffer, getter13.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12, final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13, final Function<C, T13> getter13,
            final StreamCodec<? super B, T14> codec14, final Function<C, T14> getter14,
            final Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                return factory.apply(codec1.decode(buffer), codec2.decode(buffer), codec3.decode(buffer),
                        codec4.decode(buffer), codec5.decode(buffer), codec6.decode(buffer), codec7.decode(buffer),
                        codec8.decode(buffer), codec9.decode(buffer), codec10.decode(buffer), codec11.decode(buffer),
                        codec12.decode(buffer), codec13.decode(buffer), codec14.decode(buffer));
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
                codec9.encode(buffer, getter9.apply(value));
                codec10.encode(buffer, getter10.apply(value));
                codec11.encode(buffer, getter11.apply(value));
                codec12.encode(buffer, getter12.apply(value));
                codec13.encode(buffer, getter13.apply(value));
                codec14.encode(buffer, getter14.apply(value));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12, final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13, final Function<C, T13> getter13,
            final StreamCodec<? super B, T14> codec14, final Function<C, T14> getter14,
            final StreamCodec<? super B, T15> codec15, final Function<C, T15> getter15,
            final Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, C> factory) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                T7 t7 = codec7.decode(buffer);
                T8 t8 = codec8.decode(buffer);
                T9 t9 = codec9.decode(buffer);
                T10 t10 = codec10.decode(buffer);
                T11 t11 = codec11.decode(buffer);
                T12 t12 = codec12.decode(buffer);
                T13 t13 = codec13.decode(buffer);
                T14 t14 = codec14.decode(buffer);
                T15 t15 = codec15.decode(buffer);

                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
                codec7.encode(buffer, getter7.apply(value));
                codec8.encode(buffer, getter8.apply(value));
                codec9.encode(buffer, getter9.apply(value));
                codec10.encode(buffer, getter10.apply(value));
                codec11.encode(buffer, getter11.apply(value));
                codec12.encode(buffer, getter12.apply(value));
                codec13.encode(buffer, getter13.apply(value));
                codec14.encode(buffer, getter14.apply(value));
                codec15.encode(buffer, getter15.apply(value));
            }
        };
    }

}
