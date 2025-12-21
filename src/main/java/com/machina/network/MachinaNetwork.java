package com.machina.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.util.TriConsumer;

import com.machina.Machina;
import com.machina.api.network.C2SMessage;
import com.machina.api.network.PacketSender;
import com.machina.api.network.S2CMessage;
import com.machina.api.network.c2s.C2SAssemblyStationCraft;
import com.machina.api.network.c2s.C2SFinishCinematic;
import com.machina.api.network.c2s.C2SItemMenuSync;
import com.machina.api.network.c2s.C2SMenuSetItem;
import com.machina.api.network.c2s.C2SMenuToggleConnector;
import com.machina.api.network.c2s.C2SPartBenchCraft;
import com.machina.api.network.c2s.C2SRocketCinematicOffset;
import com.machina.api.network.c2s.C2SRocketLandComplete;
import com.machina.api.network.c2s.C2SRocketLaunch;
import com.machina.api.network.c2s.C2SRocketLaunchComplete;
import com.machina.api.network.c2s.C2SRocketSetDestination;
import com.machina.api.network.c2s.C2SSideConfig;
import com.machina.api.network.c2s.C2SSpawnParticle;
import com.machina.api.network.s2c.S2CCinematicLand;
import com.machina.api.network.s2c.S2CCinematicLaunch;
import com.machina.api.network.s2c.S2CFluidEntitySync;
import com.machina.api.network.s2c.S2CFluidSync;
import com.machina.api.network.s2c.S2COpenDirectionalContainer;
import com.machina.api.network.s2c.S2CRocketScreenOpen;
import com.machina.api.network.s2c.S2CSyncStarchart;
import com.machina.api.network.s2c.S2CUpdateDimensionList;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class MachinaNetwork {

    public static int i = 0;

    public static void init() {
        c2s(C2SFinishCinematic.class);
        c2s(C2SSpawnParticle.class);
        c2s(C2SMenuSetItem.class);
        c2s(C2SMenuToggleConnector.class);
        c2s(C2SItemMenuSync.class);
        c2s(C2SSideConfig.class);
        c2s(C2SPartBenchCraft.class);
        c2s(C2SAssemblyStationCraft.class);
        c2s(C2SRocketSetDestination.class);
        c2s(C2SRocketLaunch.class);
        c2s(C2SRocketCinematicOffset.class);
        c2s(C2SRocketLaunchComplete.class);
        c2s(C2SRocketLandComplete.class);

        s2c(S2COpenDirectionalContainer.class);
        s2c(S2CFluidSync.class);
        s2c(S2CFluidEntitySync.class);
        s2c(S2CSyncStarchart.class);
        s2c(S2CUpdateDimensionList.class);
        s2c(S2CRocketScreenOpen.class);
        s2c(S2CCinematicLaunch.class);
        s2c(S2CCinematicLand.class);
    }

    // Note from Cy4, this is probably the worst registration code I have ever
    // written. It has 8 catch statements catching 5 different exceptions, uses way
    // too much reflection, and has horrible variable naming. It was a waste of
    // time. However, its really cool to use, as you can see above.

    @SuppressWarnings("unchecked")
    private static <T extends C2SMessage> void c2s(Class<T> clazz) {
        try {
            Method encode = clazz.getMethod("encode", FriendlyByteBuf.class);
            Method decode = clazz.getMethod("decode", FriendlyByteBuf.class);
            Method handle = clazz.getMethod("handle", MinecraftServer.class, ServerPlayer.class);
            BiConsumer<T, FriendlyByteBuf> e = (t, b) -> {
                try {
                    encode.invoke(t, b);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                    System.out.println(
                            "Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
                    Machina.LOGGER.error(e1.getMessage());
                }
            };
            Function<FriendlyByteBuf, T> d = (b) -> {
                try {
                    return (T) decode.invoke(null, b);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                    System.out.println(
                            "Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
                    Machina.LOGGER.error(e1.getMessage());
                }
                return null;
            };
            TriConsumer<T, MinecraftServer, ServerPlayer> h = (t, m, p) -> {
                try {
                    handle.invoke(t, m, p);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                    System.out.println(
                            "Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
                    Machina.LOGGER.error(e1.getMessage());
                }
            };

            PacketSender.CHANNEL.registerMessage(i++, clazz, e, d, makeServerBoundHandler(h));
        } catch (NoSuchMethodException | SecurityException e) {
            System.out.println("Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
            Machina.LOGGER.error(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends S2CMessage> void s2c(Class<T> clazz) {
        try {
            Method encode = clazz.getMethod("encode", FriendlyByteBuf.class);
            Method decode = clazz.getMethod("decode", FriendlyByteBuf.class);
            Method handle = clazz.getMethod("handle");
            BiConsumer<T, FriendlyByteBuf> e = (t, b) -> {
                try {
                    encode.invoke(t, b);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                    System.out.println(
                            "Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
                    Machina.LOGGER.error(e1.getMessage());
                }
            };
            Function<FriendlyByteBuf, T> d = (b) -> {
                try {
                    return (T) decode.invoke(null, b);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                    System.out.println(
                            "Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
                    Machina.LOGGER.error(e1.getMessage());
                }
                return null;
            };
            Consumer<T> h = (t) -> {
                try {
                    handle.invoke(t);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                    System.out.println(
                            "Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
                    Machina.LOGGER.error(e1.getMessage());
                }
            };

            PacketSender.CHANNEL.registerMessage(i++, clazz, e, d, makeClientBoundHandler(h));
        } catch (NoSuchMethodException | SecurityException e) {
            System.out.println("Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
            Machina.LOGGER.error(e.getMessage());
        }
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeServerBoundHandler(
            TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
        return (m, ctx) -> {
            handler.accept(m, Objects.requireNonNull(ctx.get().getSender()).getServer(), ctx.get().getSender());
            ctx.get().setPacketHandled(true);
        };
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientBoundHandler(Consumer<T> consumer) {
        return (m, ctx) -> {
            consumer.accept(m);
            ctx.get().setPacketHandled(true);
        };
    }
}
