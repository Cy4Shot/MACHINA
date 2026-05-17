package com.machina.block.machine.fission_reactor;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.fission_reactor.FissionFuelRodAssemblyBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.items.IItemHandler;

public class FissionFuelRodAssemblyBlock extends FissionFuelRodBlock {

	public FissionFuelRodAssemblyBlock(Properties props) {
		super(props);
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return FissionFuelRodAssemblyBlockEntity.class;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.FISSION_FUEL_ROD_ASSEMBLY.get();
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return null;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(FissionFuelRodAssemblyBlock::new);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
			BlockPos currentPos, BlockPos facingPos) {
		return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).getBlock() instanceof FissionFuelRodBlock;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
			FluidState fluid) {
		if (player.isCrouching() && player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.STICK)) {
			if (!level.isClientSide()) {
				BlockHelper.doWithTe(level, pos, FissionFuelRodAssemblyBlockEntity.class,
						FissionFuelRodAssemblyBlockEntity::decrementStage);
			}
			return false;
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}

	@Override
	protected ItemInteractionResult formedInteraction(Level level, BlockPos pos, Player player, InteractionHand hand,
			ItemStack stack) {

		if (stack.is(Items.STICK)) { // TODO: Wrench
			if (!level.isClientSide) {
				BlockHelper.getFromTe(level, pos, FissionFuelRodAssemblyBlockEntity.class,
						FissionFuelRodAssemblyBlockEntity::incrementStage);
			} else {
				this.spawnDestroyParticles(level, player, pos, level.getBlockState(pos));
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.formedInteraction(level, pos, player, hand, stack);
	}
}
