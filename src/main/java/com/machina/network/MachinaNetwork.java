package com.machina.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.gson.internal.UnsafeAllocator;
import com.machina.Machina;
import com.machina.api.network.C2SMessage;
import com.machina.api.network.ConfigMessage;
import com.machina.api.network.S2CMessage;
import com.machina.api.network.c2s.C2SAssemblyStationCraft;
import com.machina.api.network.c2s.C2SFinishCinematic;
import com.machina.api.network.c2s.C2SItemMenuSync;
import com.machina.api.network.c2s.C2SMenuSetItem;
import com.machina.api.network.c2s.C2SMenuToggleConnector;
import com.machina.api.network.c2s.C2SPartBenchCraft;
import com.machina.api.network.c2s.C2SRocketRefuelingStationSetTank;
import com.machina.api.network.c2s.C2SRocketCinematicOffset;
import com.machina.api.network.c2s.C2SRocketLandComplete;
import com.machina.api.network.c2s.C2SRocketLaunch;
import com.machina.api.network.c2s.C2SRocketLaunchComplete;
import com.machina.api.network.c2s.C2SRocketSetDestination;
import com.machina.api.network.c2s.C2SSideConfig;
import com.machina.api.network.c2s.C2SSpawnParticle;
import com.machina.api.network.config.C2SAckPayload;
import com.machina.api.network.config.S2CSyncBiomes;
import com.machina.api.network.config.S2CSyncStarchart;
import com.machina.api.network.s2c.S2CCinematicLand;
import com.machina.api.network.s2c.S2CCinematicLaunch;
import com.machina.api.network.s2c.S2CFluidEntitySync;
import com.machina.api.network.s2c.S2CFluidSync;
import com.machina.api.network.s2c.S2COpenDirectionalContainer;
import com.machina.api.network.s2c.S2CRocketRefuelingStationSync;
import com.machina.api.network.s2c.S2CRocketScreenOpen;
import com.machina.api.network.s2c.S2CUpdateDimensionList;
import com.machina.api.network.s2c.S2CWeatherEventChange;
import com.machina.api.network.s2c.S2CWindDirectionChange;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class MachinaNetwork {

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar reg = event.registrar(Machina.VERSION);

		c2s(reg, C2SFinishCinematic.class);
		c2s(reg, C2SSpawnParticle.class);
		c2s(reg, C2SMenuSetItem.class);
		c2s(reg, C2SMenuToggleConnector.class);
		c2s(reg, C2SItemMenuSync.class);
		c2s(reg, C2SSideConfig.class);
		c2s(reg, C2SPartBenchCraft.class);
		c2s(reg, C2SAssemblyStationCraft.class);
		c2s(reg, C2SRocketSetDestination.class);
		c2s(reg, C2SRocketLaunch.class);
		c2s(reg, C2SRocketCinematicOffset.class);
		c2s(reg, C2SRocketLaunchComplete.class);
		c2s(reg, C2SRocketLandComplete.class);
		c2s(reg, C2SRocketRefuelingStationSetTank.class);

		s2c(reg, S2CWeatherEventChange.class);
		s2c(reg, S2CWindDirectionChange.class);
		s2c(reg, S2COpenDirectionalContainer.class);
		s2c(reg, S2CFluidSync.class);
		s2c(reg, S2CFluidEntitySync.class);
		s2c(reg, S2CUpdateDimensionList.class);
		s2c(reg, S2CRocketScreenOpen.class);
		s2c(reg, S2CCinematicLaunch.class);
		s2c(reg, S2CCinematicLand.class);
		s2c(reg, S2CRocketRefuelingStationSync.class);

		config2c(reg, S2CSyncStarchart.class);
		config2c(reg, S2CSyncBiomes.class);
		config2s(reg, C2SAckPayload.class);
	}

	@SuppressWarnings("unchecked")
	private static <M extends C2SMessage<M>> void c2s(final PayloadRegistrar reg, Class<M> clazz) {
		try {
			CustomPacketPayload.Type<M> type = C2SMessage.getType(clazz);
			Method codecGetter = clazz.getMethod("streamCodec");
			StreamCodec<? super RegistryFriendlyByteBuf, M> codec = (StreamCodec<? super RegistryFriendlyByteBuf, M>) codecGetter
					.invoke(createDummyInstance(clazz));
			reg.playToServer(type, codec,
					(payload, ctx) -> payload.handle(ctx.player().getServer(), (ServerPlayer) ctx.player()));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static <M extends S2CMessage<M>> void s2c(final PayloadRegistrar reg, Class<M> clazz) {
		try {
			CustomPacketPayload.Type<M> type = S2CMessage.getType(clazz);
			Method codecGetter = clazz.getMethod("streamCodec");
			StreamCodec<? super RegistryFriendlyByteBuf, M> codec = (StreamCodec<? super RegistryFriendlyByteBuf, M>) codecGetter
					.invoke(createDummyInstance(clazz));
			reg.playToClient(type, codec, (payload, ctx) -> payload.handle(ctx.player()));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static <M extends ConfigMessage<M>> void config2c(final PayloadRegistrar reg, Class<M> clazz) {
		try {
			CustomPacketPayload.Type<M> type = ConfigMessage.getType(clazz);
			Method codecGetter = clazz.getMethod("streamCodec");
			StreamCodec<? super FriendlyByteBuf, M> codec = (StreamCodec<? super FriendlyByteBuf, M>) codecGetter
					.invoke(createDummyInstance(clazz));
			reg.configurationToClient(type, codec, (payload, ctx) -> payload.handle(ctx));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static <M extends ConfigMessage<M>> void config2s(final PayloadRegistrar reg, Class<M> clazz) {
		try {
			CustomPacketPayload.Type<M> type = ConfigMessage.getType(clazz);
			Method codecGetter = clazz.getMethod("streamCodec");
			StreamCodec<? super FriendlyByteBuf, M> codec = (StreamCodec<? super FriendlyByteBuf, M>) codecGetter
					.invoke(createDummyInstance(clazz));
			reg.configurationToServer(type, codec, (payload, ctx) -> payload.handle(ctx));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private static final <M extends CustomPacketPayload> M createDummyInstance(Class<M> clazz) {
		try {
			return UnsafeAllocator.INSTANCE.newInstance(clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
