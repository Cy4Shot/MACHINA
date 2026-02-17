package com.machina.api.util.reflect;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class MachinaStreamCodecs {

    public static final StreamCodec<ByteBuf, FriendlyByteBuf> FRIENDLY_BYTE_BUF = ByteBufCodecs.BYTE_ARRAY
            .map(x -> new FriendlyByteBuf(Unpooled.wrappedBuffer(x)), b -> b.readByteArray());
            
}
