package com.machina.api.block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.block.entity.ConnectorBlockEntity.Connection;
import com.machina.api.cap.IConnectorStorage;
import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.math.MathUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class ConnectorBlock<T extends IConnectorStorage> extends Block implements EntityBlock {
	public static final BooleanProperty TILE = BooleanProperty.create("tile");

	private static final VoxelShape PART_C = Block.box(6, 6, 6, 10, 10, 10);
	private static final VoxelShape PART_M = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);
	private static final VoxelShape PART_N = Block.box(6.5, 6.5, 0, 9.5, 9.5, 7);
	private static final VoxelShape PART_E = Block.box(9.5, 6.5, 6.5, 16, 9.5, 9.5);
	private static final VoxelShape PART_S = Block.box(6.5, 6.5, 9.5, 9.5, 9.5, 16);
	private static final VoxelShape PART_W = Block.box(0, 6.5, 6.5, 6.5, 9.5, 9.5);
	private static final VoxelShape PART_U = Block.box(6.5, 9.5, 6.5, 9.5, 16, 9.5);
	private static final VoxelShape PART_D = Block.box(6.5, 0, 6.5, 9.5, 7, 9.5);

	public ConnectorBlock(Properties props) {
		super(props.noOcclusion());

		this.registerDefaultState(this.stateDefinition.any().setValue(TILE, false));
	}

	public boolean[] getModelData(@NotNull BlockGetter level, BlockPos pos) {
		boolean north = canAttach(level, pos, Direction.NORTH);
		boolean south = canAttach(level, pos, Direction.SOUTH);
		boolean west = canAttach(level, pos, Direction.WEST);
		boolean east = canAttach(level, pos, Direction.EAST);
		boolean up = canAttach(level, pos, Direction.UP);
		boolean down = canAttach(level, pos, Direction.DOWN);

		boolean middle = false;
		if (MathUtil.numTrue(north, south, west, east, up, down) == 2) {
			for (Direction dir : Direction.values()) {
				if (canAttach(level, pos, dir)) {
					if (canAttach(level, pos, dir.getOpposite()))
						middle = true;
					break;
				}
			}
		}

		return new boolean[] { middle, north, east, south, west, up, down };
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
			@NotNull CollisionContext pContext) {
		boolean[] data = getModelData(level, pos);
		VoxelShape shape = data[0] ? PART_M : PART_C;
		if (data[1])
			shape = Shapes.or(shape, PART_N);
		if (data[2])
			shape = Shapes.or(shape, PART_E);
		if (data[3])
			shape = Shapes.or(shape, PART_S);
		if (data[4])
			shape = Shapes.or(shape, PART_W);
		if (data[5])
			shape = Shapes.or(shape, PART_U);
		if (data[6])
			shape = Shapes.or(shape, PART_D);
		return shape;
	}

	@SuppressWarnings("unchecked")
	private void syncConnections(LevelAccessor level, BlockPos pos) {
		BlockHelper.doWithTe(level, pos, ConnectorBlockEntity.class, cable -> {
			if (!level.isClientSide()) {
				cable.dirs.clear();
				for (Direction dir : Direction.values()) {
					if (isConnectable(level, pos, dir))
						cable.dirs.add(dir);
				}
				cable.sync();
			}
		});
	}

	private void refreshModel(LevelAccessor level, BlockPos pos) {
		if (level.isClientSide()) {
			BlockHelper.doWithTe(level, pos, BlockEntity.class, level.getModelDataManager()::requestRefresh);
		}
	}

	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing,
			@NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos,
			@NotNull BlockPos facingPos) {
		syncConnections(level, pos);
		refreshModel(level, pos);
		return createState(level, pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return createState(ctx.getLevel(), ctx.getClickedPos());
	}

	public abstract boolean canConnect(BlockEntity be, Direction dir);

	private boolean isConnectable(BlockGetter level, BlockPos pos, Direction dir) {
		BlockEntity be = level.getBlockEntity(pos.relative(dir));
		return !(be instanceof ConnectorBlockEntity) && canConnect(be, dir.getOpposite());
	}

	private boolean canAttach(BlockGetter level, BlockPos pos, Direction dir) {
		boolean connectable = isConnectable(level, pos, dir);
		return level.getBlockState(pos.relative(dir)).getBlock() == this || connectable;
	}

	private BlockState createState(BlockGetter level, BlockPos pos) {

		boolean tile = isConnectable(level, pos, Direction.NORTH) || isConnectable(level, pos, Direction.SOUTH)
				|| isConnectable(level, pos, Direction.WEST) || isConnectable(level, pos, Direction.EAST)
				|| isConnectable(level, pos, Direction.UP) || isConnectable(level, pos, Direction.DOWN);

		if (!tile)
			BlockHelper.doWithTe(level, pos, ConnectorBlockEntity.class, ConnectorBlockEntity::setRemoved);

		return defaultBlockState().setValue(TILE, tile);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> b) {
		b.add(TILE);
		super.createBlockStateDefinition(b);
	}

	// TODO: Change on hit to open GUI
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
//		System.out.println(hit.getLocation().toString());

		return InteractionResult.FAIL;
	}

	@Override
	public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer,
			@NotNull ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (level.isClientSide())
			return;

		if (!BlockHelper.doWithTe(level, pos, ConnectorBlockEntity.class, ConnectorBlockEntity::enqueueSearch)) {
			findConnectors(level, pos, pos);
		}
	}

	@Override
	public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState old,
			boolean moving) {
		if (level.isClientSide())
			return;

		syncConnections(level, pos);

		super.onPlace(state, level, pos, old, moving);
	}

	@Override
	public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
			@NotNull BlockState newState, boolean moving) {
		findConnectors(level, pos, pos);
		super.onRemove(state, level, pos, newState, moving);
	}

	protected abstract Map<BlockPos, Set<BlockPos>> getCache();

	@SuppressWarnings("unchecked")
	public void findConnectors(LevelAccessor world, BlockPos poss, BlockPos pos) {
		Set<BlockPos> ss = getCache().get(poss);
		if (ss == null)
			ss = new HashSet<>();

		if (!ss.contains(pos)) {
			for (Direction direction : Direction.values()) {
				BlockPos blockPos = pos.relative(direction);
				Block block = world.getBlockState(blockPos).getBlock();
				if (block == this) {
					BlockHelper.doWithTe(world, blockPos, ConnectorBlockEntity.class,
							ConnectorBlockEntity::enqueueSearch);
					ss.add(pos);
					getCache().put(poss, ss);
					((ConnectorBlock<T>) block).findConnectors(world, poss, blockPos);
				}
			}
		}
		getCache().clear();
	}

	@SuppressWarnings({ "unchecked" })
	public void searchConnectors(LevelAccessor world, BlockPos pos, ConnectorBlockEntity<T> first, int dist) {
		int newdist = dist + 1;
		for (Direction dir : Direction.values()) {
			BlockPos blockPos = pos.relative(dir);

			if (!first.isInCache(blockPos)) {
				if (!blockPos.equals(first.getBlockPos())) {

					Block block = world.getBlockState(blockPos).getBlock();
					if (block == this) {
						BlockHelper.doWithTe(world, blockPos, ConnectorBlockEntity.class,
								be -> be.dirs.forEach(d -> first.connectors
										.add(new Connection(blockPos, (Direction) d, newdist, ConnectionSide.INPUT))));
						first.addToCache(blockPos);
						((ConnectorBlock<T>) block).searchConnectors(world, blockPos, first, newdist);
					}
				}
			}
		}
	}

	protected abstract BlockEntityType<? extends ConnectorBlockEntity<T>> getBlockEntityType();

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return getBlockEntityType().create(pos, state);
	}

	@Override
	public <E extends BlockEntity> BlockEntityTicker<E> getTicker(@NotNull Level level, @NotNull BlockState state,
			@NotNull BlockEntityType<E> type) {
		return type == getBlockEntityType() ? ConnectorBlockEntity::tick : null;
	}

}
