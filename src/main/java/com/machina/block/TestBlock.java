package com.machina.block;

import com.machina.block.tile.machine.TestBE;
import com.machina.util.helper.SoundHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TestBlock extends BaseEntityBlock {

	public TestBlock(Properties p) {
		super(p);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TestBE(pos, state);
	}

	@Override
	public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity tank = level.getBlockEntity(pos);
			if (tank != null) {
				IFluidHandler handler = tank.getCapability(ForgeCapabilities.FLUID_HANDLER, hit.getDirection())
						.orElse(null);
				if (FluidUtil.interactWithFluidHandler(player, hand, handler)) {
					if (player instanceof ServerPlayer)
						SoundHelper.playSoundFromServer((ServerPlayer) player, SoundEvents.BUCKET_FILL);
				}
			}

			if (FluidUtil.getFluidHandler(player.getItemInHand(hand)).isPresent())
				return InteractionResult.SUCCESS;
//			player.openMenu(tank);
		}
		return InteractionResult.SUCCESS;
	}
}
