package com.machina.network.message;

import com.machina.client.cinema.effect.ShakeManager;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class S2CShakeScreen implements INetworkMessage {

	private final float intensity;
	private final int duration;

	public S2CShakeScreen(float intensity, int duration) {
		this.intensity = intensity;
		this.duration = duration;
	}

	@Override
	public void handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> ShakeManager.shake(this.duration, this.intensity)));
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeFloat(this.intensity);
		buffer.writeInt(this.duration);
	}

	public static S2CShakeScreen decode(PacketBuffer buffer) {
		return new S2CShakeScreen(buffer.readFloat(), buffer.readInt());
	}

}
