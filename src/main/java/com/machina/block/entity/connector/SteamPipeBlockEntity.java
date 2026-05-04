package com.machina.block.entity.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.steam.PipeSteamStorage;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteamPipeBlockEntity extends ConnectorBlockEntity<Integer, PipeSteamStorage> {

	public SteamPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SteamPipeBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.STEAM_PIPE.get(), pos, state);
	}

	@Override
	public int getRate() {
		return Integer.MAX_VALUE;
	}

	@Override
	public PipeSteamStorage createStorage(Direction side) {
		return new PipeSteamStorage(this, side);
	}

	@Override
	public int slotsPerSide() {
		return 0;
	}
}
