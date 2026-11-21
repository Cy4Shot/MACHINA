package com.machina.world.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.machina.Machina;
import com.machina.api.network.s2c.S2CClearNukes;
import com.machina.api.network.s2c.S2CNuclearExplosion;
import com.machina.world.explosion.NukeCraterGenerator;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class NuclearExplosionData extends net.minecraft.world.level.saveddata.SavedData {

	private static final Factory<NuclearExplosionData> FACTORY = new Factory<>(NuclearExplosionData::new, NuclearExplosionData::new);
	public static final String ID = Machina.MOD_ID + "_nuclear_explosions";

	private final List<ActiveExplosion> activeExplosions = new ArrayList<>();
	private final List<NukeCraterGenerator> activeGenerators = new ArrayList<>();
	private ServerLevel level;

	public static class ActiveExplosion {
		public final double x, y, z;
		public final float power;
		public final long startTick;
		public final int durationTicks;

		public ActiveExplosion(double x, double y, double z, float power, long startTick) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.power = power;
			this.startTick = startTick;
			this.durationTicks = (int) (power * 800);
		}

		public ActiveExplosion(CompoundTag tag) {
			this.x = tag.getDouble("x");
			this.y = tag.getDouble("y");
			this.z = tag.getDouble("z");
			this.power = tag.getFloat("power");
			this.startTick = tag.getLong("startTick");
			this.durationTicks = (int) (power * 800);
		}

		public CompoundTag save() {
			CompoundTag tag = new CompoundTag();
			tag.putDouble("x", x);
			tag.putDouble("y", y);
			tag.putDouble("z", z);
			tag.putFloat("power", power);
			tag.putLong("startTick", startTick);
			return tag;
		}

		public boolean isFinished(long currentTick) {
			return currentTick >= startTick + durationTicks;
		}

		public float getProgress(long currentTick) {
			long elapsed = currentTick - startTick;
			return Math.min(1.0f, (float) elapsed / durationTicks);
		}
	}

	public NuclearExplosionData() {
		super();
	}

	public NuclearExplosionData(CompoundTag tag, Provider registries) {
		load(tag);
	}

	public static NuclearExplosionData get(ServerLevel level) {
		NuclearExplosionData data = level.getDataStorage().computeIfAbsent(FACTORY, ID);
		data.level = level;
		return data;
	}

	private void load(CompoundTag tag) {
		activeExplosions.clear();
		ListTag listTag = tag.getList("explosions", Tag.TAG_COMPOUND);
		for (Tag t : listTag) {
			activeExplosions.add(new ActiveExplosion((CompoundTag) t));
		}
	}

	@Override
	public CompoundTag save(CompoundTag tag, Provider registries) {
		ListTag listTag = new ListTag();
		for (ActiveExplosion explosion : activeExplosions) {
			listTag.add(explosion.save());
		}
		tag.put("explosions", listTag);
		return tag;
	}

	public void tick() {
		if (level == null) return;
		long currentTick = level.getGameTime();

		// Tick active explosions
		Iterator<ActiveExplosion> it = activeExplosions.iterator();
		boolean changed = false;
		while (it.hasNext()) {
			ActiveExplosion explosion = it.next();
			if (explosion.isFinished(currentTick)) {
				it.remove();
				changed = true;
			}
		}
		if (changed) {
			setDirty();
		}

		// Tick crater generators
		Iterator<NukeCraterGenerator> genIt = activeGenerators.iterator();
		while (genIt.hasNext()) {
			NukeCraterGenerator generator = genIt.next();
			generator.tick();
			if (generator.isComplete()) {
				genIt.remove();
			}
		}
	}

	public void spawnExplosion(double x, double y, double z, float power) {
		if (level == null) return;
		long startTick = level.getGameTime();
		ActiveExplosion explosion = new ActiveExplosion(x, y, z, power, startTick);
		activeExplosions.add(explosion);
		setDirty();

		// Send to all players in this dimension
		S2CNuclearExplosion packet = new S2CNuclearExplosion(x, y, z, power);
		for (ServerPlayer player : level.players()) {
			PacketDistributor.sendToPlayer(player, packet);
		}

		// Start async crater generation
		NukeCraterGenerator generator = new NukeCraterGenerator(level, x, y, z, power);
		activeGenerators.add(generator);
		generator.startAsync();
	}

	public void syncToPlayer(ServerPlayer player) {
		if (level == null) return;
		long currentTick = level.getGameTime();

		// Send all active explosions to the player with their current progress
		for (ActiveExplosion explosion : activeExplosions) {
			if (!explosion.isFinished(currentTick)) {
				// Calculate elapsed time to send to client
				float progress = explosion.getProgress(currentTick);
				S2CNuclearExplosion packet = new S2CNuclearExplosion(
					explosion.x, explosion.y, explosion.z, explosion.power, progress
				);
				PacketDistributor.sendToPlayer(player, packet);
			}
		}
	}

	public List<ActiveExplosion> getActiveExplosions() {
		return new ArrayList<>(activeExplosions);
	}

	public boolean hasActiveExplosions() {
		return !activeExplosions.isEmpty();
	}

	public int clearAll() {
		int count = activeExplosions.size();

		// Cancel all crater generators
		for (NukeCraterGenerator generator : activeGenerators) {
			generator.cancel();
		}
		activeGenerators.clear();

		// Clear all explosions
		activeExplosions.clear();
		setDirty();

		// Notify all players in this dimension to clear client-side explosions
		if (level != null) {
			S2CClearNukes packet = new S2CClearNukes();
			for (ServerPlayer player : level.players()) {
				PacketDistributor.sendToPlayer(player, packet);
			}
		}

		return count;
	}
}
