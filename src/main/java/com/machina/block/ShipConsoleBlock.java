package com.machina.block;

import com.machina.block.tile.base.ShipConsoleTileEntity;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ShipConsoleBlock extends Block {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public static final VoxelShape SHAPE_BASE_N = Block.box(0.0D, 0.0D, 2.0D, 16.0D, 1.0D, 15.0D);
	public static final VoxelShape SHAPE_BASE_W = Block.box(2.0D, 0.0D, 0.0D, 15.0D, 1.0D, 16.0D);
	public static final VoxelShape SHAPE_BASE_S = Block.box(0.0D, 0.0D, 1.0D, 16.0D, 1.0D, 14.0D);
	public static final VoxelShape SHAPE_BASE_E = Block.box(1.0D, 0.0D, 0.0D, 14.0D, 1.0D, 16.0D);
	public static final VoxelShape SHAPE_POST = Block.box(6.0D, 2.0D, 6.0D, 10.0D, 14.0D, 10.0D);
	public static final VoxelShape SHAPE_COLLISION = VoxelShapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D),
			SHAPE_POST, Block.box(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D));
	public static final VoxelShape SHAPE_WEST = VoxelShapes.or(Block.box(1.0D, 11.0D, 0.0D, 5.333333D, 15.0D, 16.0D),
			Block.box(5.333333D, 13.0D, 0.0D, 9.666667D, 17.0D, 16.0D),
			Block.box(9.666667D, 15.0D, 0.0D, 14.0D, 19.0D, 16.0D), SHAPE_BASE_W, SHAPE_POST);
	public static final VoxelShape SHAPE_NORTH = VoxelShapes.or(Block.box(0.0D, 11.0D, 1.0D, 16.0D, 15.0D, 5.333333D),
			Block.box(0.0D, 13.0D, 5.333333D, 16.0D, 17.0D, 9.666667D),
			Block.box(0.0D, 15.0D, 9.666667D, 16.0D, 19.0D, 14.0D), SHAPE_BASE_N, SHAPE_POST);
	public static final VoxelShape SHAPE_EAST = VoxelShapes.or(Block.box(15.0D, 11.0D, 0.0D, 10.666667D, 15.0D, 16.0D),
			Block.box(10.666667D, 13.0D, 0.0D, 6.333333D, 17.0D, 16.0D),
			Block.box(6.333333D, 15.0D, 0.0D, 2.0D, 19.0D, 16.0D), SHAPE_BASE_E, SHAPE_POST);
	public static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(Block.box(0.0D, 11.0D, 15.0D, 16.0D, 15.0D, 10.666667D),
			Block.box(0.0D, 13.0D, 10.666667D, 16.0D, 17.0D, 6.333333D),
			Block.box(0.0D, 15.0D, 6.333333D, 16.0D, 19.0D, 2.0D), SHAPE_BASE_S, SHAPE_POST);

	public ShipConsoleBlock() {
		super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.noOcclusion().sound(SoundType.METAL));

		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
		switch ((Direction) pState.getValue(FACING)) {
		case NORTH:
			return SHAPE_NORTH;
		case SOUTH:
			return SHAPE_SOUTH;
		case EAST:
			return SHAPE_EAST;
		case WEST:
			return SHAPE_WEST;
		default:
			return SHAPE_POST;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos,
			ISelectionContext pContext) {
		return SHAPE_COLLISION;
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.SHIP_CONSOLE.get().create();
	}

	@Override
	public ActionResultType use(BlockState pState, World level, BlockPos pos, PlayerEntity player, Hand pHand,
			BlockRayTraceResult pHit) {
		if (!level.isClientSide()) {
			TileEntity te = level.getBlockEntity(pos);
			if (te instanceof ShipConsoleTileEntity)
				NetworkHooks.openGui((ServerPlayerEntity) player, (ShipConsoleTileEntity) te, pos);
		}
		return ActionResultType.SUCCESS;
	}

}
