package com.machina.block.machine;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.TankBlockEntity;
import com.machina.block.menu.TankMenu;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.IItemHandler;

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
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!stack.isEmpty()) {
			if (level.getBlockEntity(pos) instanceof TankBlockEntity tank) {
				if (level.isClientSide) {
					return ItemInteractionResult.SUCCESS;
				}

				if (tank.clicked((ServerPlayer) player, hand, stack)) {
					player.getInventory().setChanged();
					return ItemInteractionResult.CONSUME;
				}
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(TankBlock::new);
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return TankMenu::new;
	}
}