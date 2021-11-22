package com.cy4.machina.block;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.Timer;

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
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class RocketMount extends Block {

	public static final EnumProperty<ActivationState> ACTIVATION_STATE = EnumProperty.create("activation_state",
			ActivationState.class, ActivationState.values());

	public static final EnumProperty<RelayPosState> RELAY_POS_STATE = EnumProperty.create("relay_pos_state",
			RelayPosState.class, RelayPosState.values());

	public RocketMount(Properties properties) {
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE)
				.setValue(RELAY_POS_STATE, RelayPosState.N_A));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(ACTIVATION_STATE, RELAY_POS_STATE);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {

		if (player.getItemInHand(handIn).getItem() == ItemInit.WRENCH) {
			if (checkForAllRelayBlocks(worldIn, pos)) {

				resetAllRelayBlockPositions(worldIn, pos);
				worldIn.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.CENTER)
						.setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));

				Timer timer1 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, Direction.NORTH));
				timer1.setInitialDelay(1000);
				timer1.setRepeats(false);
				timer1.start();
				Timer timer2 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, DiagonalDirection.NORTH_EAST));
				timer2.setInitialDelay(9000);
				timer2.setRepeats(false);
				timer2.start();
				Timer timer3 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, Direction.EAST));
				timer3.setInitialDelay(17000);
				timer3.setRepeats(false);
				timer3.start();
				Timer timer4 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, DiagonalDirection.SOUTH_EAST));
				timer4.setInitialDelay(25000);
				timer4.setRepeats(false);
				timer4.start();
				Timer timer5 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, Direction.SOUTH));
				timer5.setInitialDelay(33000);
				timer5.setRepeats(false);
				timer5.start();
				Timer timer6 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, DiagonalDirection.SOUTH_WEST));
				timer6.setInitialDelay(41000);
				timer6.setRepeats(false);
				timer6.start();
				Timer timer7 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, Direction.WEST));
				timer7.setInitialDelay(49000);
				timer7.setRepeats(false);
				timer7.start();
				Timer timer8 = new Timer(0, actionEvent -> connectToRelay(pos, worldIn, DiagonalDirection.NORTH_WEST));
				timer8.setInitialDelay(57000);
				timer8.setRepeats(false);
				timer8.start();

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

	public void connectToRelay(BlockPos pos, World worldIn, Direction straightDirection) {
		BlockPos relativePos = pos.relative(straightDirection, 7).above();

		// isConnectingAnimation(pos, worldIn);
		PadSizeRelay.isConnectingAnimation(relativePos, worldIn);

		if (straightDirection == Direction.NORTH) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.NORTH));
		} else if (straightDirection == Direction.EAST) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.EAST));
		} else if (straightDirection == Direction.SOUTH) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.SOUTH));
		} else if (straightDirection == Direction.WEST) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.WEST));
		}
	}

	public void connectToRelay(BlockPos pos, World worldIn, DiagonalDirection diagonalDirection) {
		BlockPos relativePos = diagonalDirection.relative(pos, 7).above();

		// isConnectingAnimation(pos, worldIn);
		PadSizeRelay.isConnectingAnimation(relativePos, worldIn);

		if (diagonalDirection == DiagonalDirection.NORTH_EAST) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.NORTHEAST));
		} else if (diagonalDirection == DiagonalDirection.SOUTH_EAST) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.SOUTHEAST));
		} else if (diagonalDirection == DiagonalDirection.SOUTH_WEST) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.SOUTHWEST));
		} else if (diagonalDirection == DiagonalDirection.NORTH_WEST) {
			worldIn.setBlockAndUpdate(relativePos,
					worldIn.getBlockState(relativePos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.NORTHWEST));
		}
	}

	/**************************************************************************************************************************************************
	 * UTIL METHODS/VARIABLES
	 **************************************************************************************************************************************************/

	private static final List<Direction> STRAIGHT_DIRECTIONS = Lists.newArrayList(Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST);

	private static final List<DiagonalDirection> DIAGONAL_DIRECTIONS = Lists.newArrayList(DiagonalDirection.NORTH_WEST,
			DiagonalDirection.NORTH_EAST, DiagonalDirection.SOUTH_WEST, DiagonalDirection.SOUTH_EAST);

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

	public void resetAllRelayBlockPositions(World worldIn, BlockPos pos) {
		STRAIGHT_DIRECTIONS.forEach(direction -> {
			BlockPos newPos = pos.relative(direction, 7).above();
			BlockState state = worldIn.getBlockState(newPos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.N_A)
					.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.NOT_ACTIVE);

			worldIn.setBlockAndUpdate(newPos, state);
		});
		DIAGONAL_DIRECTIONS.forEach(direction -> {
			BlockPos newPos = direction.relative(pos, 7).above();
			BlockState state = worldIn.getBlockState(newPos).setValue(PadSizeRelay.RELAY_POS_STATE, RelayPosState.N_A)
					.setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.NOT_ACTIVE);

			worldIn.setBlockAndUpdate(newPos, state);
		});
	}

	public void isConnectingAnimation(BlockPos pos, World worldIn) {
		BlockState state1 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE);
		BlockState state2 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.WAITING);
		BlockState state3 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.ACTIVE);

		Timer timer1 = new Timer(1000, actionEvent -> worldIn.setBlockAndUpdate(pos, state2));
		timer1.setRepeats(false);
		timer1.start();
		Timer timer2 = new Timer(2000, actionEvent -> worldIn.setBlockAndUpdate(pos, state1));
		timer2.setRepeats(false);
		timer2.start();
		Timer timer3 = new Timer(3000, actionEvent -> worldIn.setBlockAndUpdate(pos, state2));
		timer3.setRepeats(false);
		timer3.start();
		Timer timer4 = new Timer(4000, actionEvent -> worldIn.setBlockAndUpdate(pos, state1));
		timer4.setRepeats(false);
		timer4.start();
		Timer timer5 = new Timer(5000, actionEvent -> worldIn.setBlockAndUpdate(pos, state2));
		timer5.setRepeats(false);
		timer5.start();
		Timer timer6 = new Timer(6000, actionEvent -> worldIn.setBlockAndUpdate(pos, state1));
		timer6.setRepeats(false);
		timer6.start();
		Timer timer7 = new Timer(7000, actionEvent -> worldIn.setBlockAndUpdate(pos, state3));
		timer7.setRepeats(false);
		timer7.start();
	}
}
