package com.machina.block.machine;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.TankBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TankBlock extends MachineBlock {

	public TankBlock(Properties props) {
		super(props.noOcclusion().isRedstoneConductor(TankBlock::never).isSuffocating(TankBlock::never)
				.isViewBlocking(TankBlock::never));
	}

	public static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
		return false;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.TANK.get();
	}

	@Override
	protected boolean isTickable() {
		return true;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return TankBlockEntity.class;
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
			@NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult res) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty()) {
			if (level.getBlockEntity(pos) instanceof TankBlockEntity tank) {
				if (level.isClientSide) {
					return InteractionResult.SUCCESS;
				}

				if (tank.clicked((ServerPlayer) player, hand, stack)) {
					player.getInventory().setChanged();
					return InteractionResult.CONSUME;
				}
			}
		}

		return super.use(state, level, pos, player, hand, res);
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(TankBlock::new);
	}
}