package com.machina.api.util;

import java.util.Random;

import javax.annotation.Nullable;

import org.joml.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleHelper {
    public static void spawnParticle(Level world, ParticleOptions type, BlockPos pos, double speed,
            @Nullable Vec3 offset) {
        Random r = new Random();
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.5D;
        double d2 = (double) pos.getZ() + 0.5D;
        if (offset != null) {
            d0 += offset.x();
            d1 += offset.y();
            d2 += offset.z();
        }
        double d3 = (r.nextDouble() - 0.5D) * speed;
        double d4 = (r.nextDouble() - 0.5D) * speed;
        double d5 = (r.nextDouble() - 0.5D) * speed;

        world.addParticle(type, d0, d1, d2, d3, d4, d5);
    }

    public static void spawnParticle(ServerLevel world, ParticleOptions type, Vector3f pos, int count, double speed,
            @Nullable Vector3f offset) {
        world.sendParticles(type, pos.x(), pos.y(), pos.z(), count, speed, offset.x(), offset.y(), offset.z());
    }
}