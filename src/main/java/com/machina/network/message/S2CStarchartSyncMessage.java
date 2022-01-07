package com.machina.network.message;

import com.machina.api.api_extension.ApiExtensions;
import com.machina.api.client.ClientDataHolder;
import com.machina.api.starchart.Starchart;
import com.matyrobbrt.lib.network.INetworkMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2CStarchartSyncMessage implements INetworkMessage {

    private final Starchart starchart;

    public S2CStarchartSyncMessage(final Starchart starchart) {
        this.starchart = starchart;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> executeClient(context, this));
    }

    private static void executeClient(NetworkEvent.Context context, S2CStarchartSyncMessage msg) {
        ClientDataHolder.setStarchart(msg.starchart);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeNbt(ApiExtensions.withExtension(Starchart.StarchartSerializer.class, ser -> ser.serializeNBT(starchart)));
    }

    public static S2CStarchartSyncMessage decode(PacketBuffer buffer) {
        return new S2CStarchartSyncMessage(ApiExtensions.withExtension(Starchart.StarchartSerializer.class, ser -> ser.deserializeNBT(buffer.readNbt())));
    }
}
