/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.network;

import java.util.Optional;
import java.util.function.Function;

import com.machina.api.network.message.INetworkMessage;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public abstract class BaseNetwork {
	
	public static SimpleChannel MACHINA_CHANNEL = null;

	protected static int ID = 0;

	protected static int nextId() {
		return ID++;
	}

	protected static <M extends INetworkMessage> void registerClientToServer(SimpleChannel channel, Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerMessage(channel, type, decoder, NetworkDirection.PLAY_TO_SERVER);
	}

	protected static <M extends INetworkMessage> void registerServerToClient(SimpleChannel channel, Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerMessage(channel, type, decoder, NetworkDirection.PLAY_TO_CLIENT);
	}

	private static <M extends INetworkMessage> void registerMessage(SimpleChannel channel, Class<M> msgClass,
			Function<PacketBuffer, M> decoder, NetworkDirection direction) {
		channel.registerMessage(nextId(), msgClass, INetworkMessage::encode, decoder, INetworkMessage::handle,
				Optional.of(direction));
	}

	public static <MSG> void sendToAllTracking(SimpleChannel channel, MSG message, TileEntity tile) {
		sendToAllTracking(channel, message, tile.getLevel(), tile.getBlockPos());
	}

	@SuppressWarnings("resource")
	public static <MSG> void sendToAllTracking(SimpleChannel channel, MSG message, World world, BlockPos pos) {
		if (world instanceof ServerWorld) {
			((ServerWorld) world).getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
					.forEach(p -> sendTo(channel, message, p));
		} else {
			channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)),
					message);
		}
	}

	public static <MSG> void sendToAllInWorld(SimpleChannel channel, MSG message, World world) {
		if (world instanceof ServerWorld) {
			((ServerWorld) world).getPlayers(player -> (player.level == world))
					.forEach(p -> sendTo(channel, message, p));
		} else {
			channel.send(PacketDistributor.ALL.noArg(), message);
		}
	}

	public static <MSG> void sendToAll(SimpleChannel channel, MSG message) {
		channel.send(PacketDistributor.ALL.noArg(), message);
	}

	/**
	 * Send this message to the specified player.
	 *
	 * @param message - the message to send
	 * @param player  - the player to send it to
	 */
	public static <MSG> void sendTo(SimpleChannel channel, MSG message, ServerPlayerEntity player) {
		channel.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

}
