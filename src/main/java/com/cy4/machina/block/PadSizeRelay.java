package com.cy4.machina.block;

import javax.swing.Timer;

import com.cy4.machina.block.properties.ActivationState;
import com.cy4.machina.block.properties.RelayPosState;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class PadSizeRelay extends Block {
	public static final EnumProperty<ActivationState> ACTIVATION_STATE = EnumProperty.create("activation_state", ActivationState.class, ActivationState.NOT_ACTIVE,
			ActivationState.WAITING, ActivationState.ACTIVE);

	public static final EnumProperty<RelayPosState> RELAY_POS_STATE = EnumProperty.create("relay_pos_state", RelayPosState.class, RelayPosState.N_A, RelayPosState.NORTH,
			RelayPosState.NORTHEAST, RelayPosState.EAST, RelayPosState.SOUTHEAST, RelayPosState.SOUTH, RelayPosState.SOUTHWEST, RelayPosState.WEST, RelayPosState.NORTHWEST, RelayPosState.CENTER);




	public PadSizeRelay(Properties properties) {
		super(properties);
		this.registerDefaultState(stateDefinition.any()
				.setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE)
				.setValue(RELAY_POS_STATE, RelayPosState.N_A));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(ACTIVATION_STATE, RELAY_POS_STATE);
	}



	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		//Simple used for testing as a way to reset a relay bloc to position N_A and getting the current position value
		if (player.isCrouching()) {
			int i = 0;
			changeRelayPos(pos, worldIn, i);
		} else {
			player.displayClientMessage(new StringTextComponent(state.getValue(RELAY_POS_STATE).toString()), true);
			//setActive(pos, worldIn);
			isConnectingAnimation(pos, worldIn);
		}


		return ActionResultType.SUCCESS;
	}

	public void changeRelayPos(BlockPos pos, World world, int newRelayPos) {
		BlockState state = world.getBlockState(pos);

		if (newRelayPos == 0) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.N_A).setValue(ACTIVATION_STATE, ActivationState.NOT_ACTIVE));
		} else if (newRelayPos == 1) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.NORTH));
		} else if (newRelayPos == 2) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.NORTHEAST));
		} else if (newRelayPos == 3) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.EAST));
		} else if (newRelayPos == 4) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.SOUTHEAST));
		} else if (newRelayPos == 5) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.SOUTH));
		} else if (newRelayPos == 6) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.SOUTHWEST));
		} else if (newRelayPos == 7) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.WEST));
		} else if (newRelayPos == 8) {
			world.setBlockAndUpdate(pos, state.setValue(RELAY_POS_STATE, RelayPosState.NORTHWEST));
		}

	}


	public void setActive(BlockPos pos, World world) {
		BlockState state = world.getBlockState(pos);
		world.setBlockAndUpdate(pos, state.setValue(ACTIVATION_STATE, ActivationState.ACTIVE));
	}




	public static void isConnectingAnimation(BlockPos pos, World worldIn) {
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
