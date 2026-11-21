package com.machina.api.block;

import java.util.List;

import com.machina.api.block.entity.MultiblockMasterBlockEntity;
import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.api.multiblock.Multiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.util.block.BlockHelper;
import com.machina.client.screen.MultiblockHousingScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.extensions.IBlockExtension;

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
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(state, level, pos, oldState, moving);
		if (level.isClientSide() || oldState.isAir() || level.getBlockEntity(pos) == null
				|| state.getBlock().equals(oldState.getBlock())) {
			return;
		}
		refreshMultiblock(level, pos);
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
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (isMaster()) {
			BlockHelper.doWithTe(level, pos, MultiblockMasterBlockEntity.class, te -> {
				te.deform(pos);
			});
		} else if (!newState.getBlock().equals(state.getBlock())) {
			BlockHelper.doWithTe(level, pos, MultiblockPartBlockEntity.class, te -> {
				te.update(pos);
			});
		}

		super.onRemove(state, level, pos, newState, moving);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, net.minecraft.world.level.material.FluidState fluid) {
		// Prevent breaking multiblock blocks while the machine is running (unless creative)
		if (!player.isCreative() && isMultiblockLocked(level, pos)) {
			return false;
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}

	private boolean isMultiblockLocked(Level level, BlockPos pos) {
		if (isMaster()) {
			return BlockHelper.getFromTe(level, pos, MultiblockMasterBlockEntity.class,
				MultiblockMasterBlockEntity::isMultiblockLocked) == Boolean.TRUE;
		} else {
			BlockPos masterPos = BlockHelper.getFromTe(level, pos, MultiblockPartBlockEntity.class,
				MultiblockPartBlockEntity::getMaster);
			if (masterPos != null) {
				return BlockHelper.getFromTe(level, masterPos, MultiblockMasterBlockEntity.class,
					MultiblockMasterBlockEntity::isMultiblockLocked) == Boolean.TRUE;
			}
		}
		return false;
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
		if (player.isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
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
			BlockHelper.doWithTe(level, pos, MultiblockMasterBlockEntity.class, te -> te.update(pos));
		} else {
			BlockHelper.doWithTe(level, pos, MultiblockPartBlockEntity.class, te -> te.attemptAssimilate(pos));
		}
	}

	protected ItemInteractionResult formedInteraction(Level level, BlockPos pos, Player player, InteractionHand hand,
			ItemStack stack) {
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (isFormed(level, pos)) {
			return formedInteraction(level, pos, player, hand, stack);
		}
		if (player.isShiftKeyDown()) {
			// Creative mode + stick = auto-build multiblock
			if (player.isCreative() && stack.is(Items.STICK) && isMaster()) {
				if (!level.isClientSide()) {
					ResourceLocation mbLoc = getMultiblock(level, pos);
					if (mbLoc != null) {
						buildMultiblock(level, pos, player, mbLoc);
					}
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}

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

	private void buildMultiblock(Level level, BlockPos controllerPos, Player player, ResourceLocation mbLoc) {
		Multiblock mb = MultiblockLoader.INSTANCE.get(mbLoc);
		if (mb == null) return;

		// Find controller position in structure (marked with !)
		Vec3i controllerStructurePos = mb.controller_render_pos;
		if (controllerStructurePos == null) return;

		// Determine rotation based on player facing
		Direction facing = player.getDirection().getOpposite();

		// Calculate corner position based on rotation
		BlockPos corner = calculateCorner(controllerPos, controllerStructurePos, mb.size, facing);

		// Place all blocks
		for (int x = 0; x < mb.size.getX(); x++) {
			for (int y = 0; y < mb.size.getY(); y++) {
				for (int z = 0; z < mb.size.getZ(); z++) {
					String key = mb.structure[x][y][z];

					// Skip air slots and controller slot
					if (key.equals(" ")) continue;
					if (key.equals("!")) continue; // Controller already placed

					// Calculate world position with rotation
					BlockPos worldPos = rotateAndOffset(corner, x, y, z, mb.size, facing);

					// Get block state to place
					BlockState toPlace = null;
					if (key.equals("?")) {
						// Optional controller slot - skip if not placing controller
						continue;
					} else {
						List<BlockState> states = mb.map.get(key);
						if (states != null && !states.isEmpty()) {
							toPlace = states.get(0);
						}
					}

					if (toPlace != null && !worldPos.equals(controllerPos)) {
						level.setBlock(worldPos, toPlace, 3);
					}
				}
			}
		}
	}

	private BlockPos calculateCorner(BlockPos controllerPos, Vec3i controllerStructurePos, Vec3i size, Direction facing) {
		int cx = controllerStructurePos.getX();
		int cy = controllerStructurePos.getY();
		int cz = controllerStructurePos.getZ();

		return switch (facing.get2DDataValue()) {
			case 1 -> // West
				controllerPos.offset(-cz, -cy, -(size.getX() - 1 - cx));
			case 2 -> // North
				controllerPos.offset(-(size.getX() - 1 - cx), -cy, -(size.getZ() - 1 - cz));
			case 3 -> // East
				controllerPos.offset(-(size.getZ() - 1 - cz), -cy, cx);
			default -> // South
				controllerPos.offset(-cx, -cy, -cz);
		};
	}

	private BlockPos rotateAndOffset(BlockPos corner, int x, int y, int z, Vec3i size, Direction facing) {
		return switch (facing.get2DDataValue()) {
			case 1 -> // West
				corner.offset(z, y, size.getX() - 1 - x);
			case 2 -> // North
				corner.offset(size.getX() - 1 - x, y, size.getZ() - 1 - z);
			case 3 -> // East
				corner.offset(size.getZ() - 1 - z, y, x);
			default -> // South
				corner.offset(x, y, z);
		};
	}
}
