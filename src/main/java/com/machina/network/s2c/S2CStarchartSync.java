package com.machina.network.s2c;

import com.machina.client.ClientStarchart;
import com.machina.network.INetworkMessage;
import com.machina.util.serial.BaseNBTMap;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2CStarchartSync implements INetworkMessage {

	private final BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> starchart;

	public S2CStarchartSync(final BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> starchart) {
		this.starchart = starchart;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			ClientStarchart.setStarchart(starchart);
        });
        context.setPacketHandled(true);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeNbt(starchart.serializeNBT());
	}

	public static S2CStarchartSync decode(PacketBuffer buffer) {
		BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> chart = StarchartData.createEmptyStarchart();
		chart.deserializeNBT(buffer.readNbt());
		return new S2CStarchartSync(chart);
	}
}