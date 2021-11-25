/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
		this.registerDefaultState(stateDefinition.any().setValue(ACTIVATION_STATE, ActivationState.UN_POWERED)
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
	
	
	
	
	
	
	
	public static BlockPos returnRelayPos(BlockPos pos, World worldIn, Direction straightDirection) {
		BlockPos relativePos = pos.relative(straightDirection, 7).above();
		return relativePos;
	}
	
	public static BlockPos returnRelayPos(BlockPos pos, World worldIn, DiagonalDirection diagonalDirection) {
		BlockPos relativePos = diagonalDirection.relative(pos, 7).above();
		return relativePos;
	}
	
	

	/**************************************************************************************************************************************************
	 * UTIL METHODS/VARIABLES
	 **************************************************************************************************************************************************/

	private static final List<Direction> STRAIGHT_DIRECTIONS = Lists.newArrayList(Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST);

	private static final List<DiagonalDirection> DIAGONAL_DIRECTIONS = Lists.newArrayList(DiagonalDirection.NORTH_WEST,
			DiagonalDirection.NORTH_EAST, DiagonalDirection.SOUTH_WEST, DiagonalDirection.SOUTH_EAST);

	public static boolean checkForAllRelayBlocks(World worldIn, BlockPos pos) {
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

	public static boolean checkForRelay(World world, BlockPos pos, Direction straightDirection) {
		return checkForRelay(world, pos.relative(straightDirection, 7).above());
	}

	public static boolean checkForRelay(World world, BlockPos pos, DiagonalDirection diagonalDirection) {
		return checkForRelay(world, diagonalDirection.relative(pos, 7).above());
	}

	public static boolean checkForRelay(World world, BlockPos pos) {
		return world.getBlockState(pos).is(BlockInit.PAD_SIZE_RELAY) && world.getBlockState(pos).getValue(PadSizeRelay.ACTIVATION_STATE) == ActivationState.NOT_ACTIVE;
	}

	

	public void isConnectingAnimation(BlockPos pos, World worldIn) {
		BlockState state1 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE);
		BlockState state2 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.WAITING);
		//BlockState state3 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.ACTIVE);

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
		Timer timer7 = new Timer(7000, actionEvent -> worldIn.setBlockAndUpdate(pos, state2));
		timer7.setRepeats(false);
		timer7.start();
	}
}
