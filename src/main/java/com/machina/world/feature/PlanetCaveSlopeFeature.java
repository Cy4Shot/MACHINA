package com.machina.world.feature;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings;
import com.machina.api.util.block.BlockHelper;
import com.machina.registration.init.TagInit.BlockTagInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.HashSet;
import java.util.Set;

public class PlanetCaveSlopeFeature extends Feature<PlanetCaveSlopeFeature.PlanetCaveSlopeFeatureConfig> {

    public record PlanetCaveSlopeFeatureConfig(CaveSurface surface, PlanetBiomeSettings settings)
            implements FeatureConfiguration {
        public static final Codec<PlanetCaveSlopeFeatureConfig> CODEC = RecordCodecBuilder
                .create(instance -> instance
                        .group(CaveSurface.CODEC.fieldOf("surface").forGetter(PlanetCaveSlopeFeatureConfig::surface),
                                PlanetBiomeSettings.CODEC.fieldOf("settings")
                                        .forGetter(PlanetCaveSlopeFeatureConfig::settings))
                        .apply(instance, PlanetCaveSlopeFeatureConfig::new));
    }

    public PlanetCaveSlopeFeature() {
        super(PlanetCaveSlopeFeatureConfig.CODEC);
    }

    public boolean place(FeaturePlaceContext<PlanetCaveSlopeFeatureConfig> conf) {
        WorldGenLevel level = conf.level();
        RandomSource rand = conf.random();
        BlockPos pos = conf.origin();

        // Prevent surface generation
        if (level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ()) - 2 <= pos.getY())
            return false;

