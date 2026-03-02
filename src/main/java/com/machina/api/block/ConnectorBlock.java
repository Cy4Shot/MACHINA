package com.machina.api.block;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.block.entity.ConnectorBlockEntity.Connection;
import com.machina.api.block.menu.DirectionalMenuFactory;
import com.machina.api.block.menu.IDirectionalMenuProvider;
import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.reflect.QuintFunction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ConnectorBlock extends Block implements EntityBlock, IClickableBlock, IDirectionalMenuProvider {
	public static final BooleanProperty TILE = BooleanProperty.create("tile");

	private static final VoxelShape PART_C = Block.box(6, 6, 6, 10, 10, 10);
	private static final VoxelShape PART_M = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);
	private static final VoxelShape PART_N = Block.box(6.5, 6.5, 0, 9.5, 9.5, 7);
	private static final VoxelShape PART_E = Block.box(9.5, 6.5, 6.5, 16, 9.5, 9.5);
	private static final VoxelShape PART_S = Block.box(6.5, 6.5, 9.5, 9.5, 9.5, 16);
	private static final VoxelShape PART_W = Block.box(0, 6.5, 6.5, 6.5, 9.5, 9.5);
	private static final VoxelShape PART_U = Block.box(6.5, 9.5, 6.5, 9.5, 16, 9.5);
	private static final VoxelShape PART_D = Block.box(6.5, 0, 6.5, 9.5, 7, 9.5);

	private static final VoxelShape CONN_N = Block.box(6, 6, 0, 10, 10, 3);
	private static final VoxelShape CONN_E = Block.box(13, 6, 6, 16, 10, 10);
	private static final VoxelShape CONN_S = Block.box(6, 6, 13, 10, 10, 16);
	private static final VoxelShape CONN_W = Block.box(0, 6, 6, 3, 10, 10);
	private static final VoxelShape CONN_U = Block.box(6, 13, 6, 10, 16, 10);
	private static final VoxelShape CONN_D = Block.box(6, 0, 6, 10, 3, 10);

	private static final VoxelShape[] PARTS = new VoxelShape[] { PART_D, PART_U, PART_N, PART_S, PART_W, PART_E };
	private static final VoxelShape[] CONNS = new VoxelShape[] { CONN_D, CONN_U, CONN_N, CONN_S, CONN_W, CONN_E };

	private static final boolean[] DEFAULT_MODEL = new boolean[] { false, false, false, false, false, false, false };

	public ConnectorBlock(Properties props) {
		super(props.noOcclusion());

		this.registerDefaultState(this.stateDefinition.any().setValue(TILE, false));
	}

	public boolean[] getModelData(@NotNull Level level, BlockPos pos) {
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

		return new boolean[] { down, up, north, south, west, east, middle };
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
			@NotNull CollisionContext pContext) {
		BlockEntity be = level.getBlockEntity(pos);
		boolean[] data;
		if (be != null) {
			data = getModelData(be.getLevel(), pos);
		} else {
			data = DEFAULT_MODEL;
		}
		VoxelShape shape = data[6] ? PART_M : PART_C;
		for (int i = 0; i < 6; i++) {
			if (data[i])
				shape = Shapes.or(shape, PARTS[i]);
		}

		if (be != null && be instanceof ConnectorBlockEntity<?, ?> cable) {
			for (Direction d : Direction.values()) {
				if (data[d.get3DDataValue()] && cable.getConnection(d).isIO()) {
					shape = Shapes.or(shape, CONNS[d.get3DDataValue()]);
				}
			}
		}

		return shape;
	}

	@SuppressWarnings("unchecked")
	public void syncConnections(Level level, BlockPos pos) {
		BlockHelper.doWithTe(level, pos, ConnectorBlockEntity.class, cable -> {
			if (!level.isClientSide()) {
				cable.dirs.clear();
				for (Direction dir : Direction.values()) {
					if (isConnectable(level, pos, dir))
						cable.dirs.add(dir);
				}
			}
		});
	}

	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing,
			@NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos,
			@NotNull BlockPos facingPos) {
		if (level instanceof ClientLevel clientLevel) {
			if (level.isClientSide() && clientLevel.getModelDataManager() != null) {
				BlockHelper.doWithTe(level, pos, BlockEntity.class, clientLevel.getModelDataManager()::requestRefresh);
			}
		} else {
			if (!BlockHelper.doWithTe(level, pos, ConnectorBlockEntity.class, ConnectorBlockEntity::enqueueSearch)) {
				findConnectors(level, pos, pos);
			}
		}
		return createState(level.getBlockEntity(pos).getLevel(), pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return createState(ctx.getLevel(), ctx.getClickedPos());
	}

	public abstract boolean canConnect(Level level, BlockPos be, Direction dir);

	private boolean isConnectable(Level level, BlockPos pos, Direction dir) {
		BlockEntity be = level.getBlockEntity(pos.relative(dir));
		return !(be instanceof ConnectorBlockEntity) && canConnect(level, pos.relative(dir), dir.getOpposite());
	}

	private boolean canAttach(Level level, BlockPos pos, Direction dir) {
		boolean connectable = isConnectable(level, pos, dir);
		return level.getBlockState(pos.relative(dir)).getBlock() == this || connectable;
	}

	private BlockState createState(Level level, BlockPos pos) {

		boolean tile = isConnectable(level, pos, Direction.NORTH) || isConnectable(level, pos, Direction.SOUTH)
				|| isConnectable(level, pos, Direction.WEST) || isConnectable(level, pos, Direction.EAST)
				|| isConnectable(level, pos, Direction.UP) || isConnectable(level, pos, Direction.DOWN);

		// TODO: What do we do about dead tile entities? If we don't tick its okay?
//		BlockEntity be = level.getBlockEntity(pos);
//		if (be != null) {
//			if (tile) {
//				be.clearRemoved();
//			} else {
//				be.setRemoved();
//			}
//		}

		return defaultBlockState().setValue(TILE, tile);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> b) {
		b.add(TILE);
		super.createBlockStateDefinition(b);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof ConnectorBlockEntity<?, ?> cable) {
			Vec3 offset = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

			for (Direction d : Direction.values()) {
				ConnectionSide side = cable.getConnection(d);
				if (side.isIO()) {
					if (CONNS[d.get3DDataValue()].bounds().distanceToSqr(offset) < 0.001f) {
						if (player.isShiftKeyDown()) {
							if (!level.isClientSide()) {
								DirectionalMenuFactory.create((ServerPlayer) player, this, pos, d);
								return ItemInteractionResult.SUCCESS;
							}
							return ItemInteractionResult.CONSUME;
						}
						cable.setConnection(d, side.toggleIO());
						return ItemInteractionResult.SUCCESS;
					}
				}
			}
		}

		return ItemInteractionResult.FAIL;
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
					((ConnectorBlock) block).findConnectors(world, poss, blockPos);
				}
			}
		}
		getCache().clear();
	}

	@SuppressWarnings("unchecked")
	public void searchConnectors(LevelAccessor world, BlockPos pos, ConnectorBlockEntity<?, ?> first, int dist) {
		int newdist = dist + 1;
		for (Direction dir : Direction.values()) {
			BlockPos blockPos = pos.relative(dir);

			if (!first.isInCache(blockPos)) {
				if (!blockPos.equals(first.getBlockPos())) {

					Block block = world.getBlockState(blockPos).getBlock();
					if (block == this) {
						BlockHelper.doWithTe(world, blockPos, ConnectorBlockEntity.class, be -> be.dirs
								.forEach(d -> first.connectors.add(new Connection(blockPos, (Direction) d, newdist))));
						first.addToCache(blockPos);
						((ConnectorBlock) block).searchConnectors(world, blockPos, first, newdist);
					}
				}
			}
		}
	}

	protected abstract BlockEntityType<? extends ConnectorBlockEntity<?, ?>> getBlockEntityType();

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return getBlockEntityType().create(pos, state);
	}

	@Override
	public <E extends BlockEntity> BlockEntityTicker<E> getTicker(@NotNull Level level, @NotNull BlockState state,
			@NotNull BlockEntityType<E> type) {
		return type == getBlockEntityType() ? ConnectorBlockEntity::tick : null;
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player, BlockPos pos, Direction d) {
		if (getMenu() != null) {
			return getMenu().apply(id, inv, ContainerLevelAccess.create(player.level(), pos),
                    player.level().getCapability(Capabilities.ItemHandler.BLOCK, pos, d), d);
		}
		return null;
	}

    public abstract QuintFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, Direction, AbstractContainerMenu> getMenu();
}
