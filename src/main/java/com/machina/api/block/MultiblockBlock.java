package com.machina.api.block;

import com.machina.api.block.entity.MultiblockMasterBlockEntity;
import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.api.util.block.BlockHelper;
import com.machina.client.screen.MultiblockHousingScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class MultiblockBlock extends MachineBlock implements IClickableBlock {

	public MultiblockBlock(Properties props) {
		super(props);
	}

	public abstract boolean isMaster();

	public boolean isFormed(Level world, BlockPos pos) {
		BlockEntity e = world.getBlockEntity(pos);
		if (e == null)
			return false;

		if (isMaster()) {
			if (!MultiblockMasterBlockEntity.class.isAssignableFrom(e.getClass())) {
				return false;
			}
			return ((MultiblockMasterBlockEntity) e).formed;
		} else {
			if (!MultiblockPartBlockEntity.class.isAssignableFrom(e.getClass())) {
				return false;
			}
			return ((MultiblockPartBlockEntity) e).formed;
		}
	}

	public ResourceLocation getMultiblock(Level world, BlockPos pos) {
		BlockEntity e = world.getBlockEntity(pos);
		if (e == null)
			return null;

		if (isMaster()) {
			if (!MultiblockMasterBlockEntity.class.isAssignableFrom(e.getClass())) {
				return null;
			}
			return ((MultiblockMasterBlockEntity) e).getMultiblock();
		} else {
			if (!MultiblockPartBlockEntity.class.isAssignableFrom(e.getClass())) {
				return null;
			}
			return ((MultiblockPartBlockEntity) e).getMultiblock();
		}
	}

	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
		super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
		if (pLevel.isClientSide() || pOldState.isAir() || pLevel.getBlockEntity(pPos) == null) {
			return;
		}
		refreshMultiblock(pLevel, pPos);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (level.isClientSide() || level.getBlockEntity(pos) == null) {
			return;
		}
		refreshMultiblock(level, pos);
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (isMaster()) {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockMasterBlockEntity.class, te -> {
				te.deform();
			});
		} else {
			BlockHelper.doWithTe(pLevel, pPos, MultiblockPartBlockEntity.class, te -> {
				te.update();
			});
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		if (isMaster()) {
			return super.getMenuProvider(state, level, pos);
		}
		BlockPos masterPos = BlockHelper.getFromTe(level, pos, MultiblockPartBlockEntity.class, be -> be.getMaster());
		if (masterPos != null && BlockHelper.doWithTe(level, masterPos, MultiblockMasterBlockEntity.class, te -> {
			// no-op: doWithTe returns true only if the BE exists and is the expected class
		})) {
			return super.getMenuProvider(state, level, masterPos);
		}

		return null;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult hitResult) {
		if (isFormed(level, pos)) {
			if (level.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				if (isMaster()) {
					return super.useWithoutItem(state, level, pos, player, hitResult);
				}

				BlockPos masterPos = BlockHelper.getFromTe(level, pos, MultiblockPartBlockEntity.class,
						be -> be.getMaster());
				if (masterPos != null
						&& BlockHelper.doWithTe(level, masterPos, MultiblockMasterBlockEntity.class, te -> {
							// no-op: just check existence/type on server
						})) {
					((ServerPlayer) player).openMenu(getMenuProvider(state, level, pos), masterPos);
					return InteractionResult.CONSUME;
				}
			}
		}

		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	private void refreshMultiblock(Level level, BlockPos pos) {
		if (isMaster()) {
			BlockHelper.doWithTe(level, pos, MultiblockMasterBlockEntity.class, te -> te.update());
		} else {
			BlockHelper.doWithTe(level, pos, MultiblockPartBlockEntity.class, te -> te.attemptAssimilate());
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (isFormed(level, pos)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (player.isShiftKeyDown()) {
			if (!level.isClientSide()) {
				return ItemInteractionResult.CONSUME;
			}
			ResourceLocation mb = getMultiblock(level, pos);
			if (mb != null) {
				Minecraft.getInstance().setScreen(new MultiblockHousingScreen(mb, getName()));
				return ItemInteractionResult.CONSUME;
			}
		}

		return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
	}
}
