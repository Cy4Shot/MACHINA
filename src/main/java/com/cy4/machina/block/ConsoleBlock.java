package com.cy4.machina.block;

import com.cy4.machina.api.util.DiagonalDirection;
import com.cy4.machina.block.properties.ActivationState;
import com.cy4.machina.block.properties.RelayPosState;
import com.cy4.machina.init.BlockInit;
import com.cy4.machina.init.ItemInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
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

	public ConsoleBlock(Properties properties) {
		super(properties);
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
				setOffRelayConnectionProcess(player, state, worldIn, pos);
			}
		}
			
		return ActionResultType.SUCCESS;
	}

	/**
	 * 
	 * Trying to get the Console Block to activate the "connectingToAllRelays" method in the RocketMountClass
	 */
	
	public void setOffRelayConnectionProcess(PlayerEntity player, BlockState state, World world, BlockPos pos) {
		//BlockPos relativePos = pos.relative(straightDirection, 7).above();
		//RocketMount.connectingToAllRelays(state, world, pos.relative(Direction.NORTH, 7).below(2));
		//returnProposedCenterBlock(player, world, pos, Direction.NORTH);
		
		if (checkForCenter(world, pos, Direction.NORTH)) {
			setOffRelayConnection(state, world, pos, Direction.NORTH);
		}
	}
	
	public void returnProposedCenterBlock(PlayerEntity player, World world, BlockPos pos, Direction straightDirection) {
		if (checkForCenter(world, pos, straightDirection)) {
			player.displayClientMessage(new StringTextComponent("It's a Rocket Mount"), true);
		} else {
			player.displayClientMessage(new StringTextComponent("It's NOT a Rocket Mount"), true);
		}
	}
	
	public void setOffRelayConnection(BlockState state, World world, BlockPos pos, Direction straightDirection) {
		BlockPos relativePos = pos.relative(straightDirection, 7).below(2);
		
		PlayerEntity player = Minecraft.getInstance().player;
		player.displayClientMessage(new StringTextComponent(relativePos.toString()), true);
		//RocketMount.connectingToAllRelays(state, world, relativePos);
	}
	
	
	
	public boolean checkForCenter(World world, BlockPos pos, Direction straightDirection) {
		return checkForCenter(world, pos.relative(straightDirection, 7).below(2));
	}

	public boolean checkForCenter(World world, BlockPos pos, DiagonalDirection diagonalDirection) {
		return checkForCenter(world, diagonalDirection.relative(pos, 7).below(2));
	}

	public boolean checkForCenter(World world, BlockPos pos) {
		return world.getBlockState(pos).is(BlockInit.ROCKET_MOUNT);
	}
	
	
	
	
	
	
	


	/**************************************************************************************************************************************************
	 * UTIL METHODS/VARIABLES
	 **************************************************************************************************************************************************/
	
	
	public boolean isConfiguredRelayBelow(World worldIn, BlockPos posIn, PlayerEntity player) {
		if (getRelayPosition(worldIn, posIn) != RelayPosState.N_A) {
			player.displayClientMessage(new StringTextComponent(worldIn.getBlockState(posIn.below()).getValue(PadSizeRelay.RELAY_POS_STATE).name()), true);
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
		if (worldIn.getBlockState(posIn.below()).is(BlockInit.PAD_SIZE_RELAY)) {
			return true;
		} else {
			return false;
		}
	}
}
