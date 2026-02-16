package com.machina.api.starchart;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.ClientStarchart;
import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CSyncStarchart;
import com.machina.api.starchart.name.SystemNameGenerator;
import com.machina.api.starchart.obj.SolarSystem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class Starchart {
    private static Starchart INSTANCE = null;

    final SolarSystem system;

    public Starchart(long seed) {
        SystemNameGenerator gen = new SystemNameGenerator();
        system = StarchartGenerator.gen(seed, gen.gen(new Random(seed)));
    }
    
    public static SolarSystem system(@NotNull Level l) {
        if (l.isClientSide()) {
            return ClientStarchart.system;
        } else {
            return system((ServerLevel) l);
        }
    }

    public static SolarSystem system(@NotNull ServerLevel l) {
        return get(l).system;
    }

    public static SolarSystem system(long seed) {
        return get(seed).system;
    }

    public static Starchart get(long seed) {
        if (INSTANCE == null) {
            INSTANCE = generate(getSeed(seed));
        }
        return INSTANCE;
    }

    public static Starchart get(@NotNull ServerLevel l) {
        if (INSTANCE == null) {
            if (!l.isClientSide()) {
                INSTANCE = generate(getSeed(l.getSeed()));
            }
        }
        return INSTANCE;
    }

    public static void syncClient(ServerPlayer player) {
        long seed = getSeed(player.serverLevel().getSeed());
        PacketDistributor.sendToPlayer(player, new S2CSyncStarchart(seed));
    }

    public static long getSeed(long levelseed) {
        return new Random(levelseed).nextLong();
    }

    private static Starchart generate(long seed) {
        return new Starchart(seed);
    }
}
