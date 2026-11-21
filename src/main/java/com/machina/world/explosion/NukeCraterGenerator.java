package com.machina.world.explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.machina.api.util.math.OpenSimplex2F;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NukeCraterGenerator {

	private static final int WORKER_COUNT = 4;
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(WORKER_COUNT, r -> {
		Thread t = new Thread(r, "NukeCrater-Worker");
		t.setDaemon(true);
		return t;
	});

	private final ServerLevel level;
	private final BlockPos center;
	private final long seed;

	private final int craterRadius;
	private final int craterDepth;
	private final int craterHeight;
	private final int fireRadius;
	private final int minY;
	private final int maxY;
	private final int lavaRadius;

	private final ConcurrentLinkedQueue<ChunkPos> chunkQueue = new ConcurrentLinkedQueue<>();
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	private final AtomicInteger chunksRemaining = new AtomicInteger(0);
	private final AtomicBoolean started = new AtomicBoolean(false);

	public record BlockChange(BlockPos pos, BlockState state) {
	}

	public NukeCraterGenerator(ServerLevel level, double x, double y, double z, float power) {
		this.level = level;
		this.center = new BlockPos((int) x, (int) y, (int) z);
		this.seed = (long) (x * 31 + y * 17 + z * 13);

		this.craterRadius = (int) (power * 16);
		this.craterDepth = (int) (power * 5);
		this.craterHeight = (int) (craterDepth * 0.6);
		this.fireRadius = (int) (power * 20);
		this.minY = Math.max(level.getMinBuildHeight() + 5, center.getY() - craterDepth - 10);
		this.maxY = center.getY() + craterDepth + 10;
		this.lavaRadius = craterRadius / 4;
	}

	public void startAsync() {
		// Populate chunk queue immediately
		List<ChunkPos> chunks = getAffectedChunksSorted();
		chunksRemaining.set(chunks.size());
		chunkQueue.addAll(chunks);

		// Start workers immediately
		started.set(true);
		for (int i = 0; i < WORKER_COUNT; i++) {
			EXECUTOR.submit(this::workerLoop);
		}
	}

	public void cancel() {
		cancelled.set(true);
	}

	public boolean isComplete() {
		return started.get() && chunksRemaining.get() <= 0;
	}

	public void tick() {
		// Workers run independently, nothing to do here
	}

	@SuppressWarnings("deprecation")
	private void workerLoop() {
		// Each worker gets its own Random seeded deterministically
		long workerSeed = seed + Thread.currentThread().getId();

		while (!cancelled.get()) {
			ChunkPos chunkPos = chunkQueue.poll();
			if (chunkPos == null)
				break;

			List<BlockChange> changes = calculateChunkChanges(chunkPos, workerSeed);

			if (!changes.isEmpty() && !cancelled.get()) {
				// Apply immediately on server thread
				level.getServer().execute(() -> applyChunkChanges(chunkPos, changes));
			}

			chunksRemaining.decrementAndGet();
		}
	}

	@SuppressWarnings("deprecation")
	private List<BlockChange> calculateChunkChanges(ChunkPos chunkPos, long workerSeed) {
		List<BlockChange> changes = new ArrayList<>();
		Random random = new Random(workerSeed ^ ((long) chunkPos.x * 31 + chunkPos.z));

		OpenSimplex2F noise = new OpenSimplex2F(seed);
		OpenSimplex2F detailNoise = new OpenSimplex2F(seed + 1);
		OpenSimplex2F lavaPatchNoise = new OpenSimplex2F(seed + 101);
		OpenSimplex2F fireNoise = new OpenSimplex2F(seed + 300);
		OpenSimplex2F fireClusterNoise = new OpenSimplex2F(seed + 301);

		int chunkMinX = chunkPos.getMinBlockX();
		int chunkMaxX = chunkPos.getMaxBlockX();
		int chunkMinZ = chunkPos.getMinBlockZ();
		int chunkMaxZ = chunkPos.getMaxBlockZ();

		// Track what this chunk will look like after destruction
		// We use a simple approach: calculate crater shape deterministically
		for (int x = chunkMinX; x <= chunkMaxX; x++) {
			for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
				int dx = x - center.getX();
				int dz = z - center.getZ();
				double horizontalDist = Math.sqrt(dx * dx + dz * dz);

				if (horizontalDist > fireRadius + 5)
					continue;

				double noiseVal = noise.noise2(dx * 0.1, dz * 0.1) * 0.3;
				double detailVal = detailNoise.noise2(dx * 0.3, dz * 0.3) * 0.15;
				double adjustedCraterRadius = craterRadius * (1.0 + noiseVal + detailVal);

				boolean inCrater = horizontalDist <= adjustedCraterRadius;

				// Calculate crater bounds for this column
				int craterBottom = minY;
				int craterTop = center.getY();
				if (inCrater) {
					double normalizedDist = horizontalDist / adjustedCraterRadius;
					double depthFactor = 1.0 - normalizedDist * normalizedDist;
					double verticalNoise = noise.noise2(dx * 0.15, dz * 0.15) * 0.2;
					int depth = (int) (craterDepth * depthFactor * (1.0 + verticalNoise));
					int height = (int) (craterHeight * depthFactor * (1.0 + verticalNoise));
					craterBottom = Math.max(minY, center.getY() - depth);
					craterTop = Math.min(maxY, center.getY() + height);
				}

				// Destruction pass - mark all blocks in crater as air
				if (inCrater) {
					for (int y = craterTop; y >= craterBottom; y--) {
						changes.add(new BlockChange(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState()));
					}
				}

				// Lava placement (only in crater)
				if (inCrater) {
					double lavaNoiseVal = noise.noise2(dx * 0.2, dz * 0.2) * 0.3;
					double adjustedLavaRadius = lavaRadius * (1.0 + lavaNoiseVal);
					boolean inCentralPool = horizontalDist <= adjustedLavaRadius;

					double patchVal = lavaPatchNoise.noise2(dx * 0.08, dz * 0.08);
					boolean inLavaPatch = patchVal > 0.4 && random.nextDouble() <= 0.15 * (patchVal - 0.4);

					if (inCentralPool || inLavaPatch) {
						// Find where the floor will be after destruction
						// The floor is at craterBottom - 1, check if there's solid ground there
						BlockPos floorPos = new BlockPos(x, craterBottom, z);
						BlockPos belowFloor = floorPos.below();

						if (level.isLoaded(belowFloor)) {
							BlockState belowState = level.getBlockState(belowFloor);
							// Below craterBottom won't be destroyed, so check current state
							if (belowState.isSolid()) {
								int poolDepth = inCentralPool
										? (int) (2 + random.nextInt(2) + (1 - horizontalDist / adjustedLavaRadius) * 2)
										: 1 + random.nextInt(2);
								for (int py = 0; py < poolDepth; py++) {
									changes.add(new BlockChange(floorPos.above(py), Blocks.LAVA.defaultBlockState()));
								}
							}
						}
					}
				}

				// Fire placement
				boolean inFireZone = horizontalDist <= fireRadius;
				if (inFireZone) {
					double baseProbability = inCrater ? 0.4
							: 0.5 * (1.0 - (horizontalDist - craterRadius) / (fireRadius - craterRadius));

					double fireNoiseVal = fireNoise.noise2(dx * 0.1, dz * 0.1) * 0.3;
					double clusterVal = fireClusterNoise.noise2(dx * 0.05, dz * 0.05);
					if (clusterVal > 0.2)
						baseProbability *= 1.5;

					if (random.nextDouble() <= baseProbability + fireNoiseVal) {
						if (inCrater) {
							// Fire on crater floor
							BlockPos floorPos = new BlockPos(x, craterBottom, z);
							BlockPos belowFloor = floorPos.below();
							if (level.isLoaded(belowFloor)) {
								BlockState belowState = level.getBlockState(belowFloor);
								if (belowState.isSolid() && !belowState.is(Blocks.LAVA)) {
									// Don't place fire where we're putting lava
									double lavaNoiseVal = noise.noise2(dx * 0.2, dz * 0.2) * 0.3;
									boolean wouldHaveLava = horizontalDist <= lavaRadius * (1.0 + lavaNoiseVal);
									if (!wouldHaveLava) {
										changes.add(new BlockChange(floorPos, Blocks.FIRE.defaultBlockState()));
									}
								}
							}
						} else {
							// Fire outside crater - find surface
							int searchTop = center.getY() + 20;
							int searchBottom = center.getY() - craterDepth - 10;

							for (int y = searchTop; y > searchBottom; y--) {
								BlockPos pos = new BlockPos(x, y, z);
								BlockPos below = pos.below();

								if (level.isLoaded(pos) && level.isLoaded(below)) {
									BlockState posState = level.getBlockState(pos);
									BlockState belowState = level.getBlockState(below);

									if (posState.isAir() && belowState.isSolid()) {
										changes.add(new BlockChange(pos, Blocks.FIRE.defaultBlockState()));
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		return changes;
	}

	private void applyChunkChanges(ChunkPos chunkPos, List<BlockChange> changes) {
		if (cancelled.get())
			return;
		if (!level.hasChunk(chunkPos.x, chunkPos.z))
			return;

		for (BlockChange change : changes) {
			BlockPos pos = change.pos;
			BlockState current = level.getBlockState(pos);

			if (current.getDestroySpeed(level, pos) < 0)
				continue;

			level.setBlock(pos, change.state, 2 | 16 | 64);
		}

		// Fluid updates at borders
		int minX = chunkPos.getMinBlockX();
		int maxX = chunkPos.getMaxBlockX();
		int minZ = chunkPos.getMinBlockZ();
		int maxZ = chunkPos.getMaxBlockZ();

		for (BlockChange change : changes) {
			if (change.state.isAir()) {
				BlockPos pos = change.pos;
				int x = pos.getX();
				int z = pos.getZ();
				if (x == minX || x == maxX || z == minZ || z == maxZ) {
					level.scheduleTick(pos, Blocks.AIR, 1);
				}
			}
		}
	}

	private List<ChunkPos> getAffectedChunksSorted() {
		List<ChunkPos> chunks = new ArrayList<>();
		ChunkPos centerChunk = new ChunkPos(center);

		int minChunkX = (center.getX() - fireRadius - 16) >> 4;
		int maxChunkX = (center.getX() + fireRadius + 16) >> 4;
		int minChunkZ = (center.getZ() - fireRadius - 16) >> 4;
		int maxChunkZ = (center.getZ() + fireRadius + 16) >> 4;

		for (int cx = minChunkX; cx <= maxChunkX; cx++) {
			for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
				chunks.add(new ChunkPos(cx, cz));
			}
		}

		// Process center first for visual effect
		chunks.sort((a, b) -> {
			int distA = Math.abs(a.x - centerChunk.x) + Math.abs(a.z - centerChunk.z);
			int distB = Math.abs(b.x - centerChunk.x) + Math.abs(b.z - centerChunk.z);
			return Integer.compare(distA, distB);
		});

		return chunks;
	}
}
