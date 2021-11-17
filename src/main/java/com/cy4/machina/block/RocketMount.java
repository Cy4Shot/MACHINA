package com.cy4.machina.block;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cy4.machina.api.util.DiagonalDirection;
import com.cy4.machina.api.util.MachinaLangTextComponent;
import com.cy4.machina.block.properties.ActivationState;
import com.cy4.machina.block.properties.RelayPosState;
import com.cy4.machina.client.MachinaLang;
import com.cy4.machina.init.BlockInit;
import com.cy4.machina.init.ItemInit;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class RocketMount extends Block {

	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	public static final EnumProperty<RelayPosState> RELAY_POS_STATE = EnumProperty.create("relay_pos_state",
			RelayPosState.class, RelayPosState.values());

	public RocketMount(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, Boolean.FALSE)
				.setValue(RELAY_POS_STATE, RelayPosState.N_A));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(ACTIVATED, RELAY_POS_STATE);
	}

	private static final List<Direction> STRAIGHT_DIRECTIONS = Lists.newArrayList(Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST);
	private static final List<DiagonalDirection> DIAGONAL_DIRECTIONS = Lists.newArrayList(DiagonalDirection.NORTH_WEST,
			DiagonalDirection.NORTH_EAST, DiagonalDirection.SOUTH_WEST, DiagonalDirection.SOUTH_EAST);

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {

		if (player.getItemInHand(handIn).getItem() == ItemInit.WRENCH) {
			if (checkForAllRelayBlocks(worldIn, pos)) {
				player.displayClientMessage(new StringTextComponent("Their all RELAYS!!!!!"), true);

				/**
				 * If one of you could find some way of adding a delay between each individual
				 * relay block having their state changed, like this block is trying to
				 * "connect" to them or something, that woudl make this so much cooler.
				 */

				setAllRelayBlockPositions(worldIn, pos);

			} else {
				STRAIGHT_DIRECTIONS
						.stream().filter(
								dir -> !checkForRelay(worldIn, pos, dir))
						.findFirst()
						.ifPresent(direction -> player
								.displayClientMessage(new MachinaLangTextComponent("multiblock.rocket.missing_relay",
										MachinaLang.getDirectionName(direction)).toComponent(), true));

				DIAGONAL_DIRECTIONS
						.stream().filter(
								dir -> !checkForRelay(worldIn, pos, dir))
						.findFirst()
						.ifPresent(direction -> player
								.displayClientMessage(new MachinaLangTextComponent("multiblock.rocket.missing_relay",
										MachinaLang.getDirectionName(direction)).toComponent(), true));
			}

		}

		return ActionResultType.SUCCESS;
	}

	public boolean checkForAllRelayBlocks(World worldIn, BlockPos pos) {
		AtomicBoolean found = new AtomicBoolean(true);
		DIAGONAL_DIRECTIONS.forEach(dir -> {
			if (!checkForRelay(worldIn, pos, dir)) {
				found.set(false);
			}
		});
		STRAIGHT_DIRECTIONS.forEach(dir -> {
			if (!checkForRelay(worldIn, pos, dir)) {
				found.set(false);
			}
		});
		return found.get();
	}

	public boolean checkForRelay(World world, BlockPos pos, Direction straightDirection) {
		return checkForRelay(world, pos.relative(straightDirection, 7).above());
	}

	public boolean checkForRelay(World world, BlockPos pos, DiagonalDirection diagonalDirection) {
		return checkForRelay(world, diagonalDirection.relative(pos, 7).above());
	}

	public boolean checkForRelay(World world, BlockPos pos) {
		return world.getBlockState(pos).is(BlockInit.PAD_SIZE_RELAY);
	}

	public void setAllRelayBlockPositions(World worldIn, BlockPos pos) {
		BlockPos northBlockPos = pos.north(7).above();
		BlockPos eastBlockPos = pos.east(7).above();
		BlockPos southBlockPos = pos.south(7).above();
		BlockPos westBlockPos = pos.west(7).above();
		BlockPos northEastBlockPos = northBlockPos.east(7);
		BlockPos southEastBlockPos = southBlockPos.east(7);
		BlockPos southWestBlockPos = southBlockPos.west(7);
		BlockPos northWestBlockPos = northBlockPos.west(7);

		BlockState northRelayBlockState = worldIn.getBlockState(northBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.NORTH)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);
		BlockState northEastBlockState = worldIn.getBlockState(northEastBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.NORTHEAST)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);
		BlockState eastBlockState = worldIn.getBlockState(eastBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.EAST)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);
		BlockState southEastBlockState = worldIn.getBlockState(southEastBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.SOUTHEAST)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);
		BlockState southBlockState = worldIn.getBlockState(southBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.SOUTH)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);
		BlockState southWestBlockState = worldIn.getBlockState(southWestBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.SOUTHWEST)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);
		BlockState westBlockState = worldIn.getBlockState(westBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.WEST)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);
		BlockState northWestBlockState = worldIn.getBlockState(northWestBlockPos)
				.setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.NORTHWEST)
				.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.WAITING);

		worldIn.setBlock(northBlockPos, northRelayBlockState, 10);
		worldIn.setBlock(northEastBlockPos, northEastBlockState, 10);
		worldIn.setBlock(eastBlockPos, eastBlockState, 10);
		worldIn.setBlock(southEastBlockPos, southEastBlockState, 10);
		worldIn.setBlock(southBlockPos, southBlockState, 10);
		worldIn.setBlock(southWestBlockPos, southWestBlockState, 10);
		worldIn.setBlock(westBlockPos, westBlockState, 10);
		worldIn.setBlock(northWestBlockPos, northWestBlockState, 10);
	}
}
