package com.machina.api.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Basic BlockEntity class which all Machina BlockEntities should extend. It
 * provides basic methods for syncing between the client and server.
 * 
 * @author Cy4Shot
 * @since Machina v0.1.0
 */
public abstract class BaseBlockEntity extends BlockEntity {

	public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void sync() {
		if (this.level instanceof ServerLevel) {
			final BlockState state = getBlockState();
			this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
			this.level.blockEntityChanged(this.worldPosition);
			this.setChanged();
		}
	}
}