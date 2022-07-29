package com.machina.network.c2s;

import com.machina.network.INetworkMessage;
import com.machina.util.server.ParticleHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SSpawnParticle implements INetworkMessage {

	private Vector3d pos;
	private Vector3d offset;
	private float maxSpeed;
	private int count;
	private IParticleData particle;

	public <T extends IParticleData> C2SSpawnParticle(T pParticle, float pMaxSpeed, int pCount,Vector3d pos,
			Vector3d offset) {
		this.pos = pos;
		this.particle = pParticle;
		this.maxSpeed = pMaxSpeed;
		this.count = pCount;
		this.offset = offset;
	}

	@Override
	public void handle(Context context) {
		ParticleHelper.spawnParticle(context.getSender().getLevel(), particle, pos, count, maxSpeed, offset);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeInt(Registry.PARTICLE_TYPE.getId(this.particle.getType()));
		buffer.writeDouble(pos.x());
		buffer.writeDouble(pos.y());
		buffer.writeDouble(pos.z());
		buffer.writeDouble(offset.x());
		buffer.writeDouble(offset.y());
		buffer.writeDouble(offset.z());
		buffer.writeFloat(this.maxSpeed);
		buffer.writeInt(this.count);
		this.particle.writeToNetwork(buffer);
	}

	public static C2SSpawnParticle decode(PacketBuffer buffer) {
		ParticleType<?> particletype = Registry.PARTICLE_TYPE.byId(buffer.readInt());
		if (particletype == null) {
			particletype = ParticleTypes.BARRIER;
		}

		Vector3d pos = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		Vector3d offset = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		float maxSpeed = buffer.readFloat();
		int count = buffer.readInt();
		IParticleData particle = readParticle(buffer, particletype);

		return new C2SSpawnParticle(particle, maxSpeed, count, pos, offset);
	}

	private static <T extends IParticleData> T readParticle(PacketBuffer buffer, ParticleType<T> pParticleType) {
		return pParticleType.getDeserializer().fromNetwork(pParticleType, buffer);
	}

}
