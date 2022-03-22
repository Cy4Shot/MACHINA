package com.machina.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * TODO make this work
 * @author matyrobbrt
 *
 */
public class S2CSyncGuiValues implements INetworkMessage {

	private final BlockPos pos;
	private final TileEntityType<?> teType;

	public S2CSyncGuiValues(BlockPos pos, TileEntityType<?> teType) {
		this.pos = pos;
		this.teType = teType;
	}

	@Override
	public void handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(this, context)));
	}

	@SuppressWarnings("resource")
	private static void handleClient(S2CSyncGuiValues msg, Context context) {
		World level = Minecraft.getInstance().level;
		TileEntity targetTile = level.getBlockEntity(msg.pos);
		if (targetTile == null) {
			return;
		}
		if (targetTile.getType() == msg.teType && targetTile instanceof TileEntity) {
		}
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeUtf(teType.getRegistryName().toString());
	}

	public static S2CSyncGuiValues decode(PacketBuffer buffer) {
		return new S2CSyncGuiValues(buffer.readBlockPos(),
				ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation(buffer.readUtf())));
	}

}