        NormalNoise noise = NormalNoise.create(rand, -8, 0.5, 1, 2, 1, 2, 1, 0, 2, 0);
        int radius = 4;
        Set<BlockPos> set = this.placeGroundPatch(level, rand, pos, radius, radius, conf.config().surface());
        this.distributeVegetation(level, rand, set, conf.config().surface(), noise, conf.config().settings());
        return !set.isEmpty();
    }

    protected Set<BlockPos> placeGroundPatch(WorldGenLevel level, RandomSource rand, BlockPos pos, int rx, int ry,
                                             CaveSurface surf) {
        BlockPos.MutableBlockPos pos1 = pos.mutable();
        BlockPos.MutableBlockPos pos2 = pos1.mutable();
        Direction direction = surf.getDirection();
        Direction direction1 = direction.getOpposite();
        Set<BlockPos> set = new HashSet<>();

        for (int i = -rx; i <= rx; ++i) {
            boolean flag = i == -rx || i == rx;

            for (int j = -ry; j <= ry; ++j) {
                boolean flag1 = j == -ry || j == ry;
                boolean flag2 = flag || flag1;
                boolean flag3 = flag && flag1;
                boolean flag4 = flag2 && !flag3;
                if (!flag3 && (!flag4 || rand.nextFloat() < 0.3f)) {
                    pos1.setWithOffset(pos, i, 0, j);

                    for (int k = 0; level.isStateAtPosition(pos1, BlockBehaviour.BlockStateBase::isAir) && k < 5; ++k) {
                        pos1.move(direction);
                    }

                    for (int i1 = 0; level.isStateAtPosition(pos1, s -> !s.isAir()) && i1 < 5; ++i1) {
                        pos1.move(direction1);
                    }

                    pos2.setWithOffset(pos1, surf.getDirection());
                    BlockState blockstate = level.getBlockState(pos2);
                    if (level.isEmptyBlock(pos1)
                            && blockstate.isFaceSturdy(level, pos2, surf.getDirection().getOpposite())) {
                        set.add(pos2.immutable());
                    }
                }
            }
        }

        return set;
    }

    protected void distributeVegetation(WorldGenLevel level, RandomSource rand, Set<BlockPos> poss, CaveSurface surf,
                                        NormalNoise noise, PlanetBiomeSettings settings) {
        for (BlockPos pos : poss) {
            if (rand.nextFloat() < 0.8) {
                decorateAt(level, pos.relative(surf.getDirection().getOpposite()), rand, noise, true, settings);
            }
        }

    }

    public static void decorateAt(WorldGenLevel chunk, BlockPos pos, RandomSource rand, NormalNoise noise,
                                  boolean allowVerticalConnections, PlanetBiomeSettings settings) {
        for (Direction dir : Direction.values()) {

            BlockPos adjecent = pos.relative(dir);

            if (canGenSide(chunk, chunk.getBlockState(adjecent), dir)) {
                switch (dir) {
                    case UP:
                        // Gen Ceil

                        break;
                    case DOWN:
                        // Gen Floor

                        final double d = getNoise(noise, adjecent, 0.125d);
                        if (d < -0.3d)
                            chunk.setBlock(adjecent, settings.extra(), 3);
                        break;
                    default:
                        // Gen Wall

                        break;
                }
            }
        }

        for (Direction dir : Direction.values()) {
            BlockPos adjecent = pos.relative(dir);
            if (canGenExtra(chunk.getBlockState(pos), chunk.getBlockState(adjecent))) {
                switch (dir) {
                    case UP:
                        // Gen Ceil Extra

                        break;
                    case DOWN:
                        // Gen Floor Extra

                        break;
                    default:

                        if (dir == Direction.EAST && adjecent.getX() % 16 == 0)
                            break;
                        if (dir == Direction.SOUTH && adjecent.getZ() % 16 == 0)
                            break;
                        if (dir == Direction.WEST && (adjecent.getX() + 1) % 16 == 0)
                            break;
                        if (dir == Direction.NORTH && (adjecent.getZ() + 1) % 16 == 0)
                            break;

                        genSlope(chunk, pos, dir, rand, allowVerticalConnections, settings);
                        break;
                }
            }
        }

    }

    public static void genSlope(WorldGenLevel world, BlockPos pos, Direction wallDir, RandomSource randomSource,
                                boolean allowVerticalConnections, PlanetBiomeSettings settings) {

        BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos().set(pos);

        mutPos.set(pos).move(0, -1, 0);
        final boolean isDown = carvable(world.getBlockState(mutPos));
        mutPos.set(pos).move(0, 1, 0);
        final boolean isUp = carvable(world.getBlockState(mutPos));
        if (!isDown && !isUp)
            return;

        if (!allowVerticalConnections) {
            if (world.getBlockState(pos.north()).isAir() && world.getBlockState(pos.east()).isAir()
                    && world.getBlockState(pos.south()).isAir() && world.getBlockState(pos.west()).isAir())
                return;
        }

        mutPos.set(pos);
        int air = 0;
        Direction oppDir = wallDir.getOpposite();
        while (air < 16 && !world.getBlockState(mutPos.move(oppDir)).isFaceSturdy(world, mutPos, wallDir))
            ++air;

        int chance = 4;
        if (air <= 3)
            chance = 2;
        if (randomSource.nextInt(10) >= chance)
            return;
        if (randomSource.nextInt(5) <= 2)
            genBlock(world, pos,
                    BlockHelper.waterlog(settings.stair().setValue(BlockStateProperties.HORIZONTAL_FACING, wallDir)
                            .setValue(BlockStateProperties.HALF, isDown ? Half.BOTTOM : Half.TOP), world, pos));
        else
            genBlock(world, pos, BlockHelper.waterlog(
                    settings.slab().setValue(BlockStateProperties.SLAB_TYPE, isDown ? SlabType.BOTTOM : SlabType.TOP),
                    world, pos));
    }

    public static void genBlock(WorldGenLevel world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 3);
    }

    public static double getNoise(NormalNoise noise, BlockPos pos, double frequency) {
        return noise.getValue((double) pos.getX() * frequency, (double) pos.getY() * frequency,
                (double) pos.getZ() * frequency);
    }

    public static boolean canGenSide(WorldGenLevel chunk, BlockState state, Direction dir) {
        return carvable(state);
    }

    public static boolean canGenExtra(BlockState state, BlockState sState) {
        return state.isAir() && carvable(sState);
    }

    public static boolean carvable(BlockState state) {
        return state.is(BlockTagInit.PLANET_CARVABLE);
    }

}
