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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

package com.machina.modules.rocket.block;

import java.util.List;

import javax.swing.Timer;

import com.google.common.collect.Lists;
import com.machina.api.util.DiagonalDirection;
import com.machina.block.properties.ActivationState;
import com.machina.block.properties.RelayPosState;
import com.machina.init.ItemInit;
import com.machina.modules.rocket.RocketModule;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class ConsoleBlock extends Block {

	public static final DirectionProperty FACING = HorizontalBlock.FACING;

	public ConsoleBlock() {
		super(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE).noOcclusion());
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection());
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if (player.getItemInHand(handIn).getItem() == ItemInit.WRENCH) {
			if (isConfiguredRelayBelow(worldIn, pos, player)) {
				relayConnectionProcess(player, state, worldIn, pos);
			}
		}
			
		return ActionResultType.SUCCESS;
	}

	
	
	public void relayConnectionProcess(PlayerEntity player, BlockState state, World world, BlockPos pos) {
		BlockPos rocketMountPosFromNorth = pos.relative(Direction.SOUTH, 7).below(2);
		
		if (isConfiguredRelayBelow(world, pos, player)) {
			if (getRelayPosition(world, pos) == RelayPosState.NORTH) {
				if (rocketMountRelayCheck(world, pos, player, Direction.SOUTH)) {
					relativeRelayConnectionAnimationChain(rocketMountPosFromNorth, world);
				}
			} else if (getRelayPosition(world, pos) == RelayPosState.NORTHEAST) {
				if (rocketMountRelayCheck(world, pos, player, DiagonalDirection.SOUTH_WEST)) {
					//player.displayClientMessage(new StringTextComponent("Sent From NorthEast Console"), false);
				}
			} else if (getRelayPosition(world, pos) == RelayPosState.EAST) {
				if (rocketMountRelayCheck(world, pos, player, Direction.WEST)) {
					//player.displayClientMessage(new StringTextComponent("Sent From East Console"), false);
				}
			} else if (getRelayPosition(world, pos) == RelayPosState.SOUTHEAST) {
				if (rocketMountRelayCheck(world, pos, player, DiagonalDirection.NORTH_WEST)) {
					//player.displayClientMessage(new StringTextComponent("Sent From SouthEast Console"), false);
				}
			} else if (getRelayPosition(world, pos) == RelayPosState.SOUTH) {
				if (rocketMountRelayCheck(world, pos, player, Direction.NORTH)) {
					//player.displayClientMessage(new StringTextComponent("Sent From South Console"), false);
				}
			} else if (getRelayPosition(world, pos) == RelayPosState.SOUTHWEST) {
				if (rocketMountRelayCheck(world, pos, player, DiagonalDirection.NORTH_EAST)) {
					//player.displayClientMessage(new StringTextComponent("Sent From SouthWest Console"), false);
				}
			} else if (getRelayPosition(world, pos) == RelayPosState.WEST) {
				if (rocketMountRelayCheck(world, pos, player, Direction.EAST)) {
					//player.displayClientMessage(new StringTextComponent("Sent From West Console"), false);
				}
			} else if (getRelayPosition(world, pos) == RelayPosState.NORTHWEST) {
				if (rocketMountRelayCheck(world, pos, player, DiagonalDirection.SOUTH_EAST)) {
					//player.displayClientMessage(new StringTextComponent("Sent From NorthWest Console"), false);
				}
			}
		}
	}
	
	public void relativeRelayConnectionAnimationChain(BlockPos posIn, World worldIn) {
		BlockPos northRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, Direction.NORTH);
		BlockPos northEastRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, DiagonalDirection.NORTH_EAST);
		BlockPos eastRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, Direction.EAST);
		BlockPos southEastRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, DiagonalDirection.SOUTH_EAST);
		BlockPos southRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, Direction.SOUTH);
		BlockPos southWestEastRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, DiagonalDirection.SOUTH_WEST);
		BlockPos westRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, Direction.WEST);
		BlockPos northWestEastRelayRelativeToRocketMount = RocketMount.returnRelayPos(posIn, worldIn, DiagonalDirection.NORTH_WEST);
		
		//resetAllRelayBlocks(worldIn, posIn);

		Timer timer1 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(northRelayRelativeToRocketMount, worldIn));
		timer1.setInitialDelay(1000); timer1.setRepeats(false); timer1.start();
		Timer timer2 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(northEastRelayRelativeToRocketMount, worldIn));
		timer2.setInitialDelay(9000); timer2.setRepeats(false); timer2.start();
		Timer timer3 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(eastRelayRelativeToRocketMount, worldIn));
		timer3.setInitialDelay(17000); timer3.setRepeats(false); timer3.start();
		Timer timer4 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(southEastRelayRelativeToRocketMount, worldIn));
		timer4.setInitialDelay(25000); timer4.setRepeats(false); timer4.start();
		Timer timer5 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(southRelayRelativeToRocketMount, worldIn));
		timer5.setInitialDelay(33000); timer5.setRepeats(false); timer5.start();
		Timer timer6 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(southWestEastRelayRelativeToRocketMount, worldIn));
		timer6.setInitialDelay(41000); timer6.setRepeats(false); timer6.start();
		Timer timer7 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(westRelayRelativeToRocketMount, worldIn));
		timer7.setInitialDelay(49000); timer7.setRepeats(false); timer7.start();
		Timer timer8 = new Timer(0, actionEvent -> PadSizeRelay.isConnectingAnimation(northWestEastRelayRelativeToRocketMount, worldIn));
		timer8.setInitialDelay(57000); timer8.setRepeats(false); timer8.start();
	}
	
	private static final List<Direction> STRAIGHT_DIRECTIONS = Lists.newArrayList(Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST);

	private static final List<DiagonalDirection> DIAGONAL_DIRECTIONS = Lists.newArrayList(DiagonalDirection.NORTH_WEST,
			DiagonalDirection.NORTH_EAST, DiagonalDirection.SOUTH_WEST, DiagonalDirection.SOUTH_EAST);
	
	public static void resetAllRelayBlocks(World worldIn, BlockPos pos) {
		STRAIGHT_DIRECTIONS.forEach(direction -> {
			BlockPos newPos = pos.relative(direction, 7).above();
			BlockState state = worldIn.getBlockState(newPos).setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.NOT_ACTIVE);

			worldIn.setBlockAndUpdate(newPos, state);
		});
		DIAGONAL_DIRECTIONS.forEach(direction -> {
			BlockPos newPos = direction.relative(pos, 7).above();
			BlockState state = worldIn.getBlockState(newPos).setValue(PadSizeRelay.ACTIVATION_STATE, ActivationState.NOT_ACTIVE);

			worldIn.setBlockAndUpdate(newPos, state);
		});
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	


	/**************************************************************************************************************************************************
	 * UTIL METHODS/VARIABLES
	 **************************************************************************************************************************************************/
	
	public boolean rocketMountRelayCheck(World worldIn, BlockPos pos, PlayerEntity player, Direction straightDirection) {
		BlockPos relativePos = pos.relative(straightDirection, 7).below(2);
		
		if (RocketMount.checkForAllRelayBlocks(worldIn, relativePos) == true) {
			player.displayClientMessage(new StringTextComponent("All Relays Are Present"), true);
			return true;
		} else {
			player.displayClientMessage(new StringTextComponent("Either a Relay is missing, or the method's busted"), true);
			return false;
		}
	}
	
	public boolean rocketMountRelayCheck(World worldIn, BlockPos pos, PlayerEntity player, DiagonalDirection diagonalDirection) {
		BlockPos relativePos = diagonalDirection.relative(pos, 7).below(2);
		
		if (RocketMount.checkForAllRelayBlocks(worldIn, relativePos) == true) {
			player.displayClientMessage(new StringTextComponent("All Relays Are Present"), true);
			return true;
		} else {
			player.displayClientMessage(new StringTextComponent("Either a Relay is missing, or the method's busted"), true);
			return false;
		}
	}
	
	
	
	
	
	public boolean isConfiguredRelayBelow(World worldIn, BlockPos posIn, PlayerEntity player) {
		if (getRelayPosition(worldIn, posIn) != RelayPosState.N_A) {
			return true;
		} else {
			player.displayClientMessage(new StringTextComponent("No Relay Below or an Unpowered One"), true);
			return false;
		}
	}
	
	public RelayPosState getRelayPosition(World worldIn, BlockPos posIn) {
		if (checkForRelayBelow(worldIn, posIn)) {
			return worldIn.getBlockState(posIn.below()).getValue(PadSizeRelay.RELAY_POS_STATE);
		} else {
			return RelayPosState.N_A;
		}
	}
	
	public boolean checkForRelayBelow(World worldIn, BlockPos posIn) {
		if (worldIn.getBlockState(posIn.below()).is(RocketModule.PAD_SIZE_RELAY_BLOCK)) {
			return true;
		} else {
			return false;
		}
	}
}
