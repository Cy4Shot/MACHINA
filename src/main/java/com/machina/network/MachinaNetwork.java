package com.machina.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.util.TriConsumer;

import com.machina.api.network.C2SMessage;
import com.machina.api.network.PacketSender;
import com.machina.api.network.S2CMessage;
import com.machina.api.network.c2s.C2SFinishCinematic;
import com.machina.api.network.s2c.S2CFluidSync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class MachinaNetwork {

	public static int i = 0;

	public static void init() {

		// C2S
		
		c2s(C2SFinishCinematic.class);

		// S2C

		s2c(S2CFluidSync.class);
	}

	// Note from Cy4, this is probably the worst registration code I have ever
	// written. It has 8 catch statements catching 5 different exceptions, uses way
	// too much reflection, and has horrible variable naming. It was a waste of
	// time. However, its really cool to use, as you can see above.

	@SuppressWarnings({ "unchecked", "unused" })
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
					e1.printStackTrace();
				}
			};
			Function<FriendlyByteBuf, T> d = (b) -> {
				try {
					return (T) decode.invoke(null, b);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					System.out.println(
							"Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
					e1.printStackTrace();
				}
				return null;
			};
			TriConsumer<T, MinecraftServer, ServerPlayer> h = (t, m, p) -> {
				try {
					handle.invoke(t, m, p);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					System.out.println(
							"Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
					e1.printStackTrace();
				}
			};

			PacketSender.CHANNEL.registerMessage(i++, clazz, e, d, makeServerBoundHandler(h));
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
			e.printStackTrace();
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
					e1.printStackTrace();
				}
			};
			Function<FriendlyByteBuf, T> d = (b) -> {
				try {
					return (T) decode.invoke(null, b);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					System.out.println(
							"Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
					e1.printStackTrace();
				}
				return null;
			};
			Consumer<T> h = (t) -> {
				try {
					handle.invoke(t);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					System.out.println(
							"Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
					e1.printStackTrace();
				}
			};

			PacketSender.CHANNEL.registerMessage(i++, clazz, e, d, makeClientBoundHandler(h));
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Could not register with name: " + clazz.getPackageName() + " - " + clazz.getName());
			e.printStackTrace();
		}
	}

	private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeServerBoundHandler(
			TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
		return (m, ctx) -> {
			handler.accept(m, ctx.get().getSender().getServer(), ctx.get().getSender());
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
