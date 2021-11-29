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

package com.machina.modules.rocket.block;

import javax.swing.Timer;

import com.machina.api.util.DiagonalDirection;
import com.machina.block.properties.ActivationState;
import com.machina.block.properties.RelayPosState;
import com.machina.init.ItemInit;
import com.machina.modules.rocket.RocketModule;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class PadSizeRelay extends Block {
	public static final EnumProperty<ActivationState> ACTIVATION_STATE = EnumProperty.create("activation_state", ActivationState.class, ActivationState.UN_POWERED, ActivationState.NOT_ACTIVE,
			ActivationState.WAITING, ActivationState.ACTIVE);

	public static final EnumProperty<RelayPosState> RELAY_POS_STATE = EnumProperty.create("relay_pos_state", RelayPosState.class, RelayPosState.N_A, RelayPosState.NORTH,
			RelayPosState.NORTHEAST, RelayPosState.EAST, RelayPosState.SOUTHEAST, RelayPosState.SOUTH, RelayPosState.SOUTHWEST, RelayPosState.WEST, RelayPosState.NORTHWEST, RelayPosState.CENTER);

	public PadSizeRelay() {
		super(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));
		this.registerDefaultState(stateDefinition.any()
				.setValue(ACTIVATION_STATE, ActivationState.UN_POWERED)
				.setValue(RELAY_POS_STATE, RelayPosState.N_A));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(ACTIVATION_STATE, RELAY_POS_STATE);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if (player.getItemInHand(handIn).getItem() == ItemInit.WRENCH) {
			determineRelayPosAndActivate(player, state, worldIn, pos);
		}
			
		return ActionResultType.SUCCESS;
	}
	
	
	
	
	
	/**************************************************************************************************************************************************
	 * UTIL METHODS/VARIABLES
	 **************************************************************************************************************************************************/

	public void determineRelayPosAndActivate(PlayerEntity player, BlockState state, World world, BlockPos pos) {
		if (checkForCenter(world, pos, Direction.NORTH)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.SOUTH).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (checkForCenter(world, pos, DiagonalDirection.NORTH_EAST)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.SOUTHWEST).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (checkForCenter(world, pos, Direction.EAST)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.WEST).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (checkForCenter(world, pos, DiagonalDirection.SOUTH_EAST)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.NORTHWEST).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (checkForCenter(world, pos, Direction.SOUTH)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.NORTH).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (checkForCenter(world, pos, DiagonalDirection.SOUTH_WEST)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.NORTHEAST).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (checkForCenter(world, pos, Direction.WEST)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.EAST).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (checkForCenter(world, pos, DiagonalDirection.NORTH_WEST)) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.SOUTHEAST).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else {
			player.displayClientMessage(new StringTextComponent("This Relay Is Not in a Valid Position"), true);
		}
	}
	
	public boolean checkForCenter(World world, BlockPos pos, Direction straightDirection) {
		return checkForCenter(world, pos.relative(straightDirection, 7).below());
	}

	public boolean checkForCenter(World world, BlockPos pos, DiagonalDirection diagonalDirection) {
		return checkForCenter(world, diagonalDirection.relative(pos, 7).below());
	}

	public boolean checkForCenter(World world, BlockPos pos) {
		return world.getBlockState(pos).is(RocketModule.ROCKET_MOUNT_BLOCK);
	}

	public static void isConnectingAnimation(BlockPos pos, World worldIn) {
		BlockState state1 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE);
		BlockState state2 = worldIn.getBlockState(pos).setValue(ACTIVATION_STATE, ActivationState.WAITING);

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
