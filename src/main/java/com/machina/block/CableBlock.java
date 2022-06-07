package com.machina.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.machina.block.tile.CableTileEntity;
import com.machina.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.math.MathUtil;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

// https://github.com/owmii/Powah/blob/e9bf8bda0063cde604e6e1c3beb5eba6197e1b49/src/main/java/owmii/powah/block/cable/CableBlock.java
public class CableBlock extends Block {

	public static final BooleanProperty NORTH = SixWayBlock.NORTH;
	public static final BooleanProperty EAST = SixWayBlock.EAST;
	public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
	public static final BooleanProperty WEST = SixWayBlock.WEST;
	public static final BooleanProperty UP = SixWayBlock.UP;
	public static final BooleanProperty DOWN = SixWayBlock.DOWN;
	public static final BooleanProperty MIDDLE = BooleanProperty.create("middle");
	public static final BooleanProperty TILE = BooleanProperty.create("tile");

	private static final VoxelShape PART_C = Block.box(6, 6, 6, 10, 10, 10);
	private static final VoxelShape PART_M = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);
	private static final VoxelShape PART_N = Block.box(6.5, 6.5, 0, 9.5, 9.5, 7);
	private static final VoxelShape PART_E = Block.box(9.5, 6.5, 6.5, 16, 9.5, 9.5);
	private static final VoxelShape PART_S = Block.box(6.5, 6.5, 9.5, 9.5, 9.5, 16);
	private static final VoxelShape PART_W = Block.box(0, 6.5, 6.5, 6.5, 9.5, 9.5);
	private static final VoxelShape PART_U = Block.box(6.5, 9.5, 6.5, 9.5, 16, 9.5);
	private static final VoxelShape PART_D = Block.box(6.5, 0, 6.5, 9.5, 7, 9.5);

	private static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

	public CableBlock() {
		super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).harvestLevel(1).strength(1f)
				.noOcclusion());

		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false)
				.setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false)
				.setValue(MIDDLE, false).setValue(TILE, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.getValue(TILE);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypesInit.CABLE.get().create();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext pContext) {
		VoxelShape shape = state.getValue(MIDDLE) ? PART_M : PART_C;
		if (state.getValue(NORTH) || isConnectable(world, pos, Direction.NORTH))
			shape = VoxelShapes.or(shape, PART_N);
		if (state.getValue(EAST) || isConnectable(world, pos, Direction.EAST))
			shape = VoxelShapes.or(shape, PART_E);
		if (state.getValue(SOUTH) || isConnectable(world, pos, Direction.SOUTH))
			shape = VoxelShapes.or(shape, PART_S);
		if (state.getValue(WEST) || isConnectable(world, pos, Direction.WEST))
			shape = VoxelShapes.or(shape, PART_W);
		if (state.getValue(UP) || isConnectable(world, pos, Direction.UP))
			shape = VoxelShapes.or(shape, PART_U);
		if (state.getValue(DOWN) || isConnectable(world, pos, Direction.DOWN))
			shape = VoxelShapes.or(shape, PART_D);
		return shape;
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld world,
			BlockPos pos, BlockPos pFacingPos) {
		doWithTe(world, pos, cable -> {
			cable.dirs.clear();
			for (Direction direction : Direction.values()) {
				if (isConnectable(world, pos, direction)) {
					cable.dirs.add(direction);
				}
			}
			cable.sync();
		});
		return createState(world, pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext c) {
		return createState(c.getLevel(), c.getClickedPos());
	}

	private BlockState createState(IWorld world, BlockPos pos) {
		final BlockState state = defaultBlockState();
		boolean[] north = canAttach(world, pos, Direction.NORTH);
		boolean[] south = canAttach(world, pos, Direction.SOUTH);
		boolean[] west = canAttach(world, pos, Direction.WEST);
		boolean[] east = canAttach(world, pos, Direction.EAST);
		boolean[] up = canAttach(world, pos, Direction.UP);
		boolean[] down = canAttach(world, pos, Direction.DOWN);

		boolean tile = north[1] || south[1] || west[1] || east[1] || up[1] || down[1];

		if (!tile)
			doWithTe(world, pos, cable -> cable.setRemoved());

		boolean middle = false;
		if (MathUtil.numTrue(north[0], south[0], west[0], east[0], up[0], down[0]) == 2) {
			for (Direction dir : Direction.values()) {
				if (canAttach(world, pos, dir)[0]) {
					if (canAttach(world, pos, dir.getOpposite())[0])
						middle = true;
					break;
				}
			}
		}

		//@formatter:off
		return state
				.setValue(NORTH, north[0])
				.setValue(SOUTH, south[0])
				.setValue(WEST, west[0])
				.setValue(EAST, east[0])
				.setValue(UP, up[0])
				.setValue(DOWN, down[0])
				.setValue(MIDDLE, middle)
				.setValue(TILE, tile);
		//@formatter:on
	}

	public boolean isConnectable(IBlockReader world, BlockPos pos, Direction dir) {
		TileEntity te = world.getBlockEntity(pos.relative(dir));
		return !(te instanceof CableTileEntity) && MachinaEnergyStorage.hasEnergy(te, dir);
	}

	public boolean[] canAttach(IWorld world, BlockPos pos, Direction dir) {
		boolean connectable = isConnectable(world, pos, dir);
		return new boolean[] { world.getBlockState(pos.relative(dir)).getBlock() == this || connectable, connectable };
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> b) {
		b.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, MIDDLE, TILE);
		super.createBlockStateDefinition(b);
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
		super.setPlacedBy(world, pos, pState, pPlacer, pStack);
		if (world.isClientSide())
			return;
		doWithTe(world, pos, cable -> {
			cable.search(this);
		});
	}

	@Override
	public void onPlace(BlockState pState, World world, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
		if (!doWithTe(world, pos, cable -> {
			cable.dirs.clear();
			for (Direction direction : Direction.values()) {
				if (isConnectable(world, pos, direction)) {
					cable.dirs.add(direction);
				}
			}
			cable.sync();
		})) {
			findCables(world, pos, pos);
		}
		super.onPlace(pState, world, pos, pOldState, pIsMoving);
	}

	@Override
	public void onRemove(BlockState pState, World world, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
		findCables(world, pos, pos);
		super.onRemove(pState, world, pos, pNewState, pIsMoving);
	}

	private boolean doWithTe(IWorld w, BlockPos p, Consumer<CableTileEntity> func) {
		TileEntity te = w.getBlockEntity(p);
		if (te instanceof CableTileEntity) {
			CableTileEntity cable = (CableTileEntity) te;
			func.accept(cable);
			return true;
		}
		return false;
	}

	public void findCables(IWorld world, BlockPos poss, BlockPos pos) {
		Set<BlockPos> ss = CACHE.get(poss);
		if (ss == null) {
			ss = new HashSet<>();
		}
		if (!ss.contains(pos)) {
			for (Direction direction : Direction.values()) {
				BlockPos blockPos = pos.relative(direction);
				BlockState state = world.getBlockState(blockPos);
				if (state.getBlock() == this) {
					doWithTe(world, blockPos, te -> {
						te.connectors.clear();
						te.search(this);
					});
					CableBlock cableBlock = (CableBlock) state.getBlock();
					ss.add(pos);
					CACHE.put(poss, ss);
					cableBlock.findCables(world, poss, blockPos);
				}
			}
		}
		CACHE.clear();
	}

	public void searchCables(IWorld world, BlockPos pos, CableTileEntity first, int dist) {
		int newdist = dist + 1;
		if (!first.cache.contains(pos)) {
			for (Direction direction : Direction.values()) {
				BlockPos blockPos = pos.relative(direction);
				if (blockPos.equals(first.getBlockPos()))
					continue;
				BlockState state = world.getBlockState(blockPos);
				if (state.getBlock() == this) {
					doWithTe(world, blockPos, te -> te.dirs.forEach(dir -> {
						first.connectors.add(new CableTileEntity.Connection(blockPos, dir, newdist));
					}));
					CableBlock cableBlock = (CableBlock) state.getBlock();
					first.cache.add(pos);
					cableBlock.searchCables(world, blockPos, first, newdist);
				}
			}
		}
	}
}
