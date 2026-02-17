package com.machina.network;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

import com.machina.Machina;
import com.machina.api.network.C2SMessage;
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

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class MachinaNetwork {

    @SuppressWarnings("unchecked")
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

        s2c(reg, S2COpenDirectionalContainer.class);
        s2c(reg, S2CFluidSync.class);
        s2c(reg, S2CFluidEntitySync.class);
        s2c(reg, S2CSyncStarchart.class);
        s2c(reg, S2CUpdateDimensionList.class);
        s2c(reg, S2CRocketScreenOpen.class);
        s2c(reg, S2CCinematicLaunch.class);
        s2c(reg, S2CCinematicLand.class);
    }

    @SuppressWarnings("unchecked")
    private static <M extends C2SMessage<M>> void c2s(final PayloadRegistrar reg, Class<M> clazz) {
        try {
            Method typeGetter = clazz.getMethod("getType", Class.class);
            Method codecGetter = clazz.getMethod("streamCodec");
            CustomPacketPayload.Type<M> type = (Type<M>) typeGetter.invoke(null, clazz);
            StreamCodec<? super RegistryFriendlyByteBuf, M> codec = (StreamCodec<? super RegistryFriendlyByteBuf, M>) codecGetter
                    .invoke(null);
            reg.playToServer(type, codec, (payload, ctx) -> {
                ((M) payload).handle(ctx.player().getServer(), (ServerPlayer) ctx.player());
            });
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static <M extends S2CMessage<M>> void s2c(final PayloadRegistrar reg, Class<M> clazz) {
        try {
            Method typeGetter = clazz.getMethod("getType", Class.class);
            Method codecGetter = clazz.getMethod("streamCodec", RegistryAccess.class);
            CustomPacketPayload.Type<M> type = (Type<M>) typeGetter.invoke(null, clazz);
            StreamCodec<? super RegistryFriendlyByteBuf, M> codec = (StreamCodec<? super RegistryFriendlyByteBuf, M>) codecGetter
                    .invoke(null);
            reg.playToClient(type, codec, (payload, ctx) -> ((M) payload).handle());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
